plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"

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

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation(kotlin("test"))

    implementation(libs.kord.extensions)
    implementation("dev.kordex:dev-unsafe")

    implementation("org.jetbrains.exposed:exposed-core:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-dao:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-crypt:${project.property("exposed_version")}")

    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:${project.property("exposed_version")}")

    implementation("org.jetbrains.exposed:exposed-json:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-money:${project.property("exposed_version")}")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${project.property("exposed_version")}")

    implementation("org.postgresql:postgresql:${project.property("postgresql_version")}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}