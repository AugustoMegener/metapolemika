package kito.metapolemika.discord.command

import kito.metapolemika.asPackageName
import java.io.File
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegisterCommand {

    companion object {
        val registry = mutableListOf<GuildChatInputCommand>()

        fun scan(directory: String = "src/main/kotlin/discord/command") {
            File(directory).listFiles()?.filter { it.extension == "kt" } ?.forEach {
                val clazz = Class.forName(it.path.asPackageName).kotlin

                if (clazz.hasAnnotation<RegisterCommand>() && clazz.isSubclassOf(GuildChatInputCommand::class))
                    registry += clazz.objectInstance as GuildChatInputCommand
            }
        }
    }
}