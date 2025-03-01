# Project - lib-spring-boot-tools

## About

Provide Spring boot facilitator around Tool.

* Auto expose Tool as HTTP endpoints
* Provide @Annotation for Spring Bean

## Features

### Auto expose Tool endpoints

Adding `spring-boot-starter-webflux` will enable Tool endpoints.

* `application.tools.endpoint.enabled` allow to enable `true` (default) or `false`
* `application.tools.endpoint.deployment-mode` configure endpoint `STATIC` (default) `DYNAMIC`

```yaml
application:
  tools:
    endpoint:
      enabled: true
      deployment-mode: STATIC
      url-prefix: "/v1/tools"
```

Check that your endpoint is really exposed by looking for logs :

```
Create one static endpoint path:/v1/tools/{tool-name}
OR
Create one generic endpoint path:/v1/tools/{tool-name}
```

If your endpoint is not created, check if your tool has been scanned by looking for log :

```
Build tools size:{size} names:[...]
```

### Create Tool from annotated bean

* `application.tools.scan.enabled` allow to enable `true` (default) or `false`

```yaml
application:
  tools:
    scan.enabled: true
```

Automatically create `Tool` from annotated method :

```kotlin
@ToolGroup
class AnnotatedClass {
    @Tool(
        id = "tool-name",
        description = "Tool description",
    )
    fun anyName(request: X): Y {
        ...
    }
}
```

All your `@Tool` annotated methods will be encapsulated into a `Tool` bean

### Logs level

* You can choose to Log with higher level by setting `logLevel: INFO` (`DEBUG` by default)

```yaml
application:
  tools.endpoint:
    log-level: INFO
```

## Import

Import using Maven :

```XML

<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-spring-boot-tools</artifactId>
    <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-spring-boot-tools:${Versions.frtu_libs}")
```

or TOML

```
lib-spring-boot-tools = { group = "com.github.frtu.libs", name = "lib-spring-boot-tools", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-spring-boot-tools.svg?label=latest%20release%20:%20lib-spring-boot-tools"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.16

* Document and facilitate log verbosity for router using different config profile

### 2.0.9

* Allow `ToolAnnotationUtils` to build `StructuredTool<INPUT, OUTPUT>` from `java.lang.reflect.Method`
* Allow `ToolBuilderFromAnnotationScanner` to build Tool from `@Tool` annotated method on `@ToolGroup` Bean
