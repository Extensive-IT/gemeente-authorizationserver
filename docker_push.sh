#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build -t extensiveit/gemeente-authorizationserver:latest .
docker push extensiveit/gemeente-authorizationserver:latest