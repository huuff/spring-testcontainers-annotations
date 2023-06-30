package xyz.haff.testcontainers.customizer

import xyz.haff.testcontainers.annotation.MongoContainerTest
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.test.context.ContextConfigurationAttributes
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.ContextCustomizerFactory
import xyz.haff.testcontainers.annotation.RabbitContainerTest
import xyz.haff.testcontainers.annotation.RedisContainerTest
import xyz.haff.testcontainers.annotation.RedisStackContainerTest

class TestcontainersContextCustomizerFactory : ContextCustomizerFactory {

    override fun createContextCustomizer(
        testClass: Class<*>,
        configAttributes: MutableList<ContextConfigurationAttributes>
    ): ContextCustomizer? = when {
        AnnotatedElementUtils.hasAnnotation(testClass, MongoContainerTest::class.java) -> {
            val annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, MongoContainerTest::class.java)!!
            MongoContainerTestContextCustomizer(annotation)
        }
        AnnotatedElementUtils.hasAnnotation(testClass, RedisContainerTest::class.java) -> {
            val annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, RedisContainerTest::class.java)!!
            RedisContainerContextCustomizer(annotation)
        }
        AnnotatedElementUtils.hasAnnotation(testClass, RedisStackContainerTest::class.java) -> {
            val annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, RedisStackContainerTest::class.java)!!
            RedisStackContainerContextCustomizer(annotation)
        }
        AnnotatedElementUtils.hasAnnotation(testClass, RabbitContainerTest::class.java) -> {
            val annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, RabbitContainerTest::class.java)!!
            RabbitContainerContextCustomizer(annotation)
        }
        else -> null
    }
}