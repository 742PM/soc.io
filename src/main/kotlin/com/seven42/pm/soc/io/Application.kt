package com.seven42.pm.soc.io

import com.seven42.pm.soc.io.telegram.TelegramBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

class Application

fun main(args: Array<String>) {
	val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
	telegramBotsApi.registerBot(TelegramBot())
}
