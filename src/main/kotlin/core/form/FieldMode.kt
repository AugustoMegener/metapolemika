package kito.metapolemika.core.form

sealed class FieldMode {
    data object Mandatory : FieldMode()
    data object Optional : FieldMode()

    class Defaulted(val default: String) : FieldMode()
}