package kito.metapolemika.discord.interaction.modal

import dev.kordex.core.components.forms.CoordinatePair
import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.components.forms.widgets.TextInputWidget
import dev.kordex.core.i18n.toKey
import kito.metapolemika.core.form.FormBase
import src.main.kotlin.discord.i18n.Translations.Modals.FormFieldEditor as Keys


class FormFieldEditorModal(val field: FormBase<*>.Field<*>) : ModalForm() {
    override var title = Keys.title

    val text = (when (field.sizeRange.last) {
        in 2001..5000 -> ::paragraphText
        in    0..2000 -> ::lineText
        else -> throw IllegalStateException("${field.name} field, in ${field.form.fields.indexOf(field)} of " +
                                            "${field.form.id} form is out of range (0-5000).")
    } as (CoordinatePair?, TextInputWidget<*>.() -> Unit) -> TextInputWidget<*>)(null) {
        label       = field.name
        placeholder = field.instructions
        initialValue = field.input?.toKey()
    }
}