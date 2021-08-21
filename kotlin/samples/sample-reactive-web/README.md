# Project - sample-reactive-web

## About

Access API using :

* http://localhost:8080/v1/resources/1234

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