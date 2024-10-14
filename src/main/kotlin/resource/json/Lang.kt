package kito.metapolemika.resource.json

import kito.metapolemika.reflect.ObjectRegister
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@ObjectRegister.Register("JSON_PARSER")
object Lang : JsonParser("/lang") {

    private lateinit var lang: Map<String, String>

    operator fun get(string: String) = lang[string]!!

    override fun parse(json: JsonObject) {
        lang = json.entries.associate { it.key to it.value.jsonPrimitive.content }
    }
}