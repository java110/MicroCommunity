#!/usr/bin/env bash

cd ..

docker-compose -f ./service-api/docker/docker-compose.yml        restart;
docker-compose -f ./service-order/docker/docker-compose.yml      restart;
docker-compose -f ./service-community/docker/docker-compose.yml       restart;
docker-compose -f ./service-store/docker/docker-compose.yml       restart;
docker-compose -f ./service-user/docker/docker-compose.yml       restart;
docker-compose -f ./service-front/docker/docker-compose.yml       restart;
docker-compose -f ./service-fee/docker/docker-compose.yml       restart;
docker-compose -f ./service-common/docker/docker-compose.yml       restart;