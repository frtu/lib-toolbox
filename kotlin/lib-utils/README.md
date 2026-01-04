# Project - lib-utils

## About

Mini lib that provides common useful method to facilitate testing :

* Loading resources
* Json utilities
* Predicates for Stream

## ValueObject

Import and declare in `build.gradle.kts` :

```gradle
plugins {
    var kotlin = "1.9.25"

    // Core
    kotlin("jvm") version kotlin
    kotlin("plugin.noarg") version kotlin
}
   
noArg {
    // Apply this magic constructor generation to any class with this annotation
    annotation("com.github.frtu.kotlin.utils.data.ValueObject")
}
```

Annotate any classes with `@ValueObject` :

```kotlin
@ValueObject
data class EventInput(
    val eventId: String?,
)
```

To combine data classes with Jackson & Serialization :

```kotlin
@ValueObject
// Critical: Don't crash on new/unexpected fields
@JsonIgnoreProperties(ignoreUnknown = true)
data class RawInput(
    @JsonProperty("event_id")
    val eventId: String?,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
```

## Import

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-utils</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or

```gradle
implementation("com.github.frtu.libs:lib-utils:${Versions.frtu_libs}")
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-utils.svg?label=latest%20release%20:%20lib-utils"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-utils%22+g%3A%22com.github.frtu.libs%22)


## Release notes

### 1.1.5

* `Any.toJsonString()` : allow to print JSON from any object using Jackson
* Move AvroBeanHelper into dedicated module `lib-serdes-avro`

### 1.1.2

* Java -> Kotlin smart cast
* bean utils for class deserialization from JSON or Avro
