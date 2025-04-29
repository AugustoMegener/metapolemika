import dev.kordex.gradle.plugins.kordex.DataCollection

plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"

    id("dev.kordex.gradle.kordex") version "1.5.8"
}

group = "kito.metapolemika"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenLocal()
    mavenCentral()

    maven {
        name = "KordEx (Snapshots)"
        url = uri("https://repo.kordex.dev/snapshots")
    }
}

kordEx {
    bot {
        dataCollection(DataCollection.Standard)

        mainClass = "kito.metapolemika.MainKt"

        module("dev-unsafe")
    }

    i18n {
        classPackage = "src.main.kotlin.discord.i18n"
        translationBundle = "metapolemika.strings"
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.exposed:exposed-core:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-dao:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-crypt:${project.property("exposed_version")}")

    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:${project.property("exposed_version")}")

    implementation("org.jetbrains.exposed:exposed-json:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-money:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:" +
            "${project.property("exposed_version")}")

    implementation("org.postgresql:postgresql:${project.property("postgresql_version")}")
    //implementation("mysql:mysql-connector-java:${project.property("mysql_version")}")

    implementation("io.github.classgraph:classgraph:4.8.162")

    implementation("org.jetbrains.kotlin:kotlin-scripting-common")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")

    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("ch.qos.logback:logback-core:1.5.13")
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}