package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.conversation.ConversationService
import com.seven42.pm.soc.io.telegram.KeyboardButtons
import org.telegram.telegrambots.meta.bots.AbsSender

class DequeueCommand(private val conversationService: ConversationService) : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        Поиск прекращен! 
        Помни что я могу подобрать тебе собеседника только пока идет поиск
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean =
            userMessage == KeyboardButtons.StopSearch
                    && conversationService.IsInQueue(userId)
                    && !conversationService.HasInterlocutor(userId)

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.Find)

    override fun run(userId: UserId, bot: AbsSender) {
        conversationService.LeaveQueue(userId)
    }
}