package com.seven42.pm.soc.io.domain.queue

import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import org.jetbrains.exposed.sql.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


interface QueueRepository {

    fun remove(userId: UserId): Unit
    fun contains(userId: UserId): Boolean
    fun all(): List<UserId>
    fun put(user: UserId): Unit
}

const val UsersQueueTable = "users_queue"

class SqlQueueRepository : QueueRepository {
    private val logger: Logger = LoggerFactory.getLogger(SqlQueueRepository::class.java)


    override fun put(user: UserId): Unit {
        logger.info("Inserting ${user} in $UsersQueueTable")
        UserModel.insert {
            it[id] = user.value
        }
    }

    override fun remove(userId: UserId) {
        logger.info("Removing $userId from $UsersQueueTable")
        UserModel.deleteWhere {
            UserModel.id eq userId.value
        }
    }



    override fun all(): List<UserId> {
        logger.info("Getting all users from $UsersQueueTable")
        return UserModel.selectAll().map { userFromResult(it).id }
    }

    override fun contains(userId: UserId): Boolean {
        logger.info("Checking if ${userId} in $UsersQueueTable")
        return all().contains(userId)
    }
}

object UserModel : Table(UsersQueueTable) {
    val id = text("id")
}

private fun userFromResult(it: ResultRow) = User(
        id = UserId(it[UserModel.id])
)