package kito.metapolemika.database.table

import kito.metapolemika.reflect.ObjectRegister
import org.jetbrains.exposed.dao.id.LongIdTable

@ObjectRegister.Register("TABLE")
object DiscordEntityCaches : LongIdTable() {
    val data = text("data")
}