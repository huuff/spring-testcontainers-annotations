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
class MongoContainerContextCustomizerFactory : ContextCustomizerFactory {

    override fun createContextCustomizer(
        testClass: Class<*>,
        configAttributes: MutableList<ContextConfigurationAttributes>
    ): ContextCustomizer? = when {
        AnnotatedElementUtils.hasAnnotation(testClass, MongoContainerTest::class.java) -> {
            val annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, MongoContainerTest::class.java)!!
            MongoContainerTestContextCustomizer(annotation)
        }
        else -> null
    }

    private class MongoContainerTestContextCustomizer(
        private val annotation: MongoContainerTest,
    ) : ContextCustomizer {

        companion object {
            private val persistentContainers: MutableMap<String, MongoDBContainer> = mutableMapOf()
        }

        private fun createContainer(tag: String)
            = MongoDBContainer("mongo:${annotation.tag}").apply { this.start() }

        override fun customizeContext(
            context: ConfigurableApplicationContext,
            mergedConfig: MergedContextConfiguration
        ) {
            val container = if (annotation.persistent) {
                persistentContainers.getOrPut(annotation.tag) {
                    createContainer(annotation.tag)
                }
            } else {
                createContainer(annotation.tag)
            }


            context.environment.propertySources.addFirst(
                MapPropertySource(
                    "MongoDB Testcontainer Properties",
                    mapOf("spring.data.mongodb.uri" to container.replicaSetUrl)
                )
            )
        }

    }
}