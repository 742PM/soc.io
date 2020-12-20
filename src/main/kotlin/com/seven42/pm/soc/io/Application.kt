package com.seven42.pm.soc.io

import com.seven42.pm.soc.io.domain.conversation.ConversationServiceImpl
import com.seven42.pm.soc.io.domain.conversation.Conversations
import com.seven42.pm.soc.io.domain.conversation.SqlConversationRepository
import com.seven42.pm.soc.io.domain.queue.Queue
import com.seven42.pm.soc.io.domain.queue.SqlQueueRepository
import com.seven42.pm.soc.io.telegram.QueueManager
import com.seven42.pm.soc.io.telegram.TelegramBot
import com.seven42.pm.soc.io.telegram.commands.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

class Application

fun main(args: Array<String>) {
	val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)

	Database.connect(
			System.getenv("JDBC_DATABASE_URL"),
			"org.postgresql.Driver",
			System.getenv("JDBC_DATABASE_USERNAME"),
			System.getenv("JDBC_DATABASE_PASSWORD"))

	transaction {
		SchemaUtils.create(Queue, Conversations)
	}

	val conversationRepository = SqlConversationRepository()
	val queueRepository = SqlQueueRepository()
	val conversationService = ConversationServiceImpl(conversationRepository, queueRepository)

	val bot = TelegramBot(
			StartCommand(),
			EnqueueCommand(conversationService),
			DequeueCommand(conversationService),
			ChangeDialogCommand(conversationService),
			StopDialogCommand(conversationService),
			ResendCommand(conversationService)
	)

	val queueManager = QueueManager(conversationService, bot, StartDialogCommand())
	queueManager.run()

	telegramBotsApi.registerBot(bot)
}
