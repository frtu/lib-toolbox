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


## Release notes

### 2.0.5 - Current version

* Bump to lib aallam-openai -> `3.8.0`

### 2.0.4

* Initial version from [github.com/frtu/agents-os](https://github.com/frtu/agents-os)
