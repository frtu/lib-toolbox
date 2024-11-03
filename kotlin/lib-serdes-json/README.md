# Project - lib-serdes-json

## About

Provide all the lib to manipulate JSON :

* Enable Json Schema generation using `SchemaGen`
* Enable to manipulate object with `JsonNode` & `String` using JsonExtension

## Usage

### Json schema

Generate Json schema using `SchemaGen.generateJsonSchema(parameterClass): String`

### Json manipulation

#### Using JsonNode as weakly typed pivot

Turn `JsonNode` from/to `Any` using :

```kotlin
fun Any.objToJsonNode(): JsonNode
fun <T> JsonNode.toJsonObj(clazz: Class<T>): T
```

#### Using String as weakly typed pivot

Turn `String` from/to `Any` using :

```kotlin
fun Any.toJsonString(pretty: Boolean = false): String
fun <T> String.toJsonObj(clazz: Class<T>): T
```

#### Conversion between weakly typed

Turn `JsonNode` from/to `String` using :

```kotlin
fun JsonNode.toJsonString(pretty: Boolean = false): String
fun String.toJsonNode(): JsonNode
```

## Import

Import using Maven :

```XML

<dependency>
    <groupId>com.github.frtu.libs</groupId>
    <artifactId>lib-serdes-json</artifactId>
    <version>${frtu-libs.version}</version>
</dependency>
```

or gradle

```
implementation("com.github.frtu.libs:lib-serdes-json:${Versions.frtu_libs}")
```

or TOML

```
serdes-json = { group = "com.github.frtu.libs", name = "lib-serdes-json", version.ref = "frtu-libs" }
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-serdes-json.svg?label=latest%20release%20:%20lib-serdes-json"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)

## Release notes

### 2.0.8

* Adding bidirectional conversion between `Any` to weakly typed (`JsonNode` or `String`)

### 2.0.4

* Initial version from [github.com/frtu/agents-os](https://github.com/frtu/agents-os)
