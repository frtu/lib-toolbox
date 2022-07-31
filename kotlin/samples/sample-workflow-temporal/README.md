# Project - sample-workflow-temporal

## About

Sample Temporal using spring-boot

This is using [serverlessworkflow](https://serverlessworkflow.github.io/) as a DSL specification. 
* It is under the [CNCF umbrella](https://www.cncf.io/projects/serverless-workflow/) along with [other projects](https://landscape.cncf.io/card-mode?category=app-definition-and-development&grouping=category)
* Has [Java SDK](https://github.com/serverlessworkflow/sdk-java) for parsing & build workflow definitions
* Has [Visual editor](https://serverlessworkflow.github.io/editor.html)

## Modules

### HTTP

#### Workflow

* POST http://localhost:8080/v1/workflows
```json
{
  "receiver": "rndfred@gmail.com",
  "subject": "Mail subject",
  "content": "Lorem ipsum dolor sit amet.",
  "status": "SENT"
}
```

#### Simple CRUD

* GET http://localhost:8080/v1/emails
* POST http://localhost:8080/v1/emails
```json
{
  "receiver": "rndfred@gmail.com",
  "subject": "Mail subject",
  "content": "Lorem ipsum dolor sit amet.",
  "status": "SENT"
}
```

### gRPC

* gRPC server-client implementation : ```com.github.frtu.sample.rpc.grpc```
* gRPC Error handling : TODO

gRPC URL : `0.0.0.0:9090`

```json
{
  "data": {
    "receiver": "rndfred@gmail.com",
    "subject": "Mail subject",
    "content": "Lorem ipsum dolor sit amet."
  },
  "status": "INIT"
}
```

### R2DBC

* R2DBC Coroutine repository : ```com.github.frtu.sample.persistence```

## Metrics

* [Temporal metrics](http://localhost:8000/metrics) at `http://localhost:8000/metrics`
* [Application metrics](http://localhost:8080/actuator/prometheus) at `http://localhost:8080/actuator/prometheus`

## See also

* [gRPC Kotlin startup](https://grpc.io/docs/languages/kotlin/)