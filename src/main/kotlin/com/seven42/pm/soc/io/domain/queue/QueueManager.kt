package com.seven42.pm.soc.io.domain.queue

import com.seven42.pm.soc.io.domain.User

interface SocialQueue {
    fun put(user: User): Unit
    fun remove(user: User): Unit
    fun all(user: User): List<User>
    fun contains(user: User): Boolean
}