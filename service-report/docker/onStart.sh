#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/report .

docker run -ti --name report_test -p8006:8006 -idt java110/report:latest

docker logs -f report_test