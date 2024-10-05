package kito.metapolemika.reflect

import kito.metapolemika.asPackageName
import java.io.File
import kotlin.reflect.KClass

abstract class ObjectProcessor<T> {
    open val directory: String = "src/main/kotlin"

    val registry = mutableListOf<T>()

    abstract fun shouldInclude(clazz: KClass<*>) : Boolean

    fun scan() {
        File(directory).listFiles()?.filter { it.extension == "kt" } ?.forEach {
            val clazz = Class.forName(it.path.asPackageName).kotlin

            if (shouldInclude(clazz))
                @Suppress("UNCHECKED_CAST") (clazz.objectInstance as? T)?.let { o -> registry += o }
        }
    }
}