import me.ruslanys.telegraff.core.dsl.handler
import me.ruslanys.telegraff.core.dto.request.MarkdownMessage
import handlers.WelcomeConstants
import me.ruslanys.telegraff.core.exception.ValidationException

enum class WantTalk {
    Yes, No
}

handler("/start", "/talk") {
    step<WantTalk>("welcome") {
        question { state ->
            MarkdownMessage(WelcomeConstants.WelcomeMessage, "Да", "Нет")
        }

        validation {
            when (it.toLowerCase()) {
                "да" -> WantTalk.Yes
                "нет" -> WantTalk.No
                else -> throw ValidationException("Пожалуйста, выбери один из вариантов")
            }
        }

        next { state ->
            null
        }
    }


    process { state, answers ->
        val wantTalk = answers["welcome"] as WantTalk

        if (wantTalk == WantTalk.Yes) {
            MarkdownMessage("""
                Заявка принята от пользователя #${state.chat.id}. 
                Ищем собеседника.
            """.trimIndent())
        } else {
            MarkdownMessage("""
                Как хочешь -_-
            """.trimIndent())
        }
    }
}