package kito.metapolemika

import kito.metapolemika.database.DatabaseApp
import io.ktor.client.*
import kito.metapolemika.discord.DiscordApp
import org.jetbrains.exposed.dao.id.IdTable
import kito.metapolemika.reflect.ObjectRegister

val httpClient = HttpClient()

suspend fun main() {

    ObjectRegister.run {
        registries["TABLE"] = ObjectRegister.ObjectRegistry(IdTable::class)
        register()
    }

    DatabaseApp.start()
     DiscordApp.main()
}

