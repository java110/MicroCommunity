#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/comment .

docker run -ti --name comment_test -p8008:8008 -idt java110/comment:latest

docker logs -f comment_test