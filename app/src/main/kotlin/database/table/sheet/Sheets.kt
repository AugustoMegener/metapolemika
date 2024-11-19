package database.table.sheet

import kito.metapolemika.core.Ranks
import kito.metapolemika.core.SheetStatus
import kito.metapolemika.core.SheetType
import kito.metapolemika.reflect.ObjectRegister
import org.jetbrains.exposed.dao.id.UUIDTable

@ObjectRegister.Register("TABLE")
object Sheets : UUIDTable() {

    val                ownerId = ulong("owner_id")
    val                 symbol = varchar("symbol", 50).uniqueIndex()
    val                   type = enumeration("type", SheetType::class)
    val                   name = varchar("name", 250)
    val            description = varchar("description", 4000)
    val                   rank = enumeration("rank", Ranks::class).default(Ranks.F)
    val  appearanceDescription = varchar("appearance_description", 4000).nullable()
    val appearanceImageAddress = text("appearance_image").nullable()
    val                 trivia = varchar("trivia", 4000).nullable()
    val                webPage = text("web_page").nullable()
    val                 status = enumeration("status", SheetStatus::class).default(SheetStatus.EVALUATION)
}