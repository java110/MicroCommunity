#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/shop .

docker run -ti --name shop_test -p8007:8007 -idt java110/shop:latest

docker logs -f shop_test