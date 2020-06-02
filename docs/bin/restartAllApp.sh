#!/usr/bin/env bash
cd ..
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
cp service-eureka/target/service-eureka.jar ./jar_deploy/service-eureka.jar
cp service-api/target/service-api.jar ./jar_deploy/service-api.jar
cp service-code/target/service-code.jar ./jar_deploy/service-code.jar
cp service-comment/target/service-comment.jar ./jar_deploy/service-comment.jar
cp service-common/target/service-common.jar ./jar_deploy/service-common.jar
cp service-community/target/service-community.jar ./jar_deploy/service-community.jar
cp service-fee/target/service-fee.jar ./jar_deploy/service-fee.jar
cp service-job/target/JobService.jar ./jar_deploy/service-job.jar
cp service-log/target/LogService.jar ./jar_deploy/service-log.jar
cp service-order/target/OrderService.jar ./jar_deploy/service-order.jar
cp service-store/target/StoreService.jar ./jar_deploy/service-store.jar
cp service-user/target/UserService.jar ./jar_deploy/service-user.jar
cp service-front/target/WebService.jar ./jar_deploy/service-front.jar
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-eureka.jar > eureka.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-api.jar > Api.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-code.jar > CodeService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-comment.jar > CommentService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-common.jar > CommonService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-community.jar > CommunityService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-fee.jar > FeeService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-job.jar > JobService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-log.jar > LogService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-order.jar > OrderService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-store.jar > StoreService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-user.jar > UserService.log 2>&1 &
nohup java -jar -Dspring.profiles.active=dev jar_deploy/service-front.jar > frontService.log 2>&1 &
