#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/console .

docker run -ti --name console_test -p8443:8443 -idt java110/console:latest

docker logs -f console_test