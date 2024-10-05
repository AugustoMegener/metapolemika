package kito.metapolemika.discord.action

import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder

abstract class InteractionButton(val id: String) {
    internal abstract fun ActionRowBuilder.buildButton()
    internal abstract fun ButtonInteractionCreateEvent.onPressed()

    fun action(event: ButtonInteractionCreateEvent) {
        if (event.interaction.componentId != id) return
        event.onPressed()
    }
}