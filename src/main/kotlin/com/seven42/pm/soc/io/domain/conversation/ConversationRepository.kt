package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.UserId
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

interface ConversationRepository {
    fun insert(info: ConversationMetaInformation)
    fun find(id: ConversationId): ConversationMetaInformation?
    fun all(): List<ConversationMetaInformation>
    fun findForUser(userId: UserId): ConversationMetaInformation?
    fun update(info: ConversationMetaInformation)
}

data class ConversationMetaInformation(
    val id: ConversationId,
    val firstInterlocutor: UserId,
    val secondInterlocutor: UserId,
    val startedAt: DateTime,
    val endedAt: DateTime?
)

inline class ConversationId(val value: String)

const val ConversationsTable = "conversations"

object ConversationInfo : Table(ConversationsTable) {
    val id = text("conversations_id") // TODO make auto generation for id?
    val user_from_id = text("user_from_id")
    val user_to_id = text("user_to_id")
    val startedAt = date("started_at")
    val endAt = date("end_at").nullable()
}

class SqlConversationRepository : ConversationRepository {
    override fun insert(info: ConversationMetaInformation) {
        ConversationInfo.insert {
            it[id] = info.id.value
            it[user_from_id] = info.firstInterlocutor.value
            it[user_to_id] = info.secondInterlocutor.value
            it[startedAt] = info.startedAt
            it[endAt] = info.endedAt
        }
    }

    override fun update(info: ConversationMetaInformation) {
        ConversationInfo.update({ ConversationInfo.id eq info.id.value }) {
            it[user_from_id] = info.firstInterlocutor.value
            it[user_to_id] = info.secondInterlocutor.value
            it[startedAt] = info.startedAt
            it[endAt] = info.endedAt
        }
    }

    override fun find(id: ConversationId): ConversationMetaInformation? {
        val info = ConversationInfo
            .select { ConversationInfo.id eq id.value }
            .limit(1)
            .firstOrNull()
        if (info != null) {
            return conversationInfoFromResult(info)
        }
        return null
    }

    override fun all(): List<ConversationMetaInformation> {
        return ConversationInfo
            .selectAll()
            .map { conversationInfoFromResult(it) }
    }

    override fun findForUser(userId: UserId): ConversationMetaInformation? {
        val info = ConversationInfo
            .select {
                (ConversationInfo.user_from_id eq userId.value and ConversationInfo.endAt.isNull()) or
                        (ConversationInfo.user_to_id eq userId.value and ConversationInfo.endAt.isNull())
            }
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
