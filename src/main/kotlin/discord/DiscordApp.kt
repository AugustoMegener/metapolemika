package kito.metapolemika.discord

import dev.kord.core.Kord
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.core.on

import kito.metapolemika.discord.interaction.command.GuildChatInputCommand
import kito.metapolemika.discord.interaction.component.button.InteractionButton
import kito.metapolemika.discord.interaction.component.modal.Modal
import kito.metapolemika.reflect.ObjectRegister
import kotlinx.coroutines.runBlocking
import java.lang.System.getenv

object DiscordApp {
    private val app = runBlocking { Kord(getenv("TOKEN")) }

    suspend fun start() {
        ObjectRegister.of<GuildChatInputCommand>().forEach {
            app.createGlobalChatInputCommand(it.name, it.description, it::inputs)
            app.on<GuildChatInputCommandInteractionCreateEvent> { it.action(this) }
        }

        ObjectRegister.of<InteractionButton>().forEach {
            app.on<ButtonInteractionCreateEvent> { it.action(this) }
        }

        ObjectRegister.of<Modal>().forEach {
            app.on<ModalSubmitInteractionCreateEvent> { it.action(this) }
        }

        app.login()
    }
}