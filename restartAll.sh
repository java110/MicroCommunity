#!/usr/bin/env bash

docker-compose -f ./Api/docker/docker-compose.yml        restart;
docker-compose -f ./OrderService/docker/docker-compose.yml      restart;
docker-compose -f ./ShopService/docker/docker-compose.yml       restart;
docker-compose -f ./StoreService/docker/docker-compose.yml       restart;
docker-compose -f ./UserService/docker/docker-compose.yml       restart;