package com.seven42.pm.soc.io

import me.ruslanys.telegraff.core.dto.request.MarkdownMessage
import me.ruslanys.telegraff.core.exception.ValidationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class HelloHandlerTests : HandlerTests("/start") {
    @Test(expected = ValidationException::class)
    fun testWelcome() {
        val step = getStep<Any>("welcome")

        val answer = step.validation("Да")
        assertThat(answer).isNotNull
        assertThat(answer).isInstanceOf(Enum::class.java)
    }
}