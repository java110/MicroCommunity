#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/center .

docker run -ti --name center_test -p8001:8001 -idt java110/center:latest

docker logs -f center_test