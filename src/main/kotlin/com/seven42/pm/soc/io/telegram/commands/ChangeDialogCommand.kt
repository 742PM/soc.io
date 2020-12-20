package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.conversation.ConversationService
import com.seven42.pm.soc.io.telegram.KeyboardButtons
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender

class ChangeDialogCommand(private val conversationService: ConversationService) : BotCommand() {
    private val interlocutorMessage: String = """
        Твой собеседник прекратил диалог!
        Идет поиск нового...
    """.trimIndent()

    override fun getMessageText(userMessage: String): String = """
        Диалог с этим собеседником прекращен!
        Идет поиск нового...
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean =
            userMessage == KeyboardButtons.ChangeDialog
                    && conversationService.HasInterlocutor(userId)

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.StopSearch)

    override fun run(userId: UserId, bot: AbsSender) {
        val interlocutor = conversationService.GetCurrentInterlocutorInfo(userId)

        conversationService.ChangeInterlocutor(userId)

        val sendMessage = SendMessage
                .builder()
                .chatId(interlocutor.value)
                .text(interlocutorMessage)
                .replyMarkup(buildKeyboard(getKeyboardButtons()))
                .build()
        bot.execute(sendMessage)
    }
}