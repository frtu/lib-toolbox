# Project - lib-spring-boot-ai-os

## About

Provide a platform for LLM :

* Abstract OpenAI api using `OpenAiCompatibleChat` (can call [Ollama](https://ollama.com/))
* Register tool with Function calls

Generalisation an extracted from project [frtu/agents-os](https://github.com/frtu/agents-os)

## Configuration

```yaml
application:
  ai.os.llm:
    # https://platform.openai.com/docs/models/continuous-model-upgrades
    model: ${AI_OS_MODEL:mistral} # To use OpenAI model 'gpt-xx' please configure 'api-key'
    base-url: ${AI_OS_LOCAL_BASE_URL:http://localhost:11434/v1/}
    api-key: ${AI_OS_OPENAI_API_KEY:sk-xxx}
```

## Import

Import using Maven :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-spring-boot-ai-os</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-spring-boot-ai-os:${Versions.frtu_libs}")
```

or TOML

```
spring-boot-llm-os = { group = "com.github.frtu.libs", name = "lib-spring-boot-ai-os", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-spring-boot-ai-os.svg?label=latest%20release%20:%20lib-spring-boot-ai-os"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Compatibilities

### Spring Boot 2.x

Spring Boot 2.x requires Properties class to import version specific
annotation `org.springframework.boot.context.properties.ConstructorBinding` that is different from Spring Boot 3.x.

In order to enable properties loads from Spring Boot 2, create your own class that extends from `ChatApiProperties` :

```kotlin
import com.github.frtu.kotlin.ai.spring.config.ChatApiProperties
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
  - com.github.frtu.kotlin.ai.spring.config.LlmOsAutoConfigs
```

## Release notes

### 2.0.8

#### Tool framework

* Split `Tool` into `Tool` interface & `ToolExecuter`
* Adding structure & generic to `Tool` with `StructuredToolExecuter`
* Adding `ToolRegistry` aggregating all `Tool` from the classpath & add `split` to create specific tools by agent
* Replace `FunctionRegistry` with `ToolRegistry` for `OpenAiCompatibleChat` registration and during execution

##### Agent

* Adding `AgentExecutor` providing ability to execute `Tool`

##### Function

* Refactor `Function` as an abstract class allowing to customize execution
* Adding structure & generic to `Function`

### 2.0.6 - OLD NAME - lib-spring-boot-llm-os

* Enable to override and extends configs & properties for Spring Boot 2.x

#### Tool framework

* Adding `Tool` & `Executable` as function & agent abstraction

##### Agent

* Adding `AbstractAgent` implementing tools & manage stateful conversation if needed
* Adding `UnstructuredBaseAgent` providing ability to call simple Q&A with Text format

##### Function

* Refactor `Function` into dedicate package `com.github.frtu.kotlin.ai.os.tool.function`

### 2.0.5

* Bump to lib aallam-openai -> `3.8.0`

### 2.0.4

* Initial version from [github.com/frtu/agents-os](https://github.com/frtu/agents-os)
