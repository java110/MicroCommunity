#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/user .

docker run -ti --name user_test -p8002:8002 -idt java110/user:latest

docker logs -f user_test