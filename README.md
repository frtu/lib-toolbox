# lib-toolbox
Library toolbox to provide specific capabilities

## Kotlin libs

All libraries for [kotlin](kotlin)

### Middleware & Spring Boot bootstrap

* [lib-spring-boot-slack](kotlin/lib-spring-boot-slack) : Allow to start Slack connectivity along with spring boot lifecycle
* [lib-spring-boot-ai-os](kotlin/lib-spring-boot-ai-os) : Allow to bootstrap agent framework with OpenAI or local Ollama
* [lib-durable-agent-framework](kotlin/workflows/lib-durable-agent-framework) : Allow to bootstrap durable agent
  framework by using Temporal as execution storage
* [lib-action](kotlin/lib-action) : Base project for normalise action calls across projects
* [workflows](kotlin/workflows) : Temporal & Serverless workflow
* [lib-workflow](kotlin/workflows/lib-workflow) : Lightweight workflow for `Tool`

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

### 2.0.8

* Adding [lib-action](kotlin/lib-action)
* Renaming [lib-spring-boot-ai-os](kotlin/lib-spring-boot-ai-os) to clearly separate what is AI and what is LLM
* Reactivate [lib-workflow](kotlin/workflows/lib-workflow)
* Reactivate [serverless-generator](kotlin/workflows/serverless-generator)
* Reactivate [temporalite](kotlin/test-containers/temporalite)

### 2.0.4

* Adding [lib-serdes-json](kotlin/lib-serdes-json)
* Adding [lib-spring-boot-ai-os](kotlin/lib-spring-boot-ai-os)

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
