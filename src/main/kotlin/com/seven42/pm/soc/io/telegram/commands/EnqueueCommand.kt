package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.conversation.ConversationService
import com.seven42.pm.soc.io.telegram.KeyboardButtons
import org.telegram.telegrambots.meta.bots.AbsSender

class EnqueueCommand(private val conversationService: ConversationService) : BotCommand() {
    override fun getMessageText(userMessage: String): String = """
        Идет поиск собеседника, как только он найдется - я напишу.
        Ты можешь в любой момент выйти из поиска, нажав кнопку внизу
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean =
            userMessage == KeyboardButtons.Find
                    && !conversationService.IsInQueue(userId)
                    && !conversationService.HasInterlocutor(userId)

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.StopSearch)

    override fun run(userId: UserId, bot: AbsSender) {
        conversationService.EnterQueue(userId)
    }
}