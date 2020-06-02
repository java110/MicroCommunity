#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/community .

docker run -ti --name community_test -p8006:8006 -idt java110/community:latest

docker logs -f community_test