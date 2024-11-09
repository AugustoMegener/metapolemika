package kito.metapolemika.core.form.sheet

import dev.kord.common.entity.Snowflake
import kito.metapolemika.core.Ranks
import kito.metapolemika.core.SheetStatus
import kito.metapolemika.core.SheetType
import kito.metapolemika.core.Validation.Companion.noValidation
import kito.metapolemika.core.Validation.Companion.orElse
import kito.metapolemika.core.form.FieldMode.Mandatory
import kito.metapolemika.core.form.FieldMode.Optional
import kito.metapolemika.core.form.FormBase
import kito.metapolemika.database.entity.SheetData
import kito.metapolemika.isImageUrl
import kito.metapolemika.isPascalCase
import kito.metapolemika.isValidUrl
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Base.Field.Error
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Base.Field.Instruction
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Base.Field.Name
import src.main.kotlin.discord.i18n.Translations.Forms.Sheet.Base.title

class SheetBaseForm(val ownerIdSnowflake: Snowflake, val sheetType: SheetType, val initialRank: Ranks = Ranks.F)
    : FormBase<SheetData>(title)
{
    val name = Field("💬", Name.name, Instruction.name, 2..250, Mandatory, noValidation, pure) { name = it }

    val symbol =
        Field("🟠", Name.symbol, Instruction.symbol, 2..50, Mandatory, { it.isPascalCase orElse Error.symbol }, pure)
            { symbol = it }

    val description =
        Field("📝", Name.description, Instruction.description, 100..4000, Mandatory, noValidation, pure)
            { description = it }

    val appearanceDescription =
        Field("👁‍🗨", Name.appearanceDescription, Instruction.appearanceDescription, 0..4000, Optional, noValidation, pure)
            { appearanceDescription = it }

    val appearanceImageAddress =
        Field("👁", Name.appearanceImageAddress, Instruction.appearanceImageAddress, 0..4000, Optional,
              { it.isImageUrl orElse Error.appearanceImageAddress }, pure) { appearanceImageAddress = it }

    val trivia = Field("💡", Name.trivia, Instruction.trivia, 0..4000, Optional, noValidation, pure) { trivia = it }

    val webPage =
        Field("🌐", Name.webPage, Instruction.webPage, 0..4000, Optional, { it.isValidUrl orElse Error.webPage }, pure)
            { webPage = it }


    override fun mount() = SheetData.new {
        fields.forEach { it.apply(this) }

        ownerId = ownerIdSnowflake.value
        type = sheetType
        rank = initialRank
        status = SheetStatus.EVALUATION
    }
}