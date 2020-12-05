package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.TelegramUserInfo
import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import com.seven42.pm.soc.io.domain.queue.SocialQueue

class ConversationService(val socialQueue: SocialQueue) {
    /**  Из системы выкидывает текущего пользователя,  а собеседника возвращает в очередь */
    fun StopDialog(userId: UserId): Void = TODO()
    /** Завершает диалог для обоих пользователей и пытается найти собеседника для [[userId]]*/
    fun ChangeHuman(userId: UserId): Boolean =  TODO()

    /**  Пытается найти собеседника для [[userId]] */
    //TODO подумать про event-based штуку или асинхронные таски и шо в котлине, потому что ну не будет же эта херня лочить тред

    fun StartDialog(user: User): Boolean = TODO()

    fun GetCurrentInterlocutorInfo(userId: UserId): TelegramUserInfo = TODO()
}