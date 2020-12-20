package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.TelegramUserInfo
import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.queue.QueueRepository
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ConversationService(val conversationRepository: ConversationRepository, val queueRepository: QueueRepository) {
    private val logger: Logger = LoggerFactory.getLogger(ConversationService::class.java)

    /**  Из системы выкидывает текущего пользователя,  а собеседника возвращает в очередь */
    fun StopDialog(userId: UserId) {
        val conversation = conversationRepository.findForUser(userId)
        if (conversation != null) {
            conversationRepository.update(
                ConversationMetaInformation(
                    conversation.id,
                    conversation.firstInterlocutor,
                    conversation.secondInterlocutor,
                    conversation.startedAt,
                    DateTime.now()
                )
            )
            if (userId != conversation.firstInterlocutor)
                queueRepository.put(userId)
            else
                queueRepository.put(conversation.firstInterlocutor)
        } else {
            throw Exception("User is in no dialog")
        }


    }

    /** Завершает диалог для обоих пользователей и пытается найти собеседника для [[userId]]*/
    fun ChangeHuman(userId: UserId) {
        val conversation = conversationRepository.findForUser(userId)
        if (conversation != null) {
            conversationRepository.update(
                ConversationMetaInformation(
                    conversation.id,
                    conversation.firstInterlocutor,
                    conversation.secondInterlocutor,
                    conversation.startedAt,
                    DateTime.now()
                )
            )
            queueRepository.put(userId)

        } else {
            throw Exception("User is in no dialog")
        }
    }

    /**  Пытается найти собеседника для [[userId]]
     *
     * Если в системе в текущий момент есть собеседник, то начинает диалог с ним и возвращает `true`.
     * Иначе добавляет пользователя в список ожидания и возвращает `false`.
     */
    fun StartDialog(user: User): Boolean {
        val all = queueRepository.all()
        if (all.isNotEmpty()) {
            val interlocutor = all.filter { it != user.id }.random()
            conversationRepository.insert(
                ConversationMetaInformation(
                    ConversationId(UUID.randomUUID().toString()),
                    user.id,
                    interlocutor,
                    DateTime.now(),
                    null
                )
            )
            queueRepository.remove(interlocutor)
            return true
        } else {
            queueRepository.put(user.id)
            return false
        }
    }

    fun GetCurrentInterlocutorInfo(userId: UserId): UserId {
        val conversation = conversationRepository.findForUser(userId)
        if (conversation != null) {
            return if (conversation.firstInterlocutor == userId)
                conversation.secondInterlocutor
            else
                conversation.firstInterlocutor
        } else {
            throw Exception("User is in no dialog")

        }
    }

    fun LeaveQueue(userId: UserId) {
        if (queueRepository.contains(userId))
            queueRepository.remove(userId)
    }

    fun IsInQueue(userId: UserId): Boolean {
        return queueRepository.contains(userId)
    }

    fun HasInterlocutor(userId: UserId): Boolean {
        return conversationRepository.findForUser(userId) != null
    }
}

