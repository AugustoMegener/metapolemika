package kito.metapolemika.discord.interaction.command

import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder
import dev.kord.rest.builder.message.embed
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.converters.impl.enumChoice
import dev.kordex.core.components.components
import dev.kordex.modules.dev.unsafe.annotations.UnsafeAPI
import dev.kordex.modules.dev.unsafe.components.forms.UnsafeModalForm
import dev.kordex.modules.dev.unsafe.components.menus.InitialInteractionSelectMenuResponse
import dev.kordex.modules.dev.unsafe.extensions.unsafeStringSelectMenu
import kito.metapolemika.core.SheetType
import kito.metapolemika.core.Validation.Invalid
import kito.metapolemika.core.Validation.Valid
import kito.metapolemika.core.form.FieldMode
import kito.metapolemika.core.form.sheet.SheetBaseForm
import kito.metapolemika.discord.interaction.command.PingCommand.Args
import kito.metapolemika.discord.interaction.modal.FormFieldEditorModal
import kito.metapolemika.reflect.ObjectRegister
import src.main.kotlin.discord.i18n.Translations.Commands.Sheet.New as Keys

@OptIn(UnsafeAPI::class)
private typealias Ctx = UnsafeCmdCtx<Args>

@OptIn(UnsafeAPI::class)
@ObjectRegister.Register("EXTENSION")
object PingCommand : UnsafeSlashCommand<Args>("new_sheet", Keys.name, Keys.description, ::Args) {

    class Args internal constructor(): Arguments() {
        val type by enumChoice<SheetType> {
            require(true)

            Keys.Arg.Type.let {
                typeName    = it.typeName
                name        = it.name
                description = it.description
            }
        }
    }

    override suspend fun Ctx.commandAction(modal: UnsafeModalForm?) {
        val sheetBase = SheetBaseForm(user.id, arguments.type)

        respondEphemeral { fieldEditResponse(this@commandAction, sheetBase) }
    }

    suspend fun FollowupMessageCreateBuilder.fieldEditResponse(ctx: Ctx, sheetBase: SheetBaseForm) {
        val locale = ctx.getLocale()
        
        Keys.Embed.let {
            embed {
                title       = it.title      .withLocale(locale).translate()
                description = it.description.withLocale(locale).translate()
            }
        }

        val emptyFields = sheetBase.fields.filter { !it.isEmpty }

        if (emptyFields.isNotEmpty())
            embed {
                emptyFields.forEach { field {
                    name  = when(it.validation) { is Invalid -> "❌"; is Valid -> "✅" } +
                            it.name.withLocale(locale).translate()
                    value = it.input!! +
                            "\n${ when(it.validation)
                            { is Invalid ->
                                    "-# ⚠${(it.validation as Invalid).message.withLocale(locale).translate()}"
                                else -> "" } }"
                    inline = it.sizeRange.last > 2000
                } }

                footer {
                    text = sheetBase.fields.filter { it.validation is Invalid }
                        .joinToString(", ") { it.name.translate() }
                }
            }

        components {
            unsafeStringSelectMenu {
                placeholder = Keys.Component.fieldSelect


                sheetBase.fields.forEachIndexed { i, f ->
                    option(f.name.withPostProcessor {
                        it + (when(f.mode) { FieldMode.Optional -> ""; else -> "*" }) }, "$i"
                    ) {
                        description = f.instructions
                        emoji = DiscordPartialEmoji(name=f.emoji)
                    }
                }

                initialResponse = InitialInteractionSelectMenuResponse.None

                action {

                    FormFieldEditorModal(sheetBase[selected.first().toInt()])
                        .let { it to it.sendAndDeferEphemeral(this)}
                        .also { (modalForm, response) -> (response ?: return@also)
                            val field = modalForm.field

                            field.input = modalForm.text.value
                            interactionResponse?.edit {
                                embeds?.clear()
                                components?.clear()

                                val res = field.validation
                                content = when (res) {
                                    is Valid   -> "✅**${field.name.withLocale(locale).translate()}**"
                                    is Invalid -> "❌**${field.name.withLocale(locale).translate()}:**" +
                                                        res.message.withLocale(locale).translate()
                                }
                            }

                            respondEphemeral { fieldEditResponse(ctx, sheetBase) }
                        }
                }
            }
        }
    }
}