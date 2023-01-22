# Project - test-containers - Temporalite

## About

Test containers for [Temporalite](https://github.com/temporalio/temporalite)

Can also use docker compose from https://github.com/frtu/vm/tree/master/docker/wkf-temporalite

## Forewords

Check [temporalite repo](https://github.com/temporalio/temporalite) for [latest version](https://github.com/temporalio/temporalite/releases).

Repo [slamdev/temporalite](https://github.com/slamdev/temporalite-container-image) is having the latest 0.3.0 as of now (jan 1st 2023).

You can use the already [built image](https://hub.docker.com/r/slamdev/temporalite/tags) or uncomment the docker-compose build to the latest version yourself.

## Usage

Make sure Docker is started before running tests !

After starting test, you will be able to have :

* temporal : port ```7233``` namespace ```default```
* admin UI : port ```8233``` at [http://localhost:8233/](http://localhost:8233/)

### Connection URLs

Connect to postgres using port ```7233```