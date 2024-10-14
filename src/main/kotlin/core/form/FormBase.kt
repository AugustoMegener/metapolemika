package kito.metapolemika.core.form

import kito.metapolemika.core.Validation
import kito.metapolemika.core.Validation.Invalid
import kito.metapolemika.core.Validation.Valid
import kito.metapolemika.core.form.FieldMode.Defaulted
import kito.metapolemika.core.form.FieldMode.Optional
import okio.utf8Size

abstract class FormBase<T>(val title: String) {

    open val fields: ArrayList<Field<*>> = arrayListOf()

    val isValid get() = fields.map { it.validation }.filterIsInstance<Invalid>().isEmpty()

    abstract fun mount() : T
    operator fun get(i: Int) = fields[i]

    inner class Field<R>(val name         : String,
                         val instructions : String,
                         val sizeRange    : IntRange,
                         val mode         : FieldMode,

                         private val validator    : (String) -> Validation,
                         private val processor    : (String) -> R,
                         private val applier      :    T.(R) -> Unit
        )
    {
        init { fields += this }

        val form = this@FormBase

        var input: String? = if (mode is Defaulted) mode.default else null
        val output: R? get() = when(validation) {is Valid -> input?.let { processor(it) }; is Invalid -> null }

        val isEmpty get() = input == null || input == ""

        val validation: Validation
            get() =
            when {
                isEmpty -> when(mode) {
                    is Optional -> Valid
                    else -> Invalid("Este campo não deve estar vazio.")
                }

                else -> when {
                    input!!.utf8Size() !in sizeRange ->
                        Invalid("Este campo deve ter entre ${sizeRange.first} e ${sizeRange.last} caracteres.")
                    else -> validator(input!!)
                }
            }

        fun apply(result: T) {
            if (validation is Invalid)
                throw IllegalStateException("Can not apply an invalid field. \"${(validation as Invalid).message}\"")

            output?.let { applier(result, it) }
        }
    }
}