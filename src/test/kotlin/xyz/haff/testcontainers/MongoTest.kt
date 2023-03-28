package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import xyz.haff.testcontainers.annotation.MongoContainerTest

@DataMongoTest
@MongoContainerTest(tag = "5.0.15")
class MongoTest(
    private val mongoTemplate: MongoTemplate,
) : FunSpec({

    test("correctly starts MongoDB container") {
        val status = mongoTemplate.executeCommand("{ serverStatus: 1 }")

        status.get("version") shouldBe "5.0.15"
    }
})