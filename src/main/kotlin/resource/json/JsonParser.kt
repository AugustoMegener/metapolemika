package kito.metapolemika.resource.json

import kito.metapolemika.reflect.ObjectRegister
import kito.metapolemika.resource.ResourceManager.json
import kotlinx.serialization.json.JsonObject

@ObjectRegister.Registry("JSON_PARSER")
abstract class JsonParser(location: String) {

    private val location = "$location.json"

    fun scan()
    { (this::class.java.getResourceAsStream(location)?.bufferedReader()?.use { it.readText() } ?:
       throw IllegalArgumentException("Can not find any json on $location."))
            .let { parse(json.decodeFromString(JsonObject.serializer(), it)) } }

    abstract fun parse(json: JsonObject)
}