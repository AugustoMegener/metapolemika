package kito.metapolemika.reflect

import io.github.classgraph.ClassGraph
import kotlin.reflect.KClass

abstract class ReflectScanner<T> {
    open val directory: String = "src/main/kotlin"

    abstract fun shouldAccept(clazz: KClass<*>) : Boolean
    abstract fun cast(clazz: KClass<*>) : T?
    abstract fun accept(obj: T)

    fun run() {

        /*val reflections = Reflections("kito.metapolemika", Scanners.SubTypes.filterResultsBy { true })
        val foo = reflections.getSubTypesOf(Any::class.java)*/

        /*TypePool.Default
            .ofSystemLoader()
            .describe("kito.metapolemika.*")
            .resolve()
            .filter { def ->
                val annotations = def.asErasure().declaredAnnotations

                (annotations.isAnnotationPresent(Scan::class.java) || annotations.any
                    { it.annotationType.asErasure().declaredAnnotations.isAnnotationPresent(Scan::class.java) })
            }
            .map { Class.forName(it.typeName) }
            .forEach { if (shouldAccept(it.kotlin)) cast(it.kotlin)?.let(::accept) }*/

        ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .acceptPackages("kito.metapolemika.*")
            .scan()
            .allClasses
            .filter { info -> info.hasAnnotation(Scan::class.java) ||
                              info.annotations.any { it.hasAnnotation(Scan::class.java) } }
            .map { Class.forName(it.name).kotlin }
            .forEach { if (shouldAccept(it)) cast(it)?.let(::accept) }

       /* Files.walk(Paths.get(directory)).filter { it.extension == "kt" } ?.forEach {
            try {
                val clazz = Class.forName(it.toString()).kotlin

                if (shouldAccept(clazz)) cast(clazz)?.let(::accept)
            }
            catch (_: ClassNotFoundException) { }*/
    }
}
