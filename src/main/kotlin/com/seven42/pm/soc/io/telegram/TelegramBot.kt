package com.seven42.pm.soc.io.telegram

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.telegram.commands.BotCommand
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

class TelegramBot(private vararg val commands: BotCommand) : TelegramLongPollingBot() {
    override fun getBotToken(): String = TelegramParams.Token

    override fun getBotUsername(): String = TelegramParams.Username

    override fun onUpdateReceived(update: Update) {
        if (!update.hasMessage() || !update.message.hasText())
            return

        val userId = UserId(update.message.chatId.toString())
        val command = commands.firstOrNull { it.isValid(update.message.text, userId) } ?: return

        command.execute(this, update.message.text, update.message.chatId)
    }
}
