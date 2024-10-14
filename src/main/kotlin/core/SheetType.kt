package kito.metapolemika.core

import dev.kordex.core.commands.application.slash.converters.ChoiceEnum

enum class SheetType(override val readableName: String) : ChoiceEnum {
    CHARACTER("personagem"), TOOL("equipamento"), SKILL("habilidade");

}