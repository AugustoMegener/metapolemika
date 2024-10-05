package kito.metapolemika

import kito.metapolemika.discord.DiscordApp
import kito.metapolemika.discord.command.RegisterCommand

suspend fun main() {
    RegisterCommand.scan()

    DiscordApp.start()
}