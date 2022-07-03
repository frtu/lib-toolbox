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
    <opentelemetry.version>1.15.0</opentelemetry.version>
</properties>

<dependencies>
    <!-- Tracing -->
    <dependency>
        <groupId>io.temporal</groupId>
        <artifactId>temporal-opentracing</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-opentracing-shim</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-sdk</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-extension-trace-propagators</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-semconv</artifactId>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-exporter-jaeger</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jaegertracing</groupId>
        <artifactId>jaeger-client</artifactId>
        <version>${jaeger.version}</version>
    </dependency>
...

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.opentelemetry</groupId>
                <artifactId>opentelemetry-bom</artifactId>
                <version>${opentelemetry.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>io.opentelemetry</groupId>
                <artifactId>opentelemetry-bom-alpha</artifactId>
                <version>${opentelemetry.version}-alpha</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>            
<dependencies>
```

## Release notes

### 1.2.1

* Bootstrap Temporal server stubs
* Add interceptors for Jaeger
