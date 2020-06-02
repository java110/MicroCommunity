#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/common .

docker run -ti --name common_test -p8006:8006 -idt java110/common:latest

docker logs -f common_test