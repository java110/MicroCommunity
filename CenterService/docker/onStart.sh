#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/docker-centerService .

docker run -ti --name centerService_test -p8001:8001 -idt java110/docker-centerService:latest

docker logs -f centerService_test