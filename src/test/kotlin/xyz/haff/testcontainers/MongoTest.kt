package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import xyz.haff.testcontainers.annotation.MongoContainerTest

// TODO: This is not working yet... something strange might be happening but I might be missing some logs
// check my quotebox project
// TODO: is the testcontainers annotation necessary?
@Testcontainers
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