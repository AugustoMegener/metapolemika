package kito.metapolemika.discord.interaction.modal

import dev.kordex.core.components.forms.CoordinatePair
import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.components.forms.widgets.TextInputWidget
import kito.metapolemika.core.form.FormBase


class FormFieldEditorModal(val field: FormBase<*>.Field<*>) : ModalForm() {
    override var title = "Defina o campo da ficha"

    val text = (when (field.sizeRange.last) {
        in 2001..5000 -> ::paragraphText
        in    0..2000 -> ::lineText
        else -> throw IllegalStateException("${field.name} field, in ${field.form.fields.indexOf(field)} of " +
                                            "${field.form.title} form is out of range (0-5000).")
    } as (CoordinatePair?, TextInputWidget<*>.() -> Unit) -> TextInputWidget<*>)(null) {
        label       = field.name
        placeholder = field.instructions
    }
}