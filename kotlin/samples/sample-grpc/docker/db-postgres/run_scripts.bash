# https://hub.docker.com/_/postgres?tab=tags&page=1&ordering=last_updated

IMAGE_NAME=postgres:12.6
CONTAINER_NAME=postgres-base
BASH_CMD=bash

postgresinit() {
    mkdir -p db/data db/init
}

echo "Type 'postgresstart' to create data folder and start ${CONTAINER_NAME}"
postgresstart() {
    echo "docker run --rm --name ${CONTAINER_NAME} -p 5432:5432 -v $PWD/data:/var/lib/postgresql/data -d ${IMAGE_NAME}"
    docker run --rm --name ${CONTAINER_NAME} \
        -p 5432:5432 \
        -v $PWD/db/data:/var/lib/postgresql/data -d\
        ${IMAGE_NAME}
    
    echo "docker logs ${CONTAINER_NAME} --follow"
    docker logs ${CONTAINER_NAME} --follow
}
echo "Type 'postgresstop' to stop ${CONTAINER_NAME}"
postgresstop() {
    echo "docker stop ${CONTAINER_NAME}"
    docker stop ${CONTAINER_NAME}
}
echo "Type 'postgresrm' to delete ${CONTAINER_NAME}"
postgresrm() {
    echo "docker rm ${CONTAINER_NAME}"
    docker rm ${CONTAINER_NAME}

    rm -Rf docker/db/data/
}

echo "Type 'postgresbash' to open a ${BASH_CMD} ${CONTAINER_NAME}"
postgresbash() {
    echo "docker exec -it ${CONTAINER_NAME} ${BASH_CMD} -l"
    docker exec -it ${CONTAINER_NAME} ${BASH_CMD} -l
}

echo "Type 'postgresdbcreate' to create a DB using parameters : postgresdbcreate APP_DB_NAME [TARGET_CONTAINER_NAME]"
postgresdbcreate() {
    local APP_DB_NAME=$1
    local TARGET_CONTAINER_NAME=${2:-$CONTAINER_NAME}

    if [[ -z $APP_DB_NAME ]]; then 
        echo "Usage : postgresdbcreate [APP_DB_NAME] " >&2
        return 1
    fi    
    echo "docker exec ${TARGET_CONTAINER_NAME} psql -U root -c \"CREATE DATABASE ${APP_DB_NAME}\""
    docker exec ${TARGET_CONTAINER_NAME} psql -U root -c "CREATE DATABASE ${APP_DB_NAME}"
}

postgresinit
postgresstart