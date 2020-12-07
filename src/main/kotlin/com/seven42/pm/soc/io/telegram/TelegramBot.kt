package com.seven42.pm.soc.io.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class TelegramBot : TelegramWebhookBot() {
    override fun getBotToken(): String = TelegramParams.Token

    override fun getBotUsername(): String = TelegramParams.Username

    override fun getBotPath(): String = "/$TelegramParams.Token"

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? {
        if (update.hasMessage() && update.message.hasText()) {
            val sendMessage = SendMessage()
            sendMessage.chatId = update.message.chatId.toString()
            sendMessage.text = "ы".repeat(update.message.text.length)
            return sendMessage
        }

        return null
    }
}