package database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object DiscordEntitiesCache : LongIdTable() {
    val data = text("data")
}