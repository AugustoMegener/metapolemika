package kito.metapolemika.reflect

import io.ktor.util.*
import kito.metapolemika.asPackageName
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

abstract class ObjectScanner<T> : ReflectScanner<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun cast(clazz: KClass<*>) = clazz.objectInstance as? T
}