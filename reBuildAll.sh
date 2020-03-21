#!/usr/bin/env bash

git pull origin master

mvn clean install -Dmaven.test.skip=true
mvn package -Dmaven.test.skip=true

#docker-compose -f ./eureka/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./Api/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./OrderService/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./CommunityService/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./StoreService/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./UserService/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./FrontService/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./FeeService/docker/docker-compose.yml       up -d --build --force-recreate;
docker-compose -f ./CommonService/docker/docker-compose.yml       up -d --build --force-recreate;
