#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/front .

docker run -ti --name front_test -p8020:8020 -idt java110/front:latest

docker logs -f front_test