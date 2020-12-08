package com.seven42.pm.soc.io.telegram.commands

class ЫCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = "ы".repeat(userMessage.length)

    override fun isValid(userMessage: String): Boolean = true

    override fun getKeyboardButtons(): List<String> = listOf("кнопка", "еще кнопка")
}