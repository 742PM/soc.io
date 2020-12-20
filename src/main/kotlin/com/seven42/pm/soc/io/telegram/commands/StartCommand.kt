package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.telegram.KeyboardButtons

class StartCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        Привет!
        Этот бот позволяет общаться со случайным собеседником.
        Если тебе одиноко или просто скучно, нажми на кнопку внизу
        😉
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean = userMessage == "/start"

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.Find)
}