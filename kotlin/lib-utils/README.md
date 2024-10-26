# Project - lib-utils

## About

Mini lib that provides common useful method to facilitate testing :

* Loading resources
* Json utilities
* Predicates for Stream

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

```
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
