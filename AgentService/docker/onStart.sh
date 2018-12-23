#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/agent .

docker run -ti --name agent_test -p8006:8006 -idt java110/agent:latest

docker logs -f agent_test