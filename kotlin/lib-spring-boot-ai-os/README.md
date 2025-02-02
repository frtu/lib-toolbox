# Project - lib-spring-boot-ai-os

## About

Provide a platform for LLM :

* Abstract OpenAI api using `OpenAiCompatibleChat` (can call [Ollama](https://ollama.com/))
* Register tool with Function calls

Generalisation an extracted from project [frtu/agents-os](https://github.com/frtu/agents-os)

## Prompt

To use [mustache](https://www.baeldung.com/mustache) for your instructions, use `PromptTemplate` class to `#format(inputParamsForRendering)` :

```kotlin
override val instructions: String
    get() = PromptTemplate(
        template = """
        {{#intents}}
        * {{id}} -> {{description}}
        {{/intents}}
    """.trimIndent(),
    ).format(mapOf("intents" to intents))
```

## Creating Agents

Extendable structure for agents

* `StructuredBaseAgent` : Agent returning structured response (Json & object)

Create your own agent

* `UnstructuredBaseAgent` : Versatile agent taking String as input & output. When you want full control on how to
  format.
* `AgentExecuter` : Advanced Agent implementing tool & function calls
* `AbstractAgent` : Base structure for Agent calling LLM

### Create Agent from PromptTemplate

Create an Agent using `Chat` & `PromptTemplate` :

```kotlin
UnstructuredBaseAgent.create(chat, prompt).also { agent ->
    logger.info("Created new agent id:${agent.id} from prompt:{}", prompt)
}
```

WIP : Allow to automatically create Agent from Prompt using spring boot config : `AgentFromPromptConfig`

### Best practices

* Always double-check your response inside your agent before returning result (UPPER or lower case, trim space, ...).
* Check the format & raise proper errors after calling LLM
* Always try to **shift left** by suggesting good format during instructions passing

## Configuration

```yaml
application:
  ai.os.llm:
    # https://platform.openai.com/docs/models/continuous-model-upgrades
    model: ${AI_OS_MODEL:mistral} # To use OpenAI model 'gpt-xx' please configure 'api-key'
    base-url: ${AI_OS_LOCAL_BASE_URL:http://localhost:11434/v1/}
    api-key: ${AI_OS_OPENAI_API_KEY:sk-xxx}
```

### Feature Toggles

By default, feature is enabled. To disable, set `enabled: false`

```yaml
application:
  ai.os.llm:
    enabled: false
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
spring-boot-ai-os = { group = "com.github.frtu.libs", name = "lib-spring-boot-ai-os", version.ref = "frtu-libs" }
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

### 2.0.15

*  Allow to list all variables from a mustache template

### 2.0.14

* Adding `PromptTemplate` to render prompt using mustache
* Allow to create `UnstructuredBaseAgent.create()` from `PromptTemplate`

### 2.0.13

#### Feature

* Adding `IntentClassifierAgent` to classify `Intent` using LLM

#### Request

* Enable `instructions` override
* Logging instruction when starting Agent

#### Structure output & categorisation

* Adding `StructuredBaseAgent` to return structured JSON object
* Adding `category` & `subCategory`

### 2.0.11

* Adding [Feature Toggles](#feature-toggles)

### 2.0.9

* Allow to better @ComponentScan using Marker interface

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

* Refactor `Function` into dedicate package `com.github.frtu.kotlin.tool.function`

### 2.0.5

* Bump to lib aallam-openai -> `3.8.0`

### 2.0.4

* Initial version from [github.com/frtu/agents-os](https://github.com/frtu/agents-os)
