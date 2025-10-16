package kito.metapolemika.discord

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Entity
import kito.metapolemika.core.form.sheet.SaveableSheetForm
import kito.metapolemika.database.entity.DiscordEntityCacheData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.KProperty

private val entityCache = HashMap<Snowflake, EntityCache>()

val Snowflake.cache get() = run {
    val id = this

    transaction {
        entityCache.computeIfAbsent(id) {
            EntityCache(
                DiscordEntityCacheData.findById(value.toLong())?.data?.let { Json.decodeFromString(it) } ?: mapOf()
            )
        }
    }
}

val Entity   .cache get() = id.cache

inline infix fun <reified T> Snowflake.cacheOf(type: CacheType<T>) = cache of type
inline infix fun <reified T> Entity   .cacheOf(type: CacheType<T>) = cache of type

val unfinishedSheets = CacheType<ArrayList<SaveableSheetForm>>("unfinished_sheets") { arrayListOf() } .persist()

@Suppress("UNCHECKED_CAST")
class EntityCache(val oldData: Map<String, String>) {

    @ApiStatus.Internal
    val cache = hashMapOf<CacheType<*>, Cache<*>>()

    inline infix fun <reified T> of(type: CacheType<T>) =
        cache.computeIfAbsent(type) {
            Cache(oldData[type.id]?.let { Json.decodeFromString(it) } ?: type.default(), serializer<T>())
        } as Cache<T>

    fun encode() = Json.encodeToString(oldData + cache.map { (k, v) -> k.id to v.encode() }.toMap())

    fun save(id: Snowflake) {
        transaction {

            val cache = DiscordEntityCacheData.findById(id.value.toLong())

            val foo = encode()

            if (cache != null) cache.data = foo
            else DiscordEntityCacheData.new(id.value.toLong()) { data = foo }
        }
    }
}

class CacheType<T>(val id: String, val default: () -> T) {
    var shouldSave = false; private set

    fun persist() = also { shouldSave = true }
}

data class Cache<T>(private var value: T, val serializer: KSerializer<T>) {
    operator fun getValue(cls: Nothing?, field: KProperty<*>) = value
    operator fun setValue(cls: Nothing?, field: KProperty<*>, new: T) { value = new }

    operator fun getValue(cls: Any?, field: KProperty<*>) = value
    operator fun setValue(cls: Any?, field: KProperty<*>, new: T) { value = new }

    fun encode() = Json.encodeToString(serializer, value)

    companion object {
        inline fun <reified T> decode(json: String) = Cache(Json.decodeFromString<T>(json), serializer())
    }
}

class CacheAccess<T, C>(cache: Cache<T>, private val getter: (T) -> C, private val setter: ((T, C) -> Unit)? = null) {
    private var cache by cache

    operator fun getValue(cls: Nothing?, field: KProperty<*>) = getter(cache)
    operator fun setValue(cls: Nothing?, field: KProperty<*>, new: C)
        { setter?.invoke(cache, new) ?: throw UnsupportedOperationException("This CacheAccess does not have a defined setter" +
                                                                     " so it should not be considered mutable.") }
}

infix fun <T, C> Cache<T>.accessing(getter: (T) -> C) = CacheAccess(this, getter)
infix fun <T, C> Cache<T>.accessing(agrs: Pair<(T) -> C, (T, C) -> Unit>) = CacheAccess(this, agrs.first, agrs.second)