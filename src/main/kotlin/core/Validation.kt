package kito.metapolemika.core

sealed class Validation {
    val isValid get() = this is Valid

    data object Valid : Validation()
    class Invalid(val message: String) : Validation()

    companion object {
        fun of(isValid: Boolean, message: String) = if (isValid) Valid else Invalid(message)
    }
}