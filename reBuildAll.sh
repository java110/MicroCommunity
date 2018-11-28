#!/usr/bin/env bash

git pull origin master

mvn clean install
mvn package;

docker-compose -f ./Api/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./CenterService/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./ShopService/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./StoreService/docker/docker-compose.yml       up -d --force-recreate;
docker-compose -f ./UserService/docker/docker-compose.yml       up -d --force-recreate;

