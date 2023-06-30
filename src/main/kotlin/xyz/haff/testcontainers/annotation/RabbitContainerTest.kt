package xyz.haff.testcontainers.annotation

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
annotation class RabbitContainerTest(
    val tag: String = "latest",
    val persistent: Boolean = true,
)
