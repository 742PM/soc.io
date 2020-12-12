package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.TelegramUserInfo
import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.queue.SocialQueue
import com.seven42.pm.soc.io.domain.queue.UsersQueueTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.ZonedDateTime

class ConversationService(val socialQueue: SocialQueue) {
    private val logger: Logger = LoggerFactory.getLogger(ConversationService::class.java)

    /**  Из системы выкидывает текущего пользователя,  а собеседника возвращает в очередь */
    fun StopDialog(userId: UserId): Void = TODO()

    /** Завершает диалог для обоих пользователей и пытается найти собеседника для [[userId]]*/
    fun ChangeHuman(userId: UserId): Boolean = TODO()

    /**  Пытается найти собеседника для [[userId]]
     *
     * Если в системе в текущий момент есть собеседник, то начинает диалог с ним и возвращает `true`.
     * Иначе добавляет пользователя в список ожидания и возвращает `false`.
     */
    fun StartDialog(user: User): Boolean = TODO()

    fun GetCurrentInterlocutorInfo(userId: UserId): TelegramUserInfo = TODO()

    fun IsInDialog(userId: UserId): Boolean = TODO()

    fun HasInterlocutor(userId: UserId): Boolean = TODO()
}

data class ConversationMetaInformation(val id: ConversationId, val firstInterlocutor: UserId, val secondInterlocutor: UserId, val startedAt: DateTime, val endedAt: DateTime)
inline class ConversationId(val value: String)