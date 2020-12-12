package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.queue.UserModel
import com.seven42.pm.soc.io.domain.queue.UsersQueueTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import java.time.LocalDate
import java.time.ZonedDateTime

interface ConversationRepository {
    //сюда добавим методов на чтение по необходимости
    //cхема такая:
    // | собеседник1 id | собеседник2 id | датавремя начала диалога | датавремя конца диалога |
    fun insert(info: ConversationMetaInformation)
    fun find(id: ConversationId): ConversationMetaInformation?
    fun all(): List<ConversationMetaInformation>
    fun findForUser(userId: UserId): ConversationMetaInformation?
}

const val ConversationsTable = "conversations"

object ConversationInfo : Table(ConversationsTable) {
    val id = text("conversations_id")
    val user_from_id = text("user_from_id")
    val user_to_id = text("user_to_id")
    val startedAt = date("started_at")
    val endAt = date("end_at")
}

class SqlConversationRepository {
    fun insert(info: ConversationMetaInformation) {
        ConversationInfo.insert {
            it[id] = info.id.value
            it[user_from_id] = info.firstInterlocutor.value
            it[user_to_id] = info.secondInterlocutor.value
            it[startedAt] = info.startedAt
            it[endAt] = info.endedAt
        }
    }

    fun find(id: ConversationId): ConversationMetaInformation? {
        val info = ConversationInfo
                .select { ConversationInfo.id eq id.value }
                .limit(1)
                .firstOrNull()
        if (info != null) {
            return conversationInfoFromResult(info)
        }
        return null
    }

    fun all(): List<ConversationMetaInformation> {
        return ConversationInfo
                .selectAll()
                .map { conversationInfoFromResult(it) }
    }

    fun findForUser(userId: UserId): ConversationMetaInformation? {
        val info = ConversationInfo
                .select { (ConversationInfo.user_from_id eq userId.value) or (ConversationInfo.user_to_id eq userId.value) }
                .limit(1)
                .firstOrNull()
        if (info != null) {
            return conversationInfoFromResult(info)
        }
        return null
    }
}

private fun conversationInfoFromResult(it: ResultRow) = ConversationMetaInformation(
        id = ConversationId(it[ConversationInfo.id]),
        firstInterlocutor = UserId(it[ConversationInfo.user_from_id]),
        secondInterlocutor = UserId(it[ConversationInfo.user_to_id]),
        startedAt = it[ConversationInfo.startedAt],
        endedAt = it[ConversationInfo.endAt]
)