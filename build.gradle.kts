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
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))
    implementation(platform("org.testcontainers:testcontainers-bom:${libs.versions.testcontainers.get()}"))

    implementation("org.testcontainers:mongodb")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(kotlin("test"))
    testImplementation("org.testcontainers:junit-jupiter")
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