# Project - test-containers-ollama

## About

Test containers for [Ollama](https://ollama.com/)

## Usage

Run Ollama service using :

```kotlin
@Testcontainers
internal class IntentClassifierAgentTest : OllamaContainer(
    // == Allows to use local Ollama for faster test
//    endpoint = "http://localhost:11434/v1/"
) {
    private lateinit var api: Chat

    @BeforeAll
    fun setup() {
        start()
        val model = "phi3:mini"
        run(model = model)
//        execInContainer("ollama", "run", model)
    }
}
```

Coupled with [lib-spring-boot-ai-os](..%2F..%2Flib-spring-boot-ai-os), can initialize client `api: Chat`

```kotlin
@Testcontainers
internal class IntentClassifierAgentTest : OllamaContainer(
    // == Allows to use local Ollama for faster test
//    endpoint = "http://localhost:11434/v1/"
) {
    private lateinit var api: Chat

    @BeforeAll
    fun setup() {
        start()
        val model = "phi3:mini"
//        run(model = model)
        execInContainer("ollama", "run", model)
        api = ChatApiConfigs().chatOllama(
            model = model,
            baseUrl = "http://localhost:$mappedPortTemporal/v1/", // getBaseUrl()
        )
    }

    @Test
    fun `Test calling IntentClassifierAgent`(): Unit = runBlocking {
        api ...
    }
}
```

## Import

Import using Maven :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>test-containers-ollama</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:test-containers-ollama:${Versions.frtu_libs}")
```

or TOML

```
spring-boot-llm-os = { group = "com.github.frtu.libs", name = "test-containers-ollama", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/test-containers-ollama.svg?label=latest%20release%20:%20test-containers-ollama"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)


## Release notes

### 2.0.12

* Add `#run(model: String = "phi3:mini")`

### 2.0.11

* Initial version
