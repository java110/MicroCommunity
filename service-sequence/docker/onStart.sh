#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/code .

docker run -ti --name code_test -p8003:8003 -idt java110/code:latest

docker logs -f code_test