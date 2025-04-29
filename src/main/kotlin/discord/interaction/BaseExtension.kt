package kito.metapolemika.discord.interaction

import dev.kordex.core.extensions.Extension
import kito.metapolemika.discord.interaction.BaseExtension.Companion.EXTENSION
import kito.metapolemika.reflect.ObjectRegister

@ObjectRegister.Registry(EXTENSION)
abstract class BaseExtension : Extension() {

    companion object {
        const val EXTENSION = "EXTENSION"
    }
}