#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/property .

docker run -ti --name property_test -p8006:8006 -idt java110/property:latest

docker logs -f property_test