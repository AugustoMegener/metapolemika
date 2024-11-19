package core

import dev.kordex.core.i18n.types.Key

sealed class Validation {
    val isValid get() = this is Valid

    data object Valid : Validation()
    class Invalid(val message: Key) : Validation()

    companion object {
        fun of(isValid: Boolean, message: Key) = if (isValid) Valid else Invalid(message)

        infix fun Boolean.orElse(message: Key) = of(this, message)

        val noValidation = { _: String -> Valid }
    }
}