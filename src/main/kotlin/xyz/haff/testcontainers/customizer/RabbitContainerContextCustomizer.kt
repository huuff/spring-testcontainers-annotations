package xyz.haff.testcontainers.customizer

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import org.testcontainers.containers.RabbitMQContainer
import xyz.haff.testcontainers.annotation.RabbitContainerTest
import xyz.haff.testcontainers.util.ContainerFactory

private fun createContainer(tag: String) = RabbitMQContainer("rabbitmq:$tag").apply {
    start()
}

class RabbitContainerContextCustomizer(
    private val annotation: RabbitContainerTest,
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
                "RabbitMQ Testcontainer Properties",
                mapOf(
                    "spring.rabbitmq.host" to "localhost",
                    "spring.rabbitmq.port" to container.amqpPort,
                    "spring.rabbitmq.username" to container.adminUsername,
                    "spring.rabbitmq.password" to container.adminPassword
                )

            )
        )
    }

}