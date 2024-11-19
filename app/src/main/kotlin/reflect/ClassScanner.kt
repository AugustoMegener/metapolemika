package kito.metapolemika.reflect

import reflect.ReflectScanner
import kotlin.reflect.KClass

abstract class ClassScanner<T : Any> : ReflectScanner<KClass<T>>() {

    @Suppress("UNCHECKED_CAST")
    override fun cast(clazz: KClass<*>) = clazz as? KClass<T>
}