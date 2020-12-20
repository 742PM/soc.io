package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.conversation.ConversationService
import com.seven42.pm.soc.io.telegram.KeyboardButtons

class ResendCommand(private val conversationService: ConversationService) : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        **Собеседник пишет:**
        $userMessage
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean =
            conversationService.HasInterlocutor(userId)

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.ChangeDialog, KeyboardButtons.StopDialog)

    override fun getReceiverChatId(sender: UserId): String {
        val interlocutor = conversationService.GetCurrentInterlocutorInfo(sender)

        return interlocutor.value
    }
}