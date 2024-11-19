package discord.interaction.command

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.core.behavior.UserBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.response.createPublicFollowup
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder
import dev.kord.rest.builder.message.embed
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.converters.impl.enumChoice
import dev.kordex.core.components.components
import dev.kordex.core.components.ephemeralButton
import dev.kordex.core.components.ephemeralStringSelectMenu
import dev.kordex.core.i18n.toKey
import dev.kordex.modules.dev.unsafe.annotations.UnsafeAPI
import dev.kordex.modules.dev.unsafe.components.forms.UnsafeModalForm
import dev.kordex.modules.dev.unsafe.components.menus.InitialInteractionSelectMenuResponse
import dev.kordex.modules.dev.unsafe.extensions.unsafeStringSelectMenu
import kito.metapolemika.core.SheetType
import kito.metapolemika.core.Validation.Invalid
import kito.metapolemika.core.form.sheet.BaseSheetForm
import kito.metapolemika.discord.accessing
import kito.metapolemika.discord.cacheOf
import kito.metapolemika.discord.interaction.BaseExtension
import kito.metapolemika.discord.interaction.command.NewSheetCommand.Args
import kito.metapolemika.discord.interaction.modal.FormFieldEditorModal
import kito.metapolemika.discord.unfinishedSheets
import kito.metapolemika.ksp.processor.RegisterObject
import kito.metapolemika.reflect.ObjectRegister
import src.main.kotlin.discord.i18n.Translations.Commands.Sheet.New as Keys


@OptIn(UnsafeAPI::class)
private typealias Ctx = UnsafeCmdCtx<Args>

@OptIn(UnsafeAPI::class)
@ObjectRegister.Register("EXTENSION")
@RegisterObject(BaseExtension::class)
object NewSheetCommand : UnsafeSlashCommand<Args>("new_sheet", Keys.name, Keys.description, ::Args) {

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
        val locale = getLocale()
        val sheets by user cacheOf unfinishedSheets

        if (sheets.isEmpty()) {
            respondPublic { content = Keys.seeDm.withLocale(locale).translate() }

            sheets += BaseSheetForm(user.id, arguments.type).specify()
            startSheetEditing(0)
        } else {
            respondEphemeral {
                selectSheetToEdit(this@commandAction, user, arguments.type)
            }
        }
    }

    private suspend fun FollowupMessageCreateBuilder
        .selectSheetToEdit(ctx: Ctx, user: UserBehavior, sheetType: SheetType? = null)
    {
        val sheets by user cacheOf unfinishedSheets

        Keys.AlreadyExists.let { keys ->
            embed {
                title       = keys.warn      .withLocale(ctx.getLocale()).translate()
                description = keys.howProceed.withLocale(ctx.getLocale()).translate()
            }
        }

        components {
            ephemeralStringSelectMenu {
                placeholder = Keys.AlreadyExists.Select.existingSheets

                sheets.forEachIndexed { i, s ->
                    option((s.sheet.name.input ?: "???").toKey(), "$i") {
                        description = s.title;
                        emoji       = DiscordPartialEmoji(name="📝")
                    }
                }

                action {
                    edit {
                        components = mutableListOf()
                        embeds     = mutableListOf()
                        content    = Keys.AlreadyExists.sucess.withLocale(ctx.getLocale()).translate()
                    }

                    content = Keys.seeDm.withLocale(getLocale()).translate()
                    ctx.startSheetEditing(selected.first().toInt())
                }
            }

            if (sheetType == null) {
                ephemeralStringSelectMenu {
                    placeholder = Keys.AlreadyExists.Select.newSheet

                    SheetType.entries.forEachIndexed { i, t ->
                        option(t.readableName, "$i")
                    }

                    action {
                        edit {
                            components = mutableListOf()
                            embeds     = mutableListOf()
                            content    = Keys.AlreadyExists.sucess.withLocale(ctx.getLocale()).translate()
                        }

                        respond { content = "-# ." }.delete()

                        sheets += BaseSheetForm(user.id, SheetType.entries[selected.last().toInt()]).specify()
                        ctx.startSheetEditing(sheets.lastIndex)
                    }
                }
            } else {
                ephemeralButton {
                    style = ButtonStyle.Success
                    partialEmoji = DiscordPartialEmoji(name = "➕")
                    label = Keys.button


                    action {
                        edit {
                            components = mutableListOf()
                            embeds     = mutableListOf()
                            content    = Keys.AlreadyExists.sucess.withLocale(ctx.getLocale()).translate()
                        }

                        respond { content = "-# ." }.delete()

                        sheets += BaseSheetForm(user.id, ctx.arguments.type).specify()
                        ctx.startSheetEditing(sheets.lastIndex)
                    }
                }
            }
        }
    }

    private suspend fun Ctx.startSheetEditing(idx: Int) {
        var message: Message? = null

        val locale = getLocale()
        val sheets by user cacheOf unfinishedSheets
        val sheet  by user cacheOf unfinishedSheets accessing { it[idx] }

        Keys.Dm.let { key -> message = user.getDmChannel().createMessage {
            embed {
                title       = "${key.title.withLocale(locale).translate()} — " +
                               sheet.title.withLocale(locale).translate()
                description = key.description.withLocale(locale).translate()

                footer { text = key.mandatory.withLocale(locale).translate() }

                sheet.totalFields.forEach {
                    val input = it.input ?: return@forEach
                    val isValid = it.validation.isValid

                    field {
                        inline = (isValid && (it.input?.length ?: 0) > 100) || !isValid
                        name   = it.name.withLocale(locale)
                                        .withPostProcessor { s -> (if (isValid) "${it.emoji} " else "❌ ") + s }
                                        .translate()
                        value  = if (isValid) input else (it.validation as Invalid).message
                            .withLocale(locale)
                            .withPostProcessor { s -> "$input — **__${s}__**" }
                            .translate()
                    }
                }
            }

            components {
                unsafeStringSelectMenu {
                    sheet.totalFields.forEachIndexed { i, f ->
                        option(f.name.withPostProcessor { it + (if (f.required) "*" else "") +
                                                               (if (f.isValid && f.required) " 👍" else "") }, "$i") {
                            description = f.instructions
                            emoji       = DiscordPartialEmoji(name = f.emoji)
                        }
                    }

                    initialResponse = InitialInteractionSelectMenuResponse.None

                    action {
                        val field    = sheet.totalFields[selected.first().toInt()]
                        val modal    = FormFieldEditorModal(field)
                        val response = modal.sendAndDeferPublic(this)

                        if (response == null) { interactionResponse?.delete(); return@action }

                        field.input = modal.text.value!!

                        message!!.delete()
                        response.createPublicFollowup { content = "-# ." }.delete()

                        startSheetEditing(idx)
                    }
                }

                ephemeralButton {
                    style        = ButtonStyle.Success
                    partialEmoji = DiscordPartialEmoji(name="✅")
                    disabled     = !sheet.isValid

                    action {
                        respond {
                            embed {
                                title       = Keys.Submit.title.withLocale(locale).translate()
                                description = Keys.Submit.description.withLocale(locale).translate()
                            }

                            components {
                                ephemeralButton {
                                    style        = ButtonStyle.Success
                                    partialEmoji = DiscordPartialEmoji(name="✅")

                                    action {
                                        edit {
                                            embeds     = mutableListOf()
                                            components = mutableListOf()
                                            content    = Keys.Submit.confirm.withLocale(locale).translate()
                                        }

                                        message!!.delete()

                                        // ToDo: Fazer a ficha salvar e fazer sistema de aprovação
                                    }
                                }

                                ephemeralButton {
                                    style        = ButtonStyle.Secondary
                                    partialEmoji = DiscordPartialEmoji(name="❌")

                                    action {
                                        edit {
                                            embeds     = mutableListOf()
                                            components = mutableListOf()
                                            content    = Keys.Submit.cancel.withLocale(locale).translate()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ephemeralButton {
                    style        = ButtonStyle.Primary
                    partialEmoji = DiscordPartialEmoji(name="📂")

                    action {
                        message!!.delete()
                        respond { selectSheetToEdit(this@startSheetEditing, user) }
                    }
                }

                ephemeralButton {
                    style        = ButtonStyle.Danger
                    partialEmoji = DiscordPartialEmoji(name="🗑")

                    action {
                        Keys.Cancel.let { keys -> respond {
                            embed {
                                title       = keys.title.withLocale(locale).translate()
                                description = keys.explain.withLocale(locale).translate()
                            }

                            components {
                                ephemeralButton {
                                    style        = ButtonStyle.Danger
                                    partialEmoji = DiscordPartialEmoji(name = "🗑")
                                    label        = keys.confirm

                                    action {
                                        sheets.remove(sheet)
                                        message!!.delete()

                                        content=Keys.Cancel.Confirm.sucess.withLocale(locale).translate()

                                        edit {
                                            embeds     = mutableListOf()
                                            components = mutableListOf()
                                            content    = Keys.Cancel.Confirm.sucess.withLocale(locale).translate()
                                        }
                                    }
                                }

                                ephemeralButton {
                                    style        = ButtonStyle.Primary
                                    partialEmoji = DiscordPartialEmoji(name="❌")
                                    label        = keys.cancel

                                    action {
                                        edit {
                                            embeds     = mutableListOf()
                                            components = mutableListOf()
                                            content    = Keys.Cancel.Cancel.sucess.withLocale(locale).translate()
                                        }
                                    }
                                }
                            }
                        } }
                    }
                }
            }

            val fields = sheet.totalFields

            content = key.progress.withLocale(locale)
                                  .withPostProcessor { "-# $it" }
                                  .withNamedPlaceholders("done"  to fields.count { it.required && it.isValid },
                                                         "total" to fields.count { it.required               })
                                  .translate()
        } }
    }
}
