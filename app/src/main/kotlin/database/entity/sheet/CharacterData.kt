package database.entity.sheet

import kito.metapolemika.database.table.sheet.Characters
import kito.metapolemika.database.table.sheet.ISheet
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CharacterData(id: EntityID<UUID>) : UUIDEntity(id), ISheet<CharacterData> {

    override val self = this

    override var sheet       by SheetData referencedOn Characters.id
             var age         by Characters.age
             var lineage     by Characters.lineage
             var pronouns    by Characters.pronouns
             var personality by Characters.personality

    companion object : UUIDEntityClass<CharacterData>(Characters)
}