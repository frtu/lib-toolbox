# Project - test-ai-os-agents

## About

Provide test suite for LLM


## Usage

* Extend `AbstractLocalAgentTest<INPUT, OUTPUT>` that accept an agent constructor `StructuredBaseAgent<INPUT, OUTPUT>`
  and a list of Ollama model name it will support.
* Create multiple test function that use `benchmarkAcrossModel` with an INPUT and a validator that takes the `output`
  and the model name that run.
* Different `model` many have different result, feel free to difference case based on `when(model)`. 

```kotlin
class XxxAgentTest : AbstractLocalAgentTest<INPUT, OUTPUT>(
        {
            XxxAgent(
                chat = it,
                ...
            )
        },
        "llama3",
    ) {

    @Test
    fun `Test description`() = benchmarkAcrossModel(
        "added an integration wto this channel.",
    ) { model, output ->
        // Can create different condition check depending on model
        with(output) {
            // LLM may mix upper & lower case
            intent.lowercase() shouldBe "Other".lowercase()
            reasoning.shouldNotBeEmpty()
        }
    }
}
```

## Import

Import using Maven :

```XML
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>test-ai-os-agents</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

or gradle

```
testImplementation("com.github.frtu.libs:test-ai-os-agents:${Versions.frtu_libs}")
```

or TOML

```
test-ai-os-agents = { group = "com.github.frtu.libs", name = "test-ai-os-agents", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/test-ai-os-agents.svg?label=latest%20release%20:%20test-ai-os-agents"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.13

* Initial version
