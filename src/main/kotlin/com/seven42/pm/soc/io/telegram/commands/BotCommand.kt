package com.seven42.pm.soc.io.telegram.commands

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.bots.AbsSender

abstract class BotCommand {
    abstract fun getMessageText(userMessage: String): String

    abstract fun isValid(userMessage: String): Boolean

    abstract fun getKeyboard(): ReplyKeyboardMarkup?

    fun execute(bot: AbsSender, userMessage: String, chatId: Long) {
        val sendMessage = SendMessage
                .builder()
                .chatId(chatId.toString())
                .text(getMessageText(userMessage))
                .replyMarkup(getKeyboard() ?: ReplyKeyboardRemove(true))
                .build()

        bot.execute(sendMessage)
    }
}