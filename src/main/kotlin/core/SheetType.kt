package kito.metapolemika.core

import dev.kordex.core.commands.application.slash.converters.ChoiceEnum
import dev.kordex.core.i18n.types.Key
import src.main.kotlin.discord.i18n.Translations.SheetTypes as Keys

enum class SheetType(override val readableName: Key) : ChoiceEnum {
    CHARACTER(Keys.character), TOOL(Keys.tool), SKILL(Keys.skill);
}