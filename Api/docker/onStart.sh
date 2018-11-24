#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/api .

docker run -ti --name api_test -p8001:8001 -idt java110/api:latest

docker logs -f api_test