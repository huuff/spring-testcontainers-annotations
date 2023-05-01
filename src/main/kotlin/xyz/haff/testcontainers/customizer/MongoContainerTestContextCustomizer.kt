package xyz.haff.testcontainers.customizer

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import org.testcontainers.containers.MongoDBContainer
import xyz.haff.testcontainers.annotation.MongoContainerTest
import xyz.haff.testcontainers.util.ContainerFactory

private fun createContainer(tag: String) = MongoDBContainer("mongo:$tag").apply {
    start()
}

class MongoContainerTestContextCustomizer(
    private val annotation: MongoContainerTest,
) : ContextCustomizer {

    companion object {
        private val containerFactory = ContainerFactory(::createContainer)
    }

    override fun customizeContext(
        context: ConfigurableApplicationContext,
        mergedConfig: MergedContextConfiguration
    ) {
        val container = containerFactory.get(annotation.tag, annotation.persistent)

        context.environment.propertySources.addFirst(
            MapPropertySource(
                "MongoDB Testcontainer Properties",
                mapOf("spring.data.mongodb.uri" to container.replicaSetUrl)
            )
        )
    }

}