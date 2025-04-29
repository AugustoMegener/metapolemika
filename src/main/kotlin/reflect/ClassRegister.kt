package kito.metapolemika.reflect

import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object ClassRegister : ClassScanner<Any>() {

    val registries = HashMap<String, ClassRegistry<*>>()


    override fun shouldAccept(clazz: KClass<*>) = clazz.hasAnnotation<Register>()

    override fun accept(obj: KClass<Any>)
        { (obj.annotations.filterIsInstance<Register>().forEach { registries[it.id]?.register(obj) }) }

    fun <T : Any>  of(id: String): ClassRegistry<T> = registries[id]!! as ClassRegistry<T>

    fun register() {
        RegistryRegister.run()
        run()
    }

    data class ClassRegistry<T : Any>(val type: KClass<out T>) : Iterable<KClass<out T>> {
        private val entries = arrayListOf<KClass<out T>>()

        internal fun register(obj: KClass<*>) { entries += obj as KClass<out T> }

        override fun iterator() = entries.iterator()
    }


    private object RegistryRegister : ClassScanner<Any>() {

        override fun shouldAccept(clazz: KClass<*>) = clazz.hasAnnotation<Registry>()

        override fun accept(obj: KClass<Any>) {
            registries[(obj.annotations.first { it is Registry } as Registry).id] = ClassRegistry(obj)
        }
    }

    @Scan
    @Repeatable
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Register(val id: String)

    @Scan
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Registry(val id: String)
}