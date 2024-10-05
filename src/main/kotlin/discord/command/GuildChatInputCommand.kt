package kito.metapolemika.discord.command

import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.BaseChoiceBuilder
import dev.kord.rest.builder.interaction.BaseInputChatBuilder
import kotlin.reflect.KProperty

abstract class GuildChatInputCommand(val name: String, val description: String) {

    internal val inputs = arrayListOf<CommandInput<*, *>>()

    @Suppress("UNCHECKED_CAST")
    fun inputs(builder: BaseInputChatBuilder) {
        inputs.forEach { it.builderSource(builder)(it.name, it.description, it.builder as BaseChoiceBuilder<*>.() -> Unit) }
    }

    suspend fun action(event: GuildChatInputCommandInteractionCreateEvent) {
        if (event.interaction.invokedCommandName != name) return

        inputs.forEach { it.updateSource(event.interaction.command) }
        event.commandAction()
    }

    abstract suspend fun GuildChatInputCommandInteractionCreateEvent.commandAction()

    inner class CommandInput<T, B: BaseChoiceBuilder<*>>(
        val name: String,
        val description: String,
        val source: InteractionCommand.() -> Map<String, T?>,
        val builderSource: BaseInputChatBuilder.() -> ((String,String, B.() -> Unit) -> Unit),
        val builder: B.() -> Unit)
    {
        private lateinit var actualSource: Map<String, T?>

        init { inputs += this }

        operator fun getValue(command: GuildChatInputCommand, property: KProperty<*>): T? = actualSource[name]

        fun updateSource(command: InteractionCommand) {
            actualSource = source(command)
        }
    }
}