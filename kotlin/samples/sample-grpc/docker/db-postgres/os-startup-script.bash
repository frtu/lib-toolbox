#!/bin/bash

# https://blog.crunchydata.com/blog/easy-postgresql-12-and-pgadmin-4-setup-with-docker
# https://blog.crunchydata.com/blog/three-easy-things-to-remember-about-postgres-indexes

mkdir postgres
cd postgres

docker volume create --driver local --name=pgvolume
docker volume create --driver local --name=pga4volume

docker network create --driver bridge pgnetwork

cat << EOF > pg-env.list
PG_MODE=primary
PG_PRIMARY_USER=postgres
PG_PRIMARY_PASSWORD=datalake
PG_DATABASE=hippo
PG_USER=hippo
PG_PASSWORD=datalake
PG_ROOT_PASSWORD=datalake
PG_PRIMARY_PORT=5432
EOF

cat << EOF > pgadmin-env.list
PGADMIN_SETUP_EMAIL=hippo
PGADMIN_SETUP_PASSWORD=datalake
SERVER_PORT=5050
EOF

docker run --publish 5432:5432 \
  --volume=postgres:/pgdata \
  --env-file=pg-env.list \
  --name="postgres" \
  --hostname="postgres" \
  --network="pgnetwork" \
  --detach \
registry.developers.crunchydata.com/crunchydata/crunchy-postgres:centos7-12.2-4.2.2

docker run --publish 5050:5050 \
  --volume=pgadmin4:/var/lib/pgadmin \
  --env-file=pgadmin-env.list \
  --name="pgadmin4" \
  --hostname="pgadmin4" \
  --network="pgnetwork" \
  --detach \
registry.developers.crunchydata.com/crunchydata/crunchy-pgadmin4:centos7-12.2-4.2.2