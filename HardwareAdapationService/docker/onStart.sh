#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/hardwareAdapation .

docker run -ti --name hardwareAdapation_test -p8010:8010 -idt java110/hardwareAdapation:latest

docker logs -f hardwareAdapation_test