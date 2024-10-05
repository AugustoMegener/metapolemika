package kito.metapolemika.reflect

import javax.naming.Name
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

object ObjectRegister : ObjectScanner<Any>() {

    internal val registriesMap = HashMap<String, ObjectRegistry<*>>()
    val registries by lazy { HashMap(registriesMap) }


    override fun shouldAccept(clazz: KClass<*>) = clazz.hasAnnotation<Register>()

    override fun accept(obj: Any)
        { registriesMap[(obj::class.annotations.first { it is Register } as Register).id]?.register(obj) }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> of(): ObjectRegistry<T> =
        registries.values.first { it.type.isSubclassOf(T::class)} as ObjectRegistry<T>


    fun register() {
        RegistryRegister.run()
        run()
    }


    data class ObjectRegistry<T : Any>(val type: KClass<T>) : Iterable<T> {
        private val entries = arrayListOf<T>()

        internal fun register(obj: Any) { @Suppress("UNCHECKED_CAST") (obj as? T)?.let { entries += obj } }

        override fun iterator() = entries.iterator()
    }


    private object RegistryRegister : ClassScanner<Any>() {

        override fun shouldAccept(clazz: KClass<*>) = clazz.hasAnnotation<Registry>()

        override fun accept(obj: KClass<Any>) {
            registriesMap[(obj.annotations.first { it is Registry } as Registry).id] = ObjectRegistry(obj)
        }
    }


    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Registry(val id: String)


    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Register(val id: String)
}