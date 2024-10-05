package kito.metapolemika

import kito.metapolemika.discord.DiscordApp
import kito.metapolemika.reflect.ObjectRegister

suspend fun main() {
    ObjectRegister.register()

    DiscordApp.start()
}