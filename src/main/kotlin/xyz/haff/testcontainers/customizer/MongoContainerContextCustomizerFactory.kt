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

// TODO: It's likely that I'll need a customizer that's valid for all annotations
// TODO: Allow creating a different container for each test, instead of reusing one for all
class MongoContainerContextCustomizerFactory : ContextCustomizerFactory {

    override fun createContextCustomizer(
        testClass: Class<*>,
        configAttributes: MutableList<ContextConfigurationAttributes>
    ): ContextCustomizer? = when {
        AnnotatedElementUtils.hasAnnotation(testClass, MongoContainerTest::class.java) -> {
            val annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, MongoContainerTest::class.java)!!
            MongoContainerTestContextCustomizer(annotation.tag)
        }
        else -> null
    }

    private class MongoContainerTestContextCustomizer(
        private val tag: String,
    ) : ContextCustomizer {
        override fun customizeContext(
            context: ConfigurableApplicationContext,
            mergedConfig: MergedContextConfiguration
        ) {
            val container = MongoDBContainer("mongo:$tag").apply { this.start() }

            context.environment.propertySources.addFirst(
                MapPropertySource(
                    "MongoDB Testcontainer Properties",
                    mapOf("spring.data.mongodb.uri" to container.replicaSetUrl)
                )
            )
        }

    }
}