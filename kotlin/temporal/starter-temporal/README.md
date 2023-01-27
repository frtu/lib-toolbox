# Project - spring-boot / starter-temporal

## About

Provide spring boot starter to bootstrap connection to Temporal. 

## Usage

In your activity add `@ActivityImplementation` to register your **spring bean** into your `Worker`. 
 
```kotlin
@ActivityImplementation(taskQueue = "TASK_QUEUE_XXX")
```

In your workflow add `@WorkflowImplementation` to register this **class** into your `Worker`. 
 
```kotlin
@WorkflowImplementation(taskQueue = "TASK_QUEUE_XXX")
```

Configure any properties in your application.yml :

```yaml
temporal.stub:
  identity: ${application.name}
  namespace: "default"
  target: "localhost:7233"
  enableHttps: false

  enableKeepAlive: false
  keepAliveTime: "PT5S"
  keepAliveTimeout: "PT5S"
  keepAlivePermitWithoutStream: false
  connectionBackoffResetFrequency: "PT5S"
  grpcReconnectFrequency: "PT5S"
  rpcQueryTimeout: "PT5S"
  rpcTimeout: "PT5S"
  rpcLongPollTimeout: "PT5S"
```

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

## See also

* https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-api

## Release notes

### 1.2.4 - Current version

* Adding [ActivityAspect using AOP](#124)
* Bump temporal.version -> 1.17.0 <!-- 2022-10-26 -->

### 1.2.3

* Allow to register workflow & activity : `@WorkflowImplementation` `@ActivityImplementation`
* Finalize `TemporalStubProperties` & `TemporalConfig` : Allows configuring all kinds of timeout
* `samples/sample-workflow-temporal` : demonstrate `com.github.frtu.libs:starter-temporal`
* Adding DSL `serverlessworkflow`

### 1.2.2

* `ObservabilityJaegerConfig` & `ObservabilityMetricsConfig` : Allows configuring Observability 
* Bump `temporal.version` [1.13.0]

### 1.2.1

* Bootstrap Temporal server stubs
* Add interceptors for Jaeger

### 1.2.4

* Adding [AOP](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-api) around Activity
