package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import xyz.haff.testcontainers.annotation.MongoContainerTest

/**
 * A literal copy-paste of the other MongoTest so I can check whether the container is reused
 */
@DataMongoTest
@MongoContainerTest(tag = "5.0.15", persistent = true)
class OtherMongoTest(
    private val mongoTemplate: MongoTemplate,
) : FunSpec({

    test("correctly starts MongoDB container (second)") {
        val status = mongoTemplate.executeCommand("{ serverStatus: 1 }")

        status["version"] shouldBe "5.0.15"
    }
})