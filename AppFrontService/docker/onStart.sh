#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/appFront .

docker run -ti --name appFront_test -p8443:8443 -idt java110/appFront:latest

docker logs -f appFront_test