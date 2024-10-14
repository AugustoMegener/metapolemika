package kito.metapolemika.discord.interaction.command

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.EphemeralSlashCommandContext
import dev.kordex.core.commands.application.slash.SlashCommand
import dev.kordex.core.commands.application.slash.SlashCommandContext
import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.extensions.ephemeralSlashCommand
import dev.kordex.core.extensions.publicSlashCommand
import dev.kordex.modules.dev.unsafe.annotations.UnsafeAPI
import dev.kordex.modules.dev.unsafe.contexts.UnsafeCommandSlashCommandContext
import dev.kordex.modules.dev.unsafe.extensions.unsafeSlashCommand
import kito.metapolemika.discord.interaction.BaseExtension
import kito.metapolemika.discord.interaction.command.SlashCommand.CommandType.*

typealias PrivateCmdCtx<T> = EphemeralSlashCommandContext<T, Nothing>

@OptIn(UnsafeAPI::class)
typealias  UnsafeCmdCtx<T> = UnsafeCommandSlashCommandContext<T, Nothing>

abstract class SlashCommand<A : Arguments, M: ModalForm, S: SlashCommand<C, A, M>, C : SlashCommandContext<C, A, M>>(
    val commandName        : String,
    val commandDescription : String,
    val type               : CommandType,
    val commandArguments   : () -> A,
    val modalForm          : (() ->M)? = null
) : BaseExtension<Unit>()
{
    override val name = commandName

    @OptIn(UnsafeAPI::class)
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

            UNSAFE -> unsafeSlashCommand({ commandArguments() }) { body() }
        }
    }

    private fun S.internalAction() {
        name        = commandName
        description = commandDescription

        action { commandAction(it) }
    }

    abstract suspend fun C.commandAction(modal: M?)


    enum class CommandType { PUBLIC, EPHEMERAL, UNSAFE }
}