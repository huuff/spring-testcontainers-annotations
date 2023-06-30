package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.context.annotation.Import
import xyz.haff.testcontainers.annotation.RabbitContainerTest

@Import(RabbitAutoConfiguration::class)
@RabbitContainerTest(tag = "3", persistent = true)
class RabbitTest(
    private val rabbitTemplate: RabbitTemplate,
) : FunSpec({

    test("rabbit") {
        rabbitTemplate.convertAndSend("test")
    }
})