package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.telegram.KeyboardButtons

class EnqueueCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        Идет поиск собеседника, как только он найдется - я напишу.
        Ты можешь в любой момент выйти из поиска, нажав кнопку внизу
    """.trimIndent()

    override fun isValid(userMessage: String): Boolean = userMessage == KeyboardButtons.Find

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.Stop)
}