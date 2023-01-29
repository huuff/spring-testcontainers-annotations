import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    id("org.jetbrains.kotlin.plugin.spring") version libs.versions.kotlin.get()

    id("org.springframework.boot") version libs.versions.spring.boot.get()
}

group = "xyz.haff"
version = "0.1.0"

tasks.wrapper {
    gradleVersion = "7.4"
}

repositories {
    mavenCentral()

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))
    implementation(platform("org.testcontainers:testcontainers-bom:${libs.versions.testcontainers.get()}"))

    implementation("org.testcontainers:mongodb")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("io.kotest:kotest-runner-junit5:${libs.versions.kotest.get()}")
    testImplementation("io.kotest:kotest-assertions-core:${libs.versions.kotest.get()}")
    // TODO: These versions in the catalog
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

tasks.bootJar {
    enabled = false
}