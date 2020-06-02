#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/store .

docker run -ti --name store_test -p8006:8006 -idt java110/store:latest

docker logs -f store_test