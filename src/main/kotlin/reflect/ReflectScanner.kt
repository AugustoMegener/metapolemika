package kito.metapolemika.reflect

import io.ktor.util.*
import kito.metapolemika.asPackageName
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

abstract class ReflectScanner<T> {
    open val directory: String = "src/main/kotlin"

    abstract fun shouldAccept(clazz: KClass<*>) : Boolean
    abstract fun cast(clazz: KClass<*>) : T?
    abstract fun accept(obj: T)

    fun run() {
        Files.walk(Paths.get(directory)).filter { it.extension == "kt" } ?.forEach {
            try {
                val clazz = Class.forName(it.toString().asPackageName).kotlin

                if (shouldAccept(clazz)) cast(clazz)?.let(::accept)
            }
            catch (_: ClassNotFoundException) { }
        }
    }
}