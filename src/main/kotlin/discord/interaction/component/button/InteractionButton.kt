package kito.metapolemika.discord.interaction.component.button

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.ButtonBuilder.InteractionButtonBuilder
import kito.metapolemika.discord.interaction.component.modal.Modal
import kito.metapolemika.reflect.ObjectRegister

@ObjectRegister.Registry("INTERACTION_BUTTON")
abstract class InteractionButton(val id: String, val style: ButtonStyle) {

    infix fun on(builder: ActionRowBuilder) { builder.run { interactionButton(style, id) { buildButton() } } }

    abstract fun InteractionButtonBuilder.buildButton()

    internal abstract suspend fun ButtonInteractionCreateEvent.onPressed()

    suspend fun action(event: ButtonInteractionCreateEvent) {
        if (event.interaction.componentId != id) return
        event.onPressed()
    }
}