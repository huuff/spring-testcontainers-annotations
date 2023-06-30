rootProject.name = "spring-testcontainers-annotations"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.7.20")
            version("spring-boot", "3.0.1")
            version("testcontainers", "1.18.3")
            version("kotest", "5.5.4")
        }
    }
}