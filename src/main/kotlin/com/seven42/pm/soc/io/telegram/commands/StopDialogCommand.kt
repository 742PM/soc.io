package com.seven42.pm.soc.io.telegram.commands

import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.conversation.ConversationService
import com.seven42.pm.soc.io.telegram.KeyboardButtons
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender

class StopDialogCommand(private val conversationService: ConversationService) : BotCommand() {
    private val interlocutorMessage: String = """
        Твой собеседник прекратил диалог!
        Идет поиск нового...
    """.trimIndent()

    override fun getMessageText(userMessage: String): String = """
        Диалог прекращен!
        Если захочешь пообщаться еще, нажми кнопку внизу
    """.trimIndent()

    override fun isValid(userMessage: String, userId: UserId): Boolean =
            userMessage == KeyboardButtons.StopDialog
                    && conversationService.HasInterlocutor(userId)

    override fun getKeyboardButtons(): List<String> = listOf(KeyboardButtons.Find)

    override fun run(userId: UserId, bot: AbsSender) {
        val interlocutor = conversationService.GetCurrentInterlocutorInfo(userId)

        conversationService.StopDialog(userId)

        val keyboard = buildKeyboard(listOf(KeyboardButtons.StopSearch))
        val sendMessage = SendMessage
                .builder()
                .chatId(interlocutor.value)
                .text(interlocutorMessage)
                .replyMarkup(keyboard)
                .build()
        bot.execute(sendMessage)
    }
}