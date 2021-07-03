# Project - lib-r2dbc

## About

Persistence for coroutine using suspend functions. R2DBC will be used to achieve this goal.

## Import

Import using :

```XML
<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-r2dbc</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

Check the latest version (clickable) :

[<img src="https://img.shields.io/maven-central/v/com.github.frtu.libs/lib-r2dbc.svg?label=latest%20release%20:%20lib-r2dbc"/>](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lib-r2dbc%22+g%3A%22com.github.frtu.libs%22)

## Init spring-boot

For PostgresQL :

```kotlin
@Import(PostgresR2dbcConfiguration::class)
class Application {
```

Or if you also include JSONB types :

```kotlin
@Import(PostgresJsonR2dbcConfiguration::class)
class Application {
```

For H2 :

```kotlin
@Import(
    H2R2dbcConfiguration::class, // Initialize H2 DB
    EntityConfiguration::class // Create Repositories
)
@SpringBootApplication
class Application {
```

## Starting up

Extend Spring Data interface : ```CoroutineCrudRepository```

For
ex [IEmailRepository](https://github.com/frtu/sample-code/blob/master/event-notification-mails/event-notification-web-coroutine-flow/src/main/kotlin/com/github/frtu/coroutine/persistence/IEmailRepository.kt):

```
@Repository
interface IEmailRepository : CoroutineCrudRepository<Email, UUID> {
...
}
```

Also import class from https://github.com/frtu/sample-code/tree/master/persistence-r2dbc/src/main/kotlin/com/github/frtu/persistence/r2dbc/query


## Release notes

### 1.1.1

* spring data R2DBC for coroutine
