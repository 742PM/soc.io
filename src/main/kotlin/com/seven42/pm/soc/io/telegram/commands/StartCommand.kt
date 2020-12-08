package com.seven42.pm.soc.io.telegram.commands

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

class StartCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
Привет!
Этот бот позволяет общаться со случайным собеседником.
Если тебе одиноко или просто скучно, нажми на кнопку внизу
😉
"""

    override fun isValid(userMessage: String): Boolean = userMessage == "/start"

    override fun getKeyboard(): ReplyKeyboardMarkup? {
        val row = KeyboardRow()
        row.add("Найти собеседника")

        return ReplyKeyboardMarkup
                .builder()
                .resizeKeyboard(true)
                .keyboardRow(row)
                .build()
    }
}