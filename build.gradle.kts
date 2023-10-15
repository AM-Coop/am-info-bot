import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
}

group = "ru.am"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.telegram:telegrambots:6.5.0")
    // implementation("io.github.microutils:kotlin-logging:3.0.5")
// https://mvnrepository.com/artifact/org.openapitools/openapi-generator
//     implementation("org.openapitools:openapi-generator:5.4.0")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.projectreactor:reactor-core")
    implementation("org.postgresql:postgresql")
// https://mvnrepository.com/artifact/org.postgresql/postgresql
//     implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.liquibase:liquibase-core:4.20.0")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.h2database:h2")

}



tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
