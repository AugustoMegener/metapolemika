package kito.metapolemika.core.form.sheet

import dev.kord.common.entity.Snowflake
import kito.metapolemika.core.Ranks
import kito.metapolemika.core.SheetStatus
import kito.metapolemika.core.SheetType
import kito.metapolemika.core.Validation
import kito.metapolemika.core.form.FieldMode
import kito.metapolemika.core.form.FormBase
import kito.metapolemika.database.entity.SheetData
import kito.metapolemika.isImageUrl
import kito.metapolemika.isPascalCase
import kito.metapolemika.isValidUrl
import kito.metapolemika.resource.json.Lang

class SheetBaseForm(
    val ownerIdSnowflake: Snowflake,
    val sheetType: SheetType,
    val initialRank: Ranks = Ranks.F
) : FormBase<SheetData>("Dados Básicos") {
    val name =
        Field(
            langName("name"), langInstruction("name"), 2..250,
            FieldMode.Mandatory, { Validation.Valid }, { it }
        ) { name = it }

    val symbol =
        Field(
            langName("symbol"), langInstruction("symbol"), 2..50,
            FieldMode.Mandatory, { Validation.of(it.isPascalCase, langError("symbol")) }, { it }
        ) { symbol = it }

    val description =
        Field(
            langName("description"), langInstruction("description"), 100..4000,
            FieldMode.Mandatory, { Validation.Valid }, { it }
        ) { description = it }

    val appearanceDescription =
        Field(
            langName("appearance_description"), langInstruction("appearance_description"), 0..4000,
            FieldMode.Optional, { Validation.Valid }, { it }
        ) { appearanceDescription = it }

    val appearanceImageAddress =
        Field(
            langName("appearance_image_address"), langInstruction("appearance_image_address"), 0..4000,
            FieldMode.Optional, { Validation.of(it.isImageUrl, langError("appearance_image_address")) }, { it }
        ) { appearanceImageAddress = it }

    val trivia =
        Field(
            langName("trivia"), langInstruction("trivia"), 0..4000,
            FieldMode.Optional, { Validation.Valid }, { it }
        ) { trivia = it }

    val webPage =
        Field(
            langName("web_page"), langInstruction("web_page"), 0..4000,
            FieldMode.Optional, { Validation.of(it.isValidUrl, langError("web_page")) }, { it }
        ) { webPage = it }


    override fun mount() = SheetData.new {
        fields.forEach { it.apply(this) }

        ownerId = ownerIdSnowflake.value
        type = sheetType
        rank = initialRank
        status = SheetStatus.EVALUATION
    }

    companion object {
        fun langName(arg: String) = Lang["form.sheet.field.name.$arg"]
        fun langInstruction(arg: String) = Lang["form.sheet.field.instruction.$arg"]
        fun langError(arg: String) = Lang["form.sheet.field.error.$arg"]
    }
}