package kito.metapolemika.database.entity

import kito.metapolemika.database.table.DiscordEntityCaches
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiscordEntityCacheData(id: EntityID<Long>) : LongEntity(id) {

    var data by DiscordEntityCaches.data

    companion object : LongEntityClass<DiscordEntityCacheData>(DiscordEntityCaches)
}