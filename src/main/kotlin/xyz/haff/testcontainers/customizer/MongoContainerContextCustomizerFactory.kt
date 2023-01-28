package xyz.haff.testcontainers.customizer

import xyz.haff.testcontainers.annotation.MongoContainerTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfigurationAttributes
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.ContextCustomizerFactory
import org.springframework.test.context.MergedContextConfiguration
import org.testcontainers.containers.MongoDBContainer

// TODO: Test it
// TODO: Get version from annotation, maybe even digests
// TODO: Allow creating a different container for each test, instead of reusing one for all
private val container by lazy { MongoDBContainer("mongo:focal").apply { this.start() } }
class MongoContainerContextCustomizerFactory : ContextCustomizerFactory {

    override fun createContextCustomizer(
        testClass: Class<*>,
        configAttributes: MutableList<ContextConfigurationAttributes>
    ): ContextCustomizer? = if (AnnotatedElementUtils.hasAnnotation(testClass, MongoContainerTest::class.java)) {
        MongoContainerTestContextCustomizer()
    } else { null }

    private inner class MongoContainerTestContextCustomizer : ContextCustomizer {
        override fun customizeContext(
            context: ConfigurableApplicationContext,
            mergedConfig: MergedContextConfiguration
        ) {
            context.environment.propertySources.addFirst(MapPropertySource("MongoDB Testcontainer Properties",
                mapOf("spring.data.mongodb.uri" to container.replicaSetUrl)
            ))
        }

    }
}