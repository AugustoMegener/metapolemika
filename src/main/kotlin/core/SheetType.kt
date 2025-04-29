package kito.metapolemika.core

import dev.kordex.core.commands.application.slash.converters.ChoiceEnum
import dev.kordex.core.i18n.types.Key
import kito.metapolemika.core.form.sheet.BaseSheetForm
import kito.metapolemika.core.form.sheet.CharacterForm
import kito.metapolemika.core.form.sheet.SheetForm
import kotlinx.serialization.Serializable
import src.main.kotlin.discord.i18n.Translations.SheetTypes as Keys

@Serializable
enum class SheetType(override val readableName: Key, private val formSupplier : (BaseSheetForm) -> SheetForm<*>) :
    ChoiceEnum
{
    CHARACTER(Keys.character, ::CharacterForm), TOOL(Keys.tool, { TODO() }), SKILL(Keys.skill, { TODO() });

    fun newOf(form: BaseSheetForm) = formSupplier(form)
}