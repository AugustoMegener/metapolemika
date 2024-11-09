package kito.metapolemika.discord.interaction.command

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.EphemeralSlashCommandContext
import dev.kordex.core.commands.application.slash.SlashCommand
import dev.kordex.core.commands.application.slash.SlashCommandContext
import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.extensions.ephemeralSlashCommand
import dev.kordex.core.extensions.publicSlashCommand
import dev.kordex.core.i18n.types.Key
import kito.metapolemika.discord.interaction.BaseExtension
import kito.metapolemika.discord.interaction.command.SlashCommand.CommandType.EPHEMERAL
import kito.metapolemika.discord.interaction.command.SlashCommand.CommandType.PUBLIC

typealias PrivateCmdCtx<T> = EphemeralSlashCommandContext<T, Nothing>



abstract class SlashCommand<A : Arguments, M: ModalForm, S: SlashCommand<C, A, M>, C : SlashCommandContext<C, A, M>>(
    override val name               : String,
             val commandName        : Key,
             val commandDescription : Key,
             val type               : CommandType,
             val commandArguments   : () -> A,
             val modalForm          : (() ->M)? = null
) : BaseExtension()
{
    override suspend fun setup() {

        val body: SlashCommand<*, *, *>.() -> Unit =
            { @Suppress("UNCHECKED_CAST") (this as S).internalAction() }


        when(type) {
            PUBLIC -> when(modalForm)
            { null -> publicSlashCommand(arguments = { commandArguments() }, body = { body() })
              else -> publicSlashCommand({ commandArguments() }, modalForm, { body() }) }

            EPHEMERAL -> when(modalForm)
            { null -> ephemeralSlashCommand(arguments = { commandArguments() }, body = { body() })
              else -> ephemeralSlashCommand({ commandArguments() }, modalForm, { body() }) }
        }
    }

    private fun S.internalAction() {
        name        = commandName
        description = commandDescription

        action { commandAction(it) }
    }

    abstract suspend fun C.commandAction(modal: M?)

    enum class CommandType { PUBLIC, EPHEMERAL }
}