# Project - lib-workflow

## About

Allow to materialize a business flow

## Tool

Implement a `ToolFlow<INPUT, OUTPUT>` by extending : 

```kotlin
class SampleToolFlow : ToolFlow<Event, String>(
    name = "Flow name",
    description = "Flow to be executed as Tool",
    parameterClass = Event::class.java,
    returnClass = String::class.java,
) {
    ...
}
```

### JAR

To use this project for `Tool` please import _optional_ packages :

```yaml
<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-serdes-json</artifactId>
    <version>${project.version}</version>
</dependency>
<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-spring-boot-llm-os</artifactId>
    <version>${project.version}</version>
</dependency>
```

## Release notes

### 2.0.7

* Reactivate `lib-workflow`
* Adding `ToolFlow` to represent a workflow as `Tool`

### 1.1.4

* Basic structure
