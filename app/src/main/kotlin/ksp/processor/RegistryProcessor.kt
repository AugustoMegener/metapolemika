package ksp.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass


class RegistryProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {

        logger.info("hey... i'm here...")

        generate(resolver.getSymbolsWithAnnotation(RegisterObject::class.qualifiedName!!)
                    .filterIsInstance<KSClassDeclaration>()
                    .map { it.getAnnotationsByType(RegisterObject::class).first().owner to it }
                    .groupBy({ it.first }, { it.second }))

        return listOf()
    }

    private fun generate(objs: Map<KClass<*>, List<KSClassDeclaration>>) {

        val packageName = "kito.metapolemika.generated"

        codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = packageName,
            fileName = "Registries"
        ).use { s ->
            s.write(
                """
                    package $packageName
                    
                    ${objs.map { (t, u) ->
                        "import ${t.qualifiedName!!}\n" + 
                        u.joinToString { "import ${it.qualifiedName!!.getQualifier()}\n" }
                    }.joinToString()}}
                    
                    ${objs.map { (t, u) ->
                        val className = t.simpleName!!
        
                        """
                            private val ${className}Members = listOf(
                                ${u.joinToString { "${it.simpleName.getShortName()},\n" }}
                            )
                            
                            val $className.registry get() = ${className}Members
                        """.trimIndent()
                    }.joinToString("\n")}
                    
                """.trimIndent().toByteArray()
            )
        }
    }
}

class RegistryProcessorProvider : SymbolProcessorProvider {

    init {
        println(":c")
    }

    override fun create(environment: SymbolProcessorEnvironment) =
        environment.run { RegistryProcessor(codeGenerator, logger) }
}

@Target(CLASS) @Retention(SOURCE)
annotation class RegisterObject(val owner: KClass<*>)