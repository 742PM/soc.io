package com.seven42.pm.soc.io.domain.conversation

import com.seven42.pm.soc.io.domain.User
import com.seven42.pm.soc.io.domain.UserId
import java.time.ZonedDateTime

interface ConversationRepository {
    //сюда добавим методов на чтение по необходимости
    //cхема такая:
    // | собеседник1 id | собеседник2 id | датавремя начала диалога | датавремя конца диалога |

}

data class ConversationMetaInformation(val firstInterlocutor: UserId, val secondInterlocutor: UserId, val startedAt: ZonedDateTime, val endedAt: ZonedDateTime)