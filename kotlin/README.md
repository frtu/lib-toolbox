# Project - kotlin

## About

All libraries for Kotlin.

Middleware & Spring Boot bootstrap :

* [lib-action](lib-action) : Base project for normalise action calls across projects
* [lib-action-tools](lib-action-tools) : Specialisation of Action framework dedicated to Tools & Functions
* [lib-spring-boot-slack](lib-spring-boot-slack) : Allow to start Slack connectivity along with spring boot lifecycle
* [lib-spring-boot-ai-os](lib-spring-boot-ai-os) : Allow to bootstrap agent framework with OpenAI or local Ollama
* [lib-durable-tool-execution](workflows/lib-durable-tool-execution) : Allow to bootstrap durable agent
  framework by using Temporal as execution storage 
* [workflows](workflows) : Temporal & Serverless workflow

Tests :

* [lib-utils](lib-utils) : bean utils for class deserialization & Java -> Kotlin smart cast
* [test-containers](test-containers) : facilitators to integrate container bootstrap to unit tests

Persistence & communication protocol :

* [lib-r2dbc](lib-r2dbc) : spring data R2DBC bootstrap for coroutine
* [lib-webclient](lib-webclient) : spring webflux webclient for coroutine
* [lib-grpc](lib-grpc) : Interceptors for gRPC
* [lib-serdes-protobuf](lib-serdes-protobuf) : protobuf utilities & metadata readers
