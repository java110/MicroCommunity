#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/fee .

docker run -ti --name fee_test -p8010:8010 -idt java110/fee:latest

docker logs -f fee_test