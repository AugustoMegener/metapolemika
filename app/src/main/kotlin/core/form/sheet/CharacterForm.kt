package core.form.sheet

import kito.metapolemika.core.Validation.Companion.noValidation
import kito.metapolemika.core.Validation.Invalid
import kito.metapolemika.core.Validation.Valid
import kito.metapolemika.core.form.FieldMode.Defaulted
import kito.metapolemika.core.form.FieldMode.Mandatory
import kito.metapolemika.database.entity.sheet.CharacterData
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Character.Field.Error
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Character.Field.Instruction
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Character.Field.Name
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Character.title
import kotlin.math.pow

class CharacterForm(sheet: BaseSheetForm) :
    SheetForm<CharacterData>("character_sheet", title, CharacterData.Companion, sheet)
{
    val age =
        Field("🐣", Name.age, Instruction.age, 0..10, Mandatory,
            { when(it.toIntOrNull()) {
                null -> Invalid(Error.ageNotNum)
                !in 0..10f.pow(9f).toInt() -> Invalid(Error.ageOutRange)
                else -> Valid
            } }, { it.toInt() }
        ) { self.age = it }

    val lineage = Field("👹", Name.lineage, Instruction.lineage, 0..10, Defaulted("Humano"), noValidation, pure) {
        self.lineage = it }

    val pronouns = Field("🗨", Name.pronouns, Instruction.pronouns, 0..50, Mandatory, noValidation, pure) {
        self.pronouns = it }

    val personality = Field("💝", Name.personality, Instruction.personality, 200..5000, Mandatory, noValidation, pure) {
        self.personality = it }
}