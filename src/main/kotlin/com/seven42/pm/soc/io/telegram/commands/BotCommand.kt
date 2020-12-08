package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.telegram.KeyboardButtons
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.bots.AbsSender

abstract class BotCommand {
    abstract fun getMessageText(userMessage: String): String

    abstract fun isValid(userMessage: String): Boolean

    abstract fun getKeyboardButtons(): List<String>

    fun execute(bot: AbsSender, userMessage: String, chatId: Long) {
        val keyboard = buildKeyboard(getKeyboardButtons())

        val sendMessage = SendMessage
                .builder()
                .chatId(chatId.toString())
                .text(getMessageText(userMessage))
                .replyMarkup(keyboard)
                .build()

        bot.execute(sendMessage)
    }

    private fun buildKeyboard(buttons: List<String>): ReplyKeyboard {
        if (buttons.isEmpty())
            return ReplyKeyboardRemove(true)

        val row = KeyboardRow()
        row.addAll(buttons)

        return ReplyKeyboardMarkup
                .builder()
                .resizeKeyboard(true)
                .keyboardRow(row)
                .build()
    }
}