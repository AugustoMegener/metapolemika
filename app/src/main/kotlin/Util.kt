import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.util.*

val String.asPackageName get() = removeSuffix(".kt").replace('\\', '.').replace("src.main.kotlin.", "kito.metapolemika.")

val String.asTitleCase get() = split(" ").joinToString(" ")
    { it.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString() } }

val String.isPascalCase get() = Regex("^[A-Z][a-z]+(?:[A-Z][a-z]+)*$").matches(this)

val String.isImageUrl get() : Boolean = runBlocking {
    httpClient.use { try { it.head(this@isImageUrl).headers[HttpHeaders.ContentType]?.startsWith("image/") == true }
                       catch (e: Exception) { false } }
}

val String.isValidUrl : Boolean get() = runBlocking {
    httpClient.use { try { it.get(this@isValidUrl).status == HttpStatusCode.OK } catch (e: Exception) { false } }
}

val GuildChatInputCommandInteraction.ephemeralResponse get() = runBlocking { deferEphemeralResponse() }
val GuildChatInputCommandInteraction.   publicResponse get() = runBlocking {    deferPublicResponse() }