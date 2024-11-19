plugins {
    kotlin("jvm")
}

group = "kito.metapolemika"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "KordEx (Snapshots)"
        url = uri("https://repo.kordex.dev/snapshots")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.kotlin.stdlib)
    implementation(kotlin("reflect"))
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

    implementation("org.jetbrains.kotlin:kotlin-scripting-common")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")

    implementation("ch.qos.logback:logback-classic:1.5.0")
    implementation("ch.qos.logback:logback-core:1.5.0")
}

tasks.test {
    useJUnitPlatform()
}