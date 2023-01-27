# Project - sample-cloudevents

## About

Project demonstrating https://github.com/cncf/wg-serverless 

### CloudEvents

Using cloudevents with HTTP or Kafka.

For gRPC sample defined by Google, see :
 
* https://cloud.google.com/eventarc/docs/workflows/cloudevents
* https://cloud.google.com/functions/docs/writing/write-event-driven-functions
* https://github.com/googleapis/google-cloudevents

### Serverless Workflow

* https://github.com/cncf/wg-serverless/tree/master/whitepapers/serverless-overview

## API

### Async with WebFlux

* Access Swagger API at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Integration with Docker

Build your Docker image at ```mvn verify``` phase

Startup your application using :

```
docker run -d --name <instance-name> -p 8080:8080 -P <your-archetype-id>
```

* -d : startup as a daemon
* --name : give it a name

## Release notes

### 0.0.1-SNAPSHOT - Current version

* Feature list