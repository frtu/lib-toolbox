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

## Release notes

### 2.0.12

* Add `#run(model: String = "phi3:mini")`

### 2.0.11

* Initial version
