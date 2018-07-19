#!/bin/bash

#### normal dev model
nohup java -jar target/eureka.jar > eureka.log $1 2>&1 &

tail -100f eureka.log