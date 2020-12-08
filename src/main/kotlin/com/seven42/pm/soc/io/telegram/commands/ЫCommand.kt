package com.seven42.pm.soc.io.telegram.commands

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

class ЫCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = "ы".repeat(userMessage.length)

    override fun isValid(userMessage: String): Boolean = true

    override fun getKeyboard(): ReplyKeyboardMarkup? {
        val row = KeyboardRow()
        row.add("кнопка")
        row.add("еще кнопка")

        return ReplyKeyboardMarkup
                .builder()
                .resizeKeyboard(true)
                .keyboardRow(row)
                .build()
    }
}