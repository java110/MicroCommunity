#!/bin/bash
#### debug model prod
#nohup java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar -Dspring.profiles.active=dev target/AppFrontService.jar > appFront.log 2>&1 &

#### normal prod model
#nohup java -jar -Dspring.profiles.active=prod target/AppFrontService.jar > appFront.log 2>&1 &

#### normal test model
#nohup java -jar -Dspring.profiles.active=test target/AppFrontService.jar > appFront.log 2>&1 &

#### normal dev model
#nohup java -jar -Dspring.profiles.active=dev target/AppFrontService.jar > appFront.log 2>&1 &

nohup java -jar -Dspring.profiles.active=$1 target/FrontService.jar > front.log 2>&1 &

tail -100f front.log