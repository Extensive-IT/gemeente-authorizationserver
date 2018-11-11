#!/usr/bin/env bash
name='dev-mysql'
[[ $(docker ps -f "name=$name" --format '{{.Names}}') == $name ]] ||
docker run --name dev-mysql -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 -d mysql:8.0.3

name='dev-phpmyadmin'
[[ $(docker ps -f "name=$name" --format '{{.Names}}') == $name ]] ||
docker run --name dev-phpmyadmin -d --link dev-mysql:db -p 8082:80 -d phpmyadmin/phpmyadmin

cat sql/createUsersDbAndUser.sql | docker exec -i dev-mysql /usr/bin/mysql -u root --password=password mysql && \
cat sql/createTables.sql | docker exec -i dev-mysql /usr/bin/mysql -u 'gemeente-users' --password='gemeente-users' 'gemeente-users'
cat sql/fillInitialUsers.sql | docker exec -i dev-mysql /usr/bin/mysql -u 'gemeente-users' --password='gemeente-users' 'gemeente-users'