package reflect

import kito.metapolemika.reflect.ClassScanner
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object ObjectRegister : ObjectScanner<Any>() {

    val registries = HashMap<String, ObjectRegistry<*>>()


    override fun shouldAccept(clazz: KClass<*>) = clazz.hasAnnotation<Register>()

    override fun accept(obj: Any)
        { (obj::class.annotations.filterIsInstance<Register>().forEach { registries[it.id]?.register(obj) }) }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> of(id: String): ObjectRegistry<T> = registries[id]!! as ObjectRegistry<T>


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
            registries[(obj.annotations.first { it is Registry } as Registry).id] = ObjectRegistry(obj)
        }
    }

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Registry(val id: String)


    @Repeatable
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Register(val id: String)
}