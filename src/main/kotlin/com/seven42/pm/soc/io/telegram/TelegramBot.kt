package com.seven42.pm.soc.io.telegram

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class TelegramBot : TelegramLongPollingBot() {
    override fun getBotToken(): String = TelegramParams.Token

    override fun getBotUsername(): String = TelegramParams.Username

    override fun onUpdateReceived(update: Update) {
        if (!update.hasMessage() || !update.message.hasText())
            return

        val sendMessage = SendMessage
                .builder()
                .chatId(update.message.chatId.toString())
                .text("ы".repeat(update.message.text.length))
                .build()
        execute(sendMessage)
    }
}
