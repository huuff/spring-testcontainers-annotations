package xyz.haff.testcontainers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.StringRedisTemplate
import xyz.haff.testcontainers.annotation.RedisContainerTest

@DataRedisTest
@RedisContainerTest
class RedisTest(
    private val redisTemplate: StringRedisTemplate,
): FunSpec({

    test("can connect to the container") {
        redisTemplate.opsForValue().set("key", "value")

        redisTemplate.opsForValue().get("key") shouldBe "value"
    }
})