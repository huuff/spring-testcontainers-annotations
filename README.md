# Spring Testcontainers Annotation
Autoconfigure testcontainers for Spring Boot tests

## Deprecated?
I did this without realizing there was a parallel work to do the same scheduled for release in Spring Boot 3.1.0, so maybe this is already deprecated? [See the relevant issue here](https://github.com/spring-projects/spring-boot/issues/34658)

## Usage
Annotate your test with the corresponding Spring Data test annotation (to autoconfigure Spring Data) and then also add the corresponding Testcontainers annotation. For example, for a Redis test.
```kotlin
@DataRedisTest
@RedisContainerTest
class RedisTest {
  ...
}
```

You'll also have to add the context customizer to actually enrich your test's context with the container. Place the following in your `src/test/resources/META-INF/spring.factories` file:

```
org.springframework.test.context.ContextCustomizerFactory=\
  xyz.haff.testcontainers.customizer.TestcontainersContextCustomizerFactory
```

You can also specify in your annotation the tag of the container and whether it's to be made persistent (shared across tests that use the same tag)

```kt
@RedisContainerTest(persistent = true, tag = "latest")
```
