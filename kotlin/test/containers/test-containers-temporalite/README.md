# Project - test-containers-temporalite

## About

Test containers for [Temporalite](https://github.com/temporalio/temporalite)

Can also use docker compose from https://github.com/frtu/vm/tree/master/docker/wkf-temporalite

## Forewords

Check [temporalite repo](https://github.com/temporalio/temporalite) for [latest version](https://github.com/temporalio/temporalite/releases).

Repo [slamdev/temporalite](https://github.com/slamdev/temporalite-container-image) is having the latest `0.10.7`

You can use the already [built image](https://hub.docker.com/r/slamdev/temporalite/tags) or uncomment the docker-compose build to the latest version yourself.

## Usage

Make sure Docker is started before running tests !

After starting test, you will be able to have internally :

* temporal : port ```7233``` namespace ```default```
* admin UI : port ```8233``` at [http://localhost:8233/](http://localhost:8233/)

### Connection URLs

Create a `WorkflowClient` using :

```kotlin
internal class XxxTest: TemporaliteContainer(endpoint = "localhost:7233") {
    private lateinit var client: WorkflowClient

    @Test
    fun test() {
        ...
    }
    
    @BeforeAll
    fun setup() {
        start()
        client = buildWorkflowClient()
    }

    @AfterAll
    fun destroy() {
        stop()
    }
}
```

Or use it as an utility :

```kotlin
val temporaliteContainer = TemporaliteContainer()
temporaliteContainer.start()
logger.debug("Started Temporal at port ${temporaliteContainer.mappedPortTemporal}")
val client = temporaliteContainer.buildWorkflowClient()
    ...
temporaliteContainer.stop()
```

### To transform your test to connect to remote Temporal

By configuring `endpoint`, it allows to test using remote Temporal endpoint :

```kotlin
val temporaliteContainer = TemporaliteContainer(endpoint = "localhost:7233")
```

## Release notes

### 2.0.8

* Reactivate `test-containers/temporalite`
* Adding ability to add `endpoint` & connect to remote Temporal for manual testing
