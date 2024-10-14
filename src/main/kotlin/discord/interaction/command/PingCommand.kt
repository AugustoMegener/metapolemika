package kito.metapolemika.discord.interaction.command

import dev.kord.rest.builder.message.embed
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.converters.impl.enumChoice
import dev.kordex.core.components.components
import dev.kordex.core.components.ephemeralStringSelectMenu
import dev.kordex.modules.dev.unsafe.annotations.UnsafeAPI
import dev.kordex.modules.dev.unsafe.commands.slash.UnsafeSlashCommand
import kito.metapolemika.core.SheetType
import kito.metapolemika.core.Validation.Invalid
import kito.metapolemika.core.Validation.Valid
import kito.metapolemika.core.form.sheet.SheetBaseForm
import kito.metapolemika.discord.interaction.command.PingCommand.Args
import kito.metapolemika.discord.interaction.command.SlashCommand.CommandType.EPHEMERAL
import kito.metapolemika.discord.interaction.modal.FormFieldEditorModal
import kito.metapolemika.reflect.ObjectRegister

@OptIn(UnsafeAPI::class)
private typealias Ctx = UnsafeCmdCtx<Args>

@OptIn(UnsafeAPI::class)
@ObjectRegister.Register("EXTENSION")
object PingCommand : SlashCommand<Args, Nothing, UnsafeSlashCommand<Args, Nothing>, Ctx>("criarficha", "pong", EPHEMERAL, ::Args) {

    class Args internal constructor(): Arguments() {
        val type by enumChoice<SheetType> {
            require(true)

            typeName    = "ficha"
            name        = "tipo"
            description = "A Ficha que deseja criar"
        }
    }


    override suspend fun Ctx.commandAction(modal: Nothing?) {
        val sheetBase = SheetBaseForm(user.id, arguments.type)

        respondEphemeral {

            embed {
                title = "Criando Ficha"
                description = "Clique no seletor abaixo para definir os campos de sua ficha."
            }

            val emptyFields = sheetBase.fields.filter { !it.isEmpty }

            if (emptyFields.isNotEmpty())
                embed {
                    emptyFields.forEach { field {
                        name  = "${ when(it.validation) { is Invalid -> "❌"; is Valid -> "✅" } }${it.name}"
                        value = it.input!! +
                                "\n${ when(it.validation) { is Invalid -> "-# ⚠${(it.validation as Invalid).message}"
                                                            else       -> "" } }"
                        inline = it.sizeRange.last > 2000
                    } }

                    footer {
                        text = sheetBase.fields.filter { it.validation is Invalid }.joinToString(", ") { it.name }
                    }
                }

            components {

                ephemeralStringSelectMenu {
                    placeholder = "Edite um campo da ficha"
                    sheetBase.fields.withIndex().forEach { (i, f) ->
                        option(f.name, "$i") { description = f.instructions }
                    }

                    action {
                        FormFieldEditorModal(sheetBase[selected[0].toInt()]).sendAndAwait(this) {

                        }
                    }
                }
            }
        }
    }
}