package kito.metapolemika.resource

import kito.metapolemika.reflect.ObjectRegister
import kito.metapolemika.resource.json.JsonParser
import kotlinx.serialization.json.Json

object ResourceManager {

    val json = Json { ignoreUnknownKeys = true }

    fun start() {
        ObjectRegister.of<JsonParser>("JSON_PARSER").forEach(JsonParser::scan)
    }
}