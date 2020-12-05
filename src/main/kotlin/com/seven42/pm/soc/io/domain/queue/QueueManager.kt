package com.seven42.pm.soc.io.domain.queue

import com.seven42.pm.soc.io.domain.User

interface SocialQueue {

    fun putUserIn(user: User): Unit

    fun removeUser(user: User): Unit
}