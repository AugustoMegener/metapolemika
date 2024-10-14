package kito.metapolemika.core.form.sheet

import kito.metapolemika.core.Validation
import kito.metapolemika.core.form.FieldMode
import kito.metapolemika.database.entity.CharacterData
import kito.metapolemika.resource.json.Lang
import kotlin.math.pow

class CharacterForm(sheet: SheetBaseForm) : SheetForm<CharacterData>("Ficha de personagem", CharacterData.Companion, sheet) {
    val age =
        Field(
            langName("age"), langInstruction("age"), 0..10, FieldMode.Mandatory,
            { when(it.toIntOrNull()) {
                null -> Validation.Invalid(langError("age_not_num"))
                !in 0..10f.pow(9f).toInt() -> Validation.Invalid(langError("age_out_range"))
                else -> Validation.Valid
            } }, { it.toInt() }
        ) { self.age = it }

    val lineage =
        Field(
            langName("lineage"), langInstruction("lineage"), 0..10, FieldMode.Defaulted("Humano"),
            { Validation.Valid }, { it }
        ) { self.lineage = it }

    val pronouns =
        Field(
            langName("pronouns"), langInstruction("pronouns"), 0..50, FieldMode.Mandatory,
            { Validation.Valid }, { it }
        ) { self.pronouns = it }

    val personalidade =
        Field(
            langName("personality"), langInstruction("personality"), 200..5000, FieldMode.Mandatory,
            { Validation.Valid }, { it }
        ) { self.personality = it }

    companion object {
        fun langName(arg: String) = Lang["form.sheet.character.field.name.$arg"]
        fun langInstruction(arg: String) = Lang["form.sheet.character.field.instruction.$arg"]
        fun langError(arg: String) = Lang["form.sheet.character.field.error.$arg"]
    }
}