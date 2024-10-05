package kito.metapolemika.discord.interaction.command

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.actionRow
import kito.metapolemika.discord.interaction.component.button.TestButton
import kito.metapolemika.ephemeralResponse
import kito.metapolemika.reflect.ObjectRegister

@ObjectRegister.Register("GCI_COMMAND")
object PingCommand : GuildChatInputCommand("ping", "pong") {

    private val msg by CommandInput("msg", "fala aii", InteractionCommand::strings, { ::string }) { required = false }

    override suspend fun GuildChatInputCommandInteractionCreateEvent.commandAction() {
        interaction.ephemeralResponse.respond {
            content = "Ping! ${msg ?: ""}"
            actionRow {
                TestButton on this
            }
        }

    }
}