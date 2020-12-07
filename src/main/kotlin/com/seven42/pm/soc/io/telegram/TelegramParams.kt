package com.seven42.pm.soc.io.telegram


class TelegramParams {
    companion object {
        val Token: String = System.getenv("TELEGRAM_TOKEN") ?: ""

        val Url: String = "https://soc-io.herokuapp.com/$Token"

        const val Username: String = "SocSocBot"

        private const val Port: Int = 8443

        const val Webhook: String = "https://soc-io.herokuapp.com:$Port"
    }
}