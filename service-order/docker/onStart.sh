#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/order .

docker run -ti --name order_test -p8001:8001 -idt java110/order:latest

docker logs -f order_test