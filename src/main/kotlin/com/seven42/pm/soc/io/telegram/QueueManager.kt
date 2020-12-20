package com.seven42.pm.soc.io.telegram

import com.seven42.pm.soc.io.domain.conversation.ConversationService
import com.seven42.pm.soc.io.telegram.commands.BotCommand
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.bots.AbsSender

class QueueManager(private val conversationService: ConversationService,
                   private val bot: AbsSender,
                   private val successCommand: BotCommand) {

    private val logger: Logger = LoggerFactory.getLogger(QueueManager::class.java)

    private val secondsDelay: Long = 15


    fun run() {
        GlobalScope.launch {
            while (true) {
                update()
                delay(secondsDelay * 1000)
            }
        }
    }

    private fun update() {
        logger.info("Updating queue...")

        val users = conversationService.StartDialogs()
        logger.info("started ${users.size / 2} dialogs")

        for (user in users) {
            successCommand.execute(bot, "", user)
        }
    }
}