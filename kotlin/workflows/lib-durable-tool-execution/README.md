# Project - lib-durable-tool-execution

## About

Agent task may not be short living and take more than a few second to execute. This framework aims to enable long
running `Agent` execution by capturing Agent & Tool exchange through Temporal history.

* Starting Workflow method as a `Tool` (function)
* Starting a `Tool` encapsulated into an Activity
* Sending callbacks through `Slack`

## Import

Import using Maven :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-durable-tool-execution</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-durable-tool-execution:${Versions.frtu_libs}")
```

or TOML

```
spring-boot-durable-agent-framework = { group = "com.github.frtu.libs", name = "lib-durable-tool-execution", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-durable-tool-execution.svg?label=latest%20release%20:%20lib-durable-tool-execution"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.8

* Initial version
