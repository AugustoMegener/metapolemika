package kito.metapolemika.database.table.sheet

import kito.metapolemika.reflect.ObjectRegister
import org.jetbrains.exposed.dao.id.IdTable
import java.util.*

@ObjectRegister.Register("TABLE")
object Characters : IdTable<UUID>() {

    override val id          = reference("sheet", Sheets)
             val age         = integer("age")
             val lineage     = varchar("lineage", 100)
             val pronouns    = varchar("pronouns", 100).nullable()
             val personality = text("personality")
}