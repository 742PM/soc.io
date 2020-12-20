package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.UserId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
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

object Conversations : Table(ConversationsTable) {
    val id = text("conversations_id") // TODO make auto generation for id?
    val user_from_id = text("user_from_id")
    val user_to_id = text("user_to_id")
    val startedAt = date("started_at")
    val endAt = date("end_at").nullable()
}

class SqlConversationRepository : ConversationRepository {
    override fun insert(info: ConversationMetaInformation) {
        transaction {
            Conversations.insert {
                it[id] = info.id.value
                it[user_from_id] = info.firstInterlocutor.value
                it[user_to_id] = info.secondInterlocutor.value
                it[startedAt] = info.startedAt
                it[endAt] = info.endedAt
            }
        }
    }

    override fun update(info: ConversationMetaInformation) {
        transaction {
            Conversations.update({ Conversations.id eq info.id.value }) {
                it[user_from_id] = info.firstInterlocutor.value
                it[user_to_id] = info.secondInterlocutor.value
                it[startedAt] = info.startedAt
                it[endAt] = info.endedAt
            }
        }
    }

    override fun find(id: ConversationId): ConversationMetaInformation? = transaction {
        val info = Conversations
                .select { Conversations.id eq id.value }
                .limit(1)
                .firstOrNull()
        if (info != null) {
            conversationInfoFromResult(info)
        }
        null
    }

    override fun all(): List<ConversationMetaInformation> = transaction {
        Conversations
                .selectAll()
                .map { conversationInfoFromResult(it) }
    }

    override fun findForUser(userId: UserId): ConversationMetaInformation? = transaction {
        val info = Conversations
                .select {
                    (Conversations.user_from_id eq userId.value and Conversations.endAt.isNull()) or
                            (Conversations.user_to_id eq userId.value and Conversations.endAt.isNull())
                }
                .limit(1)
                .firstOrNull()
        if (info != null) {
            conversationInfoFromResult(info)
        }
        null
    }
}

private fun conversationInfoFromResult(it: ResultRow) = ConversationMetaInformation(
        id = ConversationId(it[Conversations.id]),
        firstInterlocutor = UserId(it[Conversations.user_from_id]),
        secondInterlocutor = UserId(it[Conversations.user_to_id]),
        startedAt = it[Conversations.startedAt],
        endedAt = it[Conversations.endAt]
)
