# gemeente-authorizationserver
An OAuth2 Based AuthorizationServer for use within churches

## Deployment
```
docker run --name proxy -d -p 80:80 -v //var/run/docker.sock:/tmp/docker.sock:ro jwilder/nginx-proxy
docker run --name gemeente-auth \
    --link dev-mysql:db \
    -e VIRTUAL_HOST=gemeente-auth \
    -d gemeente/authorizationserver
```