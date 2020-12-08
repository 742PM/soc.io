package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.telegram.KeyboardButtons

class DequeueCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        Поиск прекращен! 
        Помни что я могу подобрать тебе собеседника только пока идет поиск
    """.trimIndent()

    override fun isValid(userMessage: String): Boolean = userMessage == KeyboardButtons.Stop

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.Find)
}