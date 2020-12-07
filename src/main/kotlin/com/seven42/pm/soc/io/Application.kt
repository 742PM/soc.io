package com.seven42.pm.soc.io

import com.seven42.pm.soc.io.telegram.TelegramBot
import com.seven42.pm.soc.io.telegram.TelegramParams
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.generics.Webhook
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook

open class Application {
	companion object {
		@JvmStatic fun main(args: Array<String>) {
			try {
				val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java, getWebhook())
				val setWebhook = SetWebhook(TelegramParams.Url)
				telegramBotsApi.registerBot(TelegramBot(DefaultBotOptions()), setWebhook)
			} catch (e: TelegramApiException) {
				e.printStackTrace()
			}
		}

		private fun getWebhook(): Webhook {
			val webhook = DefaultWebhook()
			webhook.setInternalUrl(TelegramParams.Webhook)
			return webhook
		}
	}
}
