# Project - spring-boot / starter-temporal

## About

Provide spring boot starter to bootstrap connection to Temporal 

## Import

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>starter-temporal</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

or

```
implementation("com.github.frtu.libs:starter-temporal:${Versions.frtu_libs}")
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/starter-temporal.svg?label=latest%20release%20:%20starter-temporal"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22starter-temporal%22+g%3A%22com.github.frtu.libs%22)

## Init spring-boot

### Code

Spring Boot application :

```kotlin
@Import(TemporalConfig::class)
@SpringBootApplication
class Application
```

## Release notes

### 1.2.1

* spring data R2DBC for coroutine
