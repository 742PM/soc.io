package com.seven42.pm.soc.io

import com.seven42.pm.soc.io.domain.conversation.ConversationServiceImpl
import com.seven42.pm.soc.io.domain.conversation.SqlConversationRepository
import com.seven42.pm.soc.io.domain.queue.SqlQueueRepository
import com.seven42.pm.soc.io.telegram.TelegramBot
import com.seven42.pm.soc.io.telegram.commands.DequeueCommand
import com.seven42.pm.soc.io.telegram.commands.EnqueueCommand
import com.seven42.pm.soc.io.telegram.commands.StartCommand
import com.seven42.pm.soc.io.telegram.commands.ЫCommand
import org.jetbrains.exposed.sql.Database
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

	val conversationRepository = SqlConversationRepository()
	val queueRepository = SqlQueueRepository()
	val conversationService = ConversationServiceImpl(conversationRepository, queueRepository)

	val bot = TelegramBot(
			StartCommand(),
			EnqueueCommand(conversationService),
			DequeueCommand(conversationService),
			ЫCommand()
	)

	telegramBotsApi.registerBot(bot)
}
