package kito.metapolemika.discord.interaction

import dev.kordex.core.extensions.Extension
import kito.metapolemika.reflect.ObjectRegister


@ObjectRegister.Registry("EXTENSION")
abstract class BaseExtension<A : Any> : Extension()