#!/usr/bin/env bash

git pull origin master

mvn clean install
mvn package;

docker-compose -f ./app/Api/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./app/CenterService/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./app/ShopService/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./app/StoreService/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./app/UserService/docker/docker-compose.yml       up -d --force-recreate;

