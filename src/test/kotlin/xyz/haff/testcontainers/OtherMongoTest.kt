package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import xyz.haff.testcontainers.annotation.MongoContainerTest

/**
 * A literal copy-paste of the other MongoTest so I can check whether the container is reused.
 * Disabled because it's not particularly useful unless checking the logs, but I really don't know how to check
 * otherwise
 */
@DataMongoTest
@Disabled
@MongoContainerTest(tag = "5.0.15", persistent = true)
class OtherMongoTest(
    private val mongoTemplate: MongoTemplate,
) : FunSpec({

    xtest("correctly starts MongoDB container (second)") {
        val status = mongoTemplate.executeCommand("{ serverStatus: 1 }")

        status["version"] shouldBe "5.0.15"
    }
})