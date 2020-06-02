#!/bin/bash
#### debug model prod
#nohup java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar -Dspring.profiles.active=dev CommentService.jar $1 > comment.log 2>&1 &

#### normal prod model
#nohup java -jar -Dspring.profiles.active=prod $1 CommentService.jar > comment.log $1 2>&1 &

#### normal test model
#nohup java -jar -Dspring.profiles.active=test $1 CommentService.jar > comment.log $1 2>&1 &

#### normal dev model
nohup java -jar -Dspring.profiles.active=dev $1 CommentService.jar > comment.log $1 2>&1 &

tail -100f comment.log