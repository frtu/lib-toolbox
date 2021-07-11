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

### H2

application.yml :

```yaml
application:
  persistence:
    url: r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
```

Spring Boot application :

```kotlin
@Import(H2R2dbcConfiguration::class)
@SpringBootApplication
class Application {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().also { initializer ->
            initializer.setConnectionFactory(connectionFactory)
            initializer.setDatabasePopulator(
                CompositeDatabasePopulator().also { populator ->
                    // Add all populators
                    populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("db/migration/V0_1_1__table-email-json.sql")))
                }
            )
        }
```

### PostgresQL

application.yml :

```yaml
application:
  persistence:
    host: localhost
    port: 5432
    database: sample
    username: user_admin
    password: pass
    url: r2dbc:postgresql://${application.persistence.host}:${application.persistence.port}/${application.persistence.database}

```

```kotlin
@Import(PostgresR2dbcConfiguration::class)
class Application {
```

Or if you also include JSONB types :

```kotlin
@Import(PostgresJsonR2dbcConfiguration::class)
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
