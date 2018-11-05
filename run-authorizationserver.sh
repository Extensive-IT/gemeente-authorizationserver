#!/bin/sh
docker run --name gemeente-auth \
    -e VIRTUAL_HOST=gemeente-auth \
    -d gemeente/authorizationserver