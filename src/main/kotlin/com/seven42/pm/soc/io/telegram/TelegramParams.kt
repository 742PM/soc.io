package com.seven42.pm.soc.io.telegram


class TelegramParams {
    companion object {
        val Token: String = System.getenv("TELEGRAM_TOKEN") ?: ""

        const val Username: String = "SocSocBot"
    }
}