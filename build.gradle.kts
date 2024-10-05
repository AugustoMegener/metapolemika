plugins {
    kotlin("jvm") version "2.0.0"
}

group = "kito.metapolemika"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation(kotlin("test"))

    implementation("dev.kord:kord-core:${project.property("kord_version")}")

    implementation("org.hibernate:hibernate-core:${project.property("hibernate_version")}")
    implementation("org.hibernate:hibernate-hikaricp:${project.property("hibernate_version")}")
    implementation("mysql:mysql-connector-java:${project.property("mysql_connector_version")}")

    implementation("ch.qos.logback:logback-classic:1.4.12")

    implementation("org.reflections:reflections:0.10.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}