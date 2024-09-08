# lib-toolbox
Library toolbox to provide specific capabilities

## Java libs

All libraries for [java](java)

* [lib-dot](java/lib-dot) : lib to generate dot files
* [lib-mail](java/lib-mail) : lib to send email using SMTP

## Kotlin libs

All libraries for [kotlin](kotlin)

* [lib-r2dbc](kotlin/lib-r2dbc) : spring data R2DBC for coroutine
* [lib-webclient](kotlin/lib-webclient) : spring webflux webclient for coroutine
* [lib-utils](kotlin/lib-utils) : bean utils for class deserialization & Java -> Kotlin smart cast

## Release notes

### 2.0.3 - Current version

* Update [lib-kotlin-bom](kotlin%2Flib-kotlin-bom) with latest libs
* Deprecate [temporal-bom](kotlin%2Fworkflows%2Ftemporal-bom) in favor of `io.temporal:temporal-bom`

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
