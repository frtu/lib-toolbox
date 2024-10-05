# lib-toolbox
Library toolbox to provide specific capabilities

## Kotlin libs

All libraries for [kotlin](kotlin)

### Middleware & Spring Boot bootstrap

* [lib-spring-boot-slack](kotlin/lib-spring-boot-slack) : Allow to start Slack connectivity along with spring boot lifecycle
* [workflows](kotlin/workflows) : Temporal & Serverless workflow

### Tests

* [lib-utils](kotlin/lib-utils) : bean utils for class deserialization & Java -> Kotlin smart cast
* [test-containers](kotlin/test-containers) : facilitators to integrate container bootstrap to unit tests

### Persistence & communication protocol

* [lib-r2dbc](kotlin/lib-r2dbc) : spring data R2DBC bootstrap for coroutine
* [lib-webclient](kotlin/lib-webclient) : spring webflux webclient for coroutine
* [lib-grpc](kotlin/lib-grpc) : Interceptors for gRPC
* [lib-serdes-protobuf](kotlin/lib-serdes-protobuf) : protobuf utilities & metadata readers

## Java libs (paused at the moment)

All libraries for [java](java)

* [lib-dot](java/lib-dot) : lib to generate dot files
* [lib-mail](java/lib-mail) : lib to send email using SMTP

## Release notes

Check latest release notes at https://github.com/frtu/lib-toolbox/releases

### 2.0.4

* Adding [lib-serdes-json](kotlin%2Flib-serdes-json)
* Adding [lib-spring-boot-llm-os](kotlin%2Flib-spring-boot-llm-os)

#### lib-spring-boot-slack

* Adding ability to run long-running command (>3s) using `LongRunningSlashCommandHandler`

### 2.0.3

* Update [lib-kotlin-bom](kotlin%2Flib-kotlin-bom) with latest libs
* Deprecate [temporal-bom](kotlin%2Fworkflows%2Ftemporal-bom) in favor of `io.temporal:temporal-bom`
* Removed [lib-workflow](kotlin%2Fworkflows%2Flib-workflow)

### 2.0.2

#### Module

* Adding [lib-spring-boot-slack](kotlin%2Flib-spring-boot-slack)
* Pause release for all `java` libs
* Discontinue `temporalite`

#### Lib versions

* Bump kotlin.version -> 1.9.23
* Bump kotlinx.version -> 1.7.3

### 2.0.0

* Bump JDK -> 17
* Bump spring-boot.version -> 3.2.5
* Bump kotlin.version -> 1.7.22
* Bump kotlinx.version -> 1.7.2

### 1.2.4

* Adding [ActivityAspect using AOP](kotlin/spring-boot/starter-temporal#124)
* Adding [Testcontainers for Temporalite](kotlin/test-containers/temporalite)
* Bump temporal.version -> 1.17.0 <!-- 2022-10-26 -->

### 1.2.0

* Bump kotlin.version -> 1.5.32
* Bump kotlinx.version -> 1.5.2

### 1.1.4

* Bump frtu.logger.version ->1.1.4
