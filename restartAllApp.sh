#!/usr/bin/env bash

git pull origin master

mvn clean install -Dmaven.test.skip=true
mvn package -Dmaven.test.skip=true

# 刷hosts
echo '172.17.0.2 dev.java110.com' >> /etc/hosts
echo '172.17.0.2 dev.zk.java110.com' >> /etc/hosts
echo '172.21.0.6 dev.redis.java110.com' >> /etc/hosts
echo '172.17.0.2 dev.kafka.java110.com' >> /etc/hosts
echo '172.21.0.15 dev.db.java110.com' >> /etc/hosts
echo '172.17.0.8 api.java110.com' >> /etc/hosts

if [[ -a ~/jar_deploy ]];then
echo "存在文件jar_deploy";
rm -rf jar_deploy
fi;
mkdir jar_deploy
# 启动eureka
cp eureka/target/eureka.jar ./jar_deploy/eureka.jar
cp Api/target/Api.jar ./jar_deploy/Api.jar
cp CodeService/target/CodeService.jar ./jar_deploy/CodeService.jar
cp CommentService/target/CommentService.jar ./jar_deploy/CommentService.jar
cp CommonService/target/CommonService.jar ./jar_deploy/CommonService.jar
cp CommunityService/target/CommunityService.jar ./jar_deploy/CommunityService.jar
cp FeeService/target/FeeService.jar ./jar_deploy/FeeService.jar
cp JobService/target/JobService.jar ./jar_deploy/JobService.jar
cp LogService/target/LogService.jar ./jar_deploy/LogService.jar
cp OrderService/target/OrderService.jar ./jar_deploy/OrderService.jar
cp StoreService/target/StoreService.jar ./jar_deploy/StoreService.jar
cp UserService/target/UserService.jar ./jar_deploy/UserService.jar
cp WebService/target/WebService.jar ./jar_deploy/WebService.jar
nohup java -jar -Dspring.profiles.active=dev jar_deploy/eureka.jar > eureka.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/Api.jar > Api.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/CodeService.jar > CodeService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/CommentService.jar > CommentService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/CommonService.jar > CommonService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/CommunityService.jar > CommunityService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/FeeService.jar > FeeService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/JobService.jar > JobService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/LogService.jar > LogService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/OrderService.jar > OrderService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/StoreService.jar > StoreService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/UserService.jar > UserService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/WebService.jar > WebService.log 2>&1 &
