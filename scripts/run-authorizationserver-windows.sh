#!/bin/sh
name='gemeente-auth'
if [ $(docker ps -a -f "name=$name" --format '{{.Names}}') == $name ]; then
    docker container stop gemeente-auth
    docker container rm gemeente-auth
fi

docker run --name gemeente-auth \
    --link dev-mysql:db \
    -e VIRTUAL_HOST=gemeente-auth \
    -v "C:\Environment\gemeente-authorizationserver\config":/config \
    -v "C:\Environment\gemeente-authorizationserver\external-data":/external-data \
    -d gemeente/authorizationserver