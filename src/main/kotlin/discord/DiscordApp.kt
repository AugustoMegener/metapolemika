package kito.metapolemika.discord

import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.env
import kito.metapolemika.discord.interaction.BaseExtension
import kito.metapolemika.discord.interaction.BaseExtension.Companion.EXTENSION
import kito.metapolemika.reflect.ObjectRegister
import kotlinx.coroutines.runBlocking

object DiscordApp {

    private val app by lazy {
        runBlocking {
            ExtensibleBot(env("TOKEN")) {
                extensions {
                    ObjectRegister.of<BaseExtension>(EXTENSION).forEach { add { it } }
                }
            }
        }
    }

    suspend fun main() {
        app.start()
    }
}