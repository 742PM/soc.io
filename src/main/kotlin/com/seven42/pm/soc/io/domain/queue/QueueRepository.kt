package com.seven42.pm.soc.io.domain.queue

import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import org.jetbrains.exposed.sql.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface QueueRepository {
    //сюда добавим методов на чтение по необходимости
    //cхема такая:
    // | айди юзера |
    fun put(user: User): Unit
    fun remove(user: User): Unit
    fun all(user: User): List<User>
    fun contains(user: User): Boolean
}

const val UsersQueueTable = "users_queue"

class SqlQueueRepository {
    private val logger: Logger = LoggerFactory.getLogger(SqlQueueRepository::class.java)
    fun put(user: User): Unit {
        logger.info("Inserting ${user.id} in $UsersQueueTable")
        UserModel.insert {
            it[id] = user.id.value
        }
    }

    fun remove(user: User): Unit {
        logger.info("Removing ${user.id} from $UsersQueueTable")
        UserModel.deleteWhere {
            UserModel.id eq user.id.value
        }
    }

    fun all(): List<User> {
        logger.info("Getting all users from $UsersQueueTable")
        return UserModel.selectAll().map { userFromResult(it) }
    }

    fun contains(user: User): Boolean {
        logger.info("Checking if ${user.id} in $UsersQueueTable")
        return all().contains(user)
    }
}

object UserModel : Table(UsersQueueTable) {
    val id = text("id")
}

private fun userFromResult(it: ResultRow) = User(
        id = UserId(it[UserModel.id])
)