package kito.metapolemika.discord

import dev.kord.core.Kord
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import kito.metapolemika.discord.command.RegisterCommand.Companion.registry
import kotlinx.coroutines.runBlocking
import java.lang.System.getenv

object DiscordApp {
    private val app = runBlocking { Kord(getenv("TOKEN")) }

    suspend fun start() {

        registry.forEach {
            app.createGlobalChatInputCommand(it.name, it.description, it::inputs)
            app.on<GuildChatInputCommandInteractionCreateEvent> {
                it.action(this)
            }
        }

        app.login()
    }
}