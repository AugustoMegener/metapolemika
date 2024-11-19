import io.ktor.client.*
import kito.metapolemika.database.DatabaseApp
import kito.metapolemika.discord.DiscordApp
import kito.metapolemika.reflect.ObjectRegister
import kito.metapolemika.reflect.ObjectRegister.ObjectRegistry
import org.jetbrains.exposed.dao.id.IdTable

val httpClient = HttpClient()

suspend fun main() {

    ObjectRegister.run {
        registries["TABLE"] = ObjectRegistry(IdTable::class)
        register()
    }

    DatabaseApp.start()
     DiscordApp.main()
}

