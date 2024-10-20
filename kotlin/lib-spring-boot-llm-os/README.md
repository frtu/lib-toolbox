# Project - lib-spring-boot-llm-os

## About

Provide a platform for LLM :

* Abstract OpenAI api using `OpenAiCompatibleChat` (can call [Ollama](https://ollama.com/))
* Register tool with Function calls

## Import

Import using Maven :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-spring-boot-llm-os</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-spring-boot-llm-os:${Versions.frtu_libs}")
```

or TOML

```
spring-boot-slack = { group = "com.github.frtu.libs", name = "lib-spring-boot-slack", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-spring-boot-llm-os.svg?label=latest%20release%20:%20lib-spring-boot-llm-os"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Compatibilities

### Spring Boot 2.x

Spring Boot 2.x requires Properties class to import version specific
annotation `org.springframework.boot.context.properties.ConstructorBinding` that is different from Spring Boot 3.x.

In order to enable properties loads from Spring Boot 2, create your own class that extends from `ChatApiProperties` :

```kotlin
import com.github.frtu.kotlin.llm.spring.config.ChatApiProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
class ChatApiPropertiesSpringBoot2(
    apiKey: String? = null,
    model: String = LOCAL_MODEL, // "mistral"
    baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
) : ChatApiProperties(
    apiKey,
    model,
    baseUrl,
)
```

You also need to copy your own version
of [LlmOsAutoConfigs.kt](src%2Fmain%2Fkotlin%2Fcom%2Fgithub%2Ffrtu%2Fkotlin%2Fllm%2Fspring%2Fconfig%2FLlmOsAutoConfigs.kt)
and exclude the current one :

```yaml
spring.autoconfigure.exclude:
  - com.github.frtu.kotlin.llm.spring.config.LlmOsAutoConfigs
```

## Release notes

### 2.0.6 - Current version

* Adding `Tool` & `Executable` as function & agent abstraction
* Refactor `Function`* into dedicate package `com.github.frtu.kotlin.llm.os.tool.function`
* Enable to override and extends configs & properties for Spring Boot 2.x

### 2.0.5

* Bump to lib aallam-openai -> `3.8.0`

### 2.0.4

* Initial version from [github.com/frtu/agents-os](https://github.com/frtu/agents-os)
