#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/fee .

docker run -ti --name fee_test -p8006:8006 -idt java110/fee:latest

docker logs -f fee_test