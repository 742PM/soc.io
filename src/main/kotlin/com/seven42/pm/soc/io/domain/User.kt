package com.seven42.pm.soc.io.domain

data class User(val name: String, val id: UserId)
data class TelegramUserInfo(val telegramId: String)

inline class UserId(val value: String)
