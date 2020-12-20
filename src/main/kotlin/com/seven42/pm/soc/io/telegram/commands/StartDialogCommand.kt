package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.telegram.KeyboardButtons

class StartDialogCommand : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        Собеседник найден!
        Все твои сообщения я буду пересылалть ему, а его - тебе.
        Чтобы найти нового собеседника или прекратить общение, воспользуйся кнопками внизу
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean = false

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.ChangeDialog, KeyboardButtons.StopDialog)
}