package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId

class ЫCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = "ы".repeat(userMessage.length)

    override fun isValid(userMessage: String, userId: UserId): Boolean = true

    override fun getKeyboardButtons(): List<String> = listOf("кнопка", "еще кнопка")
}