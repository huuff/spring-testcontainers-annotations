package xyz.haff.testcontainers.customizer

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.utility.DockerImageName
import xyz.haff.testcontainers.annotation.RedisContainerTest
import xyz.haff.testcontainers.annotation.RedisStackContainerTest

private fun createContainer(tag: String): GenericContainer<*> =
    GenericContainer(DockerImageName.parse("redis/redis-stack:$tag")).apply {
        withExposedPorts(6379)
        waitingFor(LogMessageWaitStrategy().apply {
            withRegEx(".*Ready to accept connections.*")
        })
        start()
    }

class RedisStackContainerContextCustomizer(
    private val annotation: RedisStackContainerTest,
) : ContextCustomizer {

    companion object {
        private val persistentContainers: MutableMap<String, GenericContainer<*>> = mutableMapOf()
    }

    override fun customizeContext(context: ConfigurableApplicationContext, mergedConfig: MergedContextConfiguration) {
        // TODO: DRY this and the mongodb one, and especially the redis one
        val container = if (annotation.persistent) {
            persistentContainers.getOrPut(annotation.tag) {
                createContainer(annotation.tag)
            }
        } else {
            createContainer(annotation.tag)
        }

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