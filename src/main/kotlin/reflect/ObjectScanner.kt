package kito.metapolemika.reflect

import kotlin.reflect.KClass

abstract class ObjectScanner<T> : ReflectScanner<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun cast(clazz: KClass<*>) = clazz.objectInstance as? T
}