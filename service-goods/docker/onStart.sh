#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/goods .

docker run -ti --name goods_test -p8014:8014 -idt java110/goods:latest

docker logs -f goods_test