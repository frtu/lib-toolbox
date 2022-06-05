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

### Observability

To enable jaeger :

```yaml
jaeger:
  enabled: true
  endpoint: "http://localhost:14250"
```

```xml
<properties>
    <jaeger.version>1.8.0</jaeger.version>
    <opentelemetry.version>1.14.0</opentelemetry.version>
</properties>

<dependencies>
    <!-- Tracing -->
    <dependency>
        <groupId>io.temporal</groupId>
        <artifactId>temporal-opentracing</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-sdk</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-extension-trace-propagators</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-semconv</artifactId>
        <version>${opentelemetry.version}-alpha</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-opentracing-shim</artifactId>
        <version>${opentelemetry.version}-alpha</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-exporter-jaeger</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jaegertracing</groupId>
        <artifactId>jaeger-client</artifactId>
        <version>${jaeger.version}</version>
    </dependency>
...
<dependencies>
```

## Release notes

### 1.2.1

* Bootstrap Temporal server stubs
* Add interceptors for Jaeger
