plugins {
    kotlin("jvm") version "2.0.21"
    /*kotlin("plugin.serialization") version "2.0.21"

    id("dev.kordex.gradle.kordex") version "+"
    id("com.google.devtools.ksp")  version "2.0.21-1.0.28"*/

    idea
}

allprojects {

    group = "kito.metapolemika"
    version = "1.0-SNAPSHOT"

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    idea {
        module {
            val src = file("build/generated/ksp/main/kotlin")

            sourceDirs          =          sourceDirs + src
            generatedSourceDirs = generatedSourceDirs + src
        }
    }

    dependencies {
        implementation(libs.kotlin.stdlib)
        testImplementation(kotlin("test"))

        implementation("ch.qos.logback:logback-classic:1.5.0")
        implementation("ch.qos.logback:logback-core:1.5.0")
    }

    tasks.test { useJUnitPlatform() }

    kotlin { jvmToolchain(21) }
}

/*repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}*/

/*ksp {
    arg("ksp.incremental", "true")
}*/

/*dependencies {
    implementation("com.google.dagger:dagger-compiler:2.51.1")
    ksp("com.google.dagger:dagger-compiler:2.51.1")
    ksp("com.google.devtools.ksp:symbol-processing-api:2.0.21-1.0.28")
}*/

