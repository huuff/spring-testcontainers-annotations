import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    id("org.jetbrains.kotlin.plugin.spring") version libs.versions.kotlin.get()

    //id("org.springframework.boot") version libs.versions.spring.boot.get()

    id("maven-publish")
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("signing")
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
    testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    testImplementation("org.springframework.boot:spring-boot-starter-data-redis")
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

//tasks.bootJar {
    //mainClass.set("xyz.haff.testcontainers.TestcontainersAnnotationsApplication")
//}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                packaging = "jar"
                name.set(project.name)
                description.set("Annotations for quickly firing containers in Spring tests")

                url.set("https://github.com/huuff/spring-testcontainers-annotations")
                scm {
                    connection.set("scm:git:git://github.com/huuff/spring-testcontainers-annotations.git")
                    developerConnection.set("scm:git:git@github.com:huuff/spring-testcontainers-annotations.git")
                    url.set("https://github.com/huuff/spring-testcontainers-annotations/tree/master")
                }

                licenses {
                    license {
                        name.set("WTFPL - Do What The Fuck You Want To Public License")
                        url.set("http://www.wtfpl.net")
                    }
                }

                developers {
                    developer {
                        name.set("Francisco Sánchez")
                        email.set("haf@protonmail.ch")
                        organizationUrl.set("https://github.com/huuff")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(properties["sonatype.user"] as String)
            password.set(properties["sonatype.password"] as String)
        }
    }
}