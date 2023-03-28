package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import xyz.haff.testcontainers.annotation.MongoContainerTest

@DataMongoTest
@MongoContainerTest
class MongoTest(
    private val mongoTemplate: MongoTemplate,
) : FunSpec({

    test("correctly starts MongoDB container") {
        val status = mongoTemplate.executeCommand("{ serverStatus: 1 }")

        println(status)
    }
})