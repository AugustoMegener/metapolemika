package kito.metapolemika.discord.interaction.component.modal

import dev.kord.common.entity.TextInputStyle
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import kito.metapolemika.reflect.ObjectRegister

@ObjectRegister.Register("MODAL")
object TestModal : Modal("test", "Conentário anônimo") {

    private val    name by ModalInput("name", "Nome", TextInputStyle.Short) { required = true }
    private val comment by ModalInput("comment", "Comentário", TextInputStyle.Paragraph) { required = false }

    override suspend fun ModalSubmitInteraction.onSubmit() {
        respondPublic {
            content = "📢$name FEZ UM COMENTÁRIO!!!\n" +
                      "${comment?.let { "\"${it}\"" }}"
        }
    }

}