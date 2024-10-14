package kito.metapolemika

import io.ktor.client.*
import kito.metapolemika.database.DatabaseApp
import kito.metapolemika.discord.DiscordApp
import kito.metapolemika.reflect.ObjectRegister
import kito.metapolemika.reflect.ObjectRegister.ObjectRegistry
import kito.metapolemika.resource.ResourceManager
import org.jetbrains.exposed.dao.id.IdTable

val httpClient = HttpClient()

suspend fun main() {

    ObjectRegister.run {
        registries["TABLE"] = ObjectRegistry(IdTable::class)
        register()
    }


    ResourceManager.start()
        DatabaseApp.start()
         DiscordApp.main()
}