package kito.metapolemika.core.form

import dev.kord.common.entity.DiscordPartialEmoji
import dev.kordex.core.i18n.types.Key
import kito.metapolemika.core.Validation
import kito.metapolemika.core.Validation.Invalid
import kito.metapolemika.core.Validation.Valid
import kito.metapolemika.core.form.FieldMode.Defaulted
import kito.metapolemika.core.form.FieldMode.Optional
import okio.utf8Size
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Base.Field.Error

abstract class FormBase<T>(val title: Key) {

    open val fields: ArrayList<Field<*>> = arrayListOf()

    val isValid get() = fields.map { it.validation }.filterIsInstance<Invalid>().isEmpty()

    abstract fun mount() : T
    operator fun get(i: Int) = fields[i]

    inner class Field<R>(val emoji        : String,
                         val name         : Key,
                         val instructions : Key,
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
                    else -> Invalid(Error.empty)
                }

                else -> when {
                    input!!.utf8Size() !in sizeRange ->
                        Invalid(Error.outRange.withNamedPlaceholders("{max}" to sizeRange.first,
                                                                     "{min}" to sizeRange.last))
                    else -> validator(input!!)
                }
            }

        fun apply(result: T) {
            if (validation is Invalid)
                throw IllegalStateException("Can not apply an invalid field. \"${(validation as Invalid).message}\"")

            output?.let { applier(result, it) }
        }
    }

    companion object {
        val pure: (String) -> String = { it }
    }
}