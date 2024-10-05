package kito.metapolemika.discord.interaction.component.button

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.ButtonBuilder.InteractionButtonBuilder

import kito.metapolemika.discord.interaction.component.button.InteractionButton
import kito.metapolemika.discord.interaction.component.modal.Modal.Companion.sends
import kito.metapolemika.discord.interaction.component.modal.TestModal
import kito.metapolemika.reflect.ObjectRegister

@ObjectRegister.Register("INTERACTION_BUTTON")
object TestButton : InteractionButton("test", ButtonStyle.Primary) {
    override fun InteractionButtonBuilder.buildButton() {
        label = "Butãum"
    }

    override suspend fun ButtonInteractionCreateEvent.onPressed() {
        /*interaction.respondEphemeral {
            content = "OIIII"

        }*/

        interaction sends TestModal
    }
}