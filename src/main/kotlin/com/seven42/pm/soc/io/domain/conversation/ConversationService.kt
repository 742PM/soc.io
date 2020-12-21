package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.TelegramUserInfo
import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.queue.QueueRepository
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import org.apache.log4j.Logger
import java.util.*

interface ConversationService {

    /**  Из системы выкидывает текущего пользователя,  а собеседника возвращает в очередь */
    fun StopDialog(userId: UserId)

    /** Завершает диалог для обоих пользователей и пытается найти собеседника для [[userId]]*/
    fun ChangeInterlocutor(userId: UserId)

    /** Собирает пары собеседников из всех пользователей в очереди */
    fun StartDialogs(): List<UserId>

    /** Добавляет пользователя в очередь
     *
     * Если пользователь уже в очереди, то возвращает false, иначе true
     */
    fun EnterQueue(userId: UserId): Boolean
    fun HasInterlocutor(userId: UserId): Boolean
    fun IsInQueue(userId: UserId): Boolean

    /** Удаляет пользователя из очереди
     *
     * Если пользователь уже вне очереди, то возвращает false, иначе true
     */
    fun LeaveQueue(userId: UserId): Boolean
    fun GetCurrentInterlocutorInfo(userId: UserId): UserId
}

class ConversationServiceImpl(
    val conversationRepository: ConversationRepository,
    val queueRepository: QueueRepository
) : ConversationService {
    private val logger: Logger = Logger.getLogger(ConversationService::class.java)
//    private val logger: Logger = LoggerFactory.getLogger(ConversationService::class.java)


    override fun StopDialog(userId: UserId) {
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
                queueRepository.put(conversation.firstInterlocutor)
            else
                queueRepository.put(conversation.secondInterlocutor)
        } else {
            throw Exception("User is in no dialog")
        }


    }

    override fun ChangeInterlocutor(userId: UserId) {
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
            queueRepository.put(conversation.firstInterlocutor)
            queueRepository.put(conversation.secondInterlocutor)

        } else {
            throw Exception("User is in no dialog")
        }
    }


    override fun StartDialogs(): List<UserId> {
        val all = queueRepository.all()
        return all.shuffled().windowed(2, 2).filter { it.size == 2 }.map {
            conversationRepository.insert(
                ConversationMetaInformation(
                    ConversationId(UUID.randomUUID().toString()),
                    it[0],
                    it[1],
                    DateTime.now(),
                    null
                )
            )
            queueRepository.remove(it[0])
            queueRepository.remove(it[1])
            it
        }.flatten()
    }

    override fun EnterQueue(userId: UserId): Boolean {
        if (queueRepository.contains(userId))
            return false
        queueRepository.put(userId)
        return true
    }

    override fun GetCurrentInterlocutorInfo(userId: UserId): UserId {
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

    override fun LeaveQueue(userId: UserId): Boolean {
        if (!queueRepository.contains(userId))
            return false

        queueRepository.remove(userId)
        return true
    }

    override fun IsInQueue(userId: UserId): Boolean {
        return queueRepository.contains(userId)
    }

    override fun HasInterlocutor(userId: UserId): Boolean {
        return conversationRepository.findForUser(userId) != null
    }
}

