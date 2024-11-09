package kito.metapolemika.discord.interaction.command

import dev.kordex.core.commands.Arguments
import dev.kordex.core.i18n.types.Key
import dev.kordex.modules.dev.unsafe.annotations.UnsafeAPI
import dev.kordex.modules.dev.unsafe.components.forms.UnsafeModalForm
import dev.kordex.modules.dev.unsafe.contexts.UnsafeCommandSlashCommandContext
import dev.kordex.modules.dev.unsafe.extensions.unsafeSlashCommand
import kito.metapolemika.discord.interaction.BaseExtension

@OptIn(UnsafeAPI::class)
typealias  UnsafeCmdCtx<T> = UnsafeCommandSlashCommandContext<T, UnsafeModalForm>

abstract class UnsafeSlashCommand<A: Arguments>(override val name               : String,
                                                         val commandName        : Key,
                                                         val commandDescription : Key,
                                                         val commandArguments   : () -> A) :
    BaseExtension()
{
    @OptIn(UnsafeAPI::class)
    override suspend fun setup() {
        unsafeSlashCommand({ commandArguments() }) {
            name        = commandName
            description = commandDescription

            action { commandAction(it) }
        }
    }

    @OptIn(UnsafeAPI::class)
    abstract suspend fun UnsafeCommandSlashCommandContext<A, UnsafeModalForm>.commandAction(modal: UnsafeModalForm?)
}