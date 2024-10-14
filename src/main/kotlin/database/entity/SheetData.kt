package kito.metapolemika.database.entity

import kito.metapolemika.database.table.sheet.Sheets
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class SheetData(id: EntityID<UUID>) : UUIDEntity(id)  {

    var ownerId                 by Sheets.ownerId
    var symbol                  by Sheets.symbol
    var type                    by Sheets.type
    var name                    by Sheets.name
    var description             by Sheets.description
    var rank                    by Sheets.rank
    var appearanceDescription   by Sheets.appearanceDescription
    var appearanceImageAddress  by Sheets.appearanceImageAddress
    var trivia                  by Sheets.trivia
    var webPage                 by Sheets.webPage
    var status                  by Sheets.status

    companion object : UUIDEntityClass<SheetData>(Sheets)
}