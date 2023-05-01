package xyz.haff.testcontainers.customizer

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.utility.DockerImageName
import xyz.haff.testcontainers.annotation.RedisContainerTest
import xyz.haff.testcontainers.util.ContainerFactory

private fun createContainer(tag: String): GenericContainer<*> =
    GenericContainer(DockerImageName.parse("redis:$tag")).apply {
        withExposedPorts(6379)
        waitingFor(LogMessageWaitStrategy().apply {
            withRegEx(".*Ready to accept connections.*")
        })
        start()
    }

class RedisContainerContextCustomizer(
    private val annotation: RedisContainerTest,
) : ContextCustomizer {

    companion object {
        private val containerFactory = ContainerFactory(::createContainer)
    }

    override fun customizeContext(context: ConfigurableApplicationContext, mergedConfig: MergedContextConfiguration) {
        val container = containerFactory.get(annotation.tag, annotation.persistent)

        context.environment.propertySources.addFirst(
            MapPropertySource(
                "Redis Testcontainer Properties",
                mapOf(
                    "spring.data.redis.host" to container.host,
                    "spring.data.redis.port" to container.getMappedPort(6379).toString(),
                )
            )
        )
    }


}