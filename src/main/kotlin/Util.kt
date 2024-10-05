package kito.metapolemika

import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import kotlinx.coroutines.runBlocking

val String.asPackageName get() = removeSuffix(".kt").replace('\\', '.').replace("src.main.kotlin.", "kito.metapolemika.")

val GuildChatInputCommandInteraction.ephemeralResponse get() = runBlocking { deferEphemeralResponse() }
val GuildChatInputCommandInteraction.   publicResponse get() = runBlocking {    deferPublicResponse() }