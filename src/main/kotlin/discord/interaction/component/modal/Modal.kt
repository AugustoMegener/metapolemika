package kito.metapolemika.discord.interaction.component.modal

import dev.kord.common.entity.TextInputStyle
import dev.kord.core.behavior.interaction.ModalParentInteractionBehavior
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.TextInputBuilder
import dev.kord.rest.builder.interaction.ModalBuilder
import kito.metapolemika.reflect.ObjectRegister
import kotlin.reflect.KProperty

@ObjectRegister.Registry("MODAL")
abstract class Modal(val customId: String, val title: String) {

    private var inputs = arrayListOf<ModalInput>()
    private lateinit var actualSource: Map<String, String?>

    private fun updateSource(modal: ModalSubmitInteraction) {
        actualSource = modal.textInputs.mapValues { it.value.value }
    }

    suspend fun action(modal: ModalSubmitInteractionCreateEvent) {
        updateSource(modal.interaction)
        modal.interaction.onSubmit()
    }

    abstract suspend fun ModalSubmitInteraction.onSubmit()

    open fun ModalBuilder.modalBuilder() {}

    inner class ModalInput(
        private val id: String,
        private val label: String,
        private val style: TextInputStyle,
        private val builder: TextInputBuilder.() -> Unit)
    {
        init { inputs += this }

        operator fun getValue(command: Modal, property: KProperty<*>) = actualSource[id]

        fun buildInput(rowBuilder: ActionRowBuilder) { rowBuilder.textInput(style, id, label, builder) }
    }

    companion object {
        suspend infix fun ModalParentInteractionBehavior.sends(to: Modal) {
            to.run {
                modal(title, customId) {
                    modalBuilder()
                    inputs.forEach { actionRow { it.buildInput(this)  }  }
                }
            }
        }
    }
}