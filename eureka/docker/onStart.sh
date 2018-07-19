#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/docker-eureka .

docker run -ti --name eureka_test -p8761:8761 -idt java110/docker-eureka:latest

docker logs -f eureka_test