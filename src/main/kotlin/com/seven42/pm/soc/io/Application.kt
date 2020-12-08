package com.seven42.pm.soc.io

import com.seven42.pm.soc.io.telegram.TelegramBot
import com.seven42.pm.soc.io.telegram.commands.DequeueCommand
import com.seven42.pm.soc.io.telegram.commands.EnqueueCommand
import com.seven42.pm.soc.io.telegram.commands.StartCommand
import com.seven42.pm.soc.io.telegram.commands.ЫCommand
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

class Application

fun main(args: Array<String>) {
	val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)

	val bot = TelegramBot(
			StartCommand(),
			EnqueueCommand(),
			DequeueCommand(),
			ЫCommand()
	)

	telegramBotsApi.registerBot(bot)
}
