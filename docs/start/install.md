### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw

### 本页内容

1. [docker 安装（推荐）](#docker-安装推荐)
2. [普通安装](#普通安装)

## docker 安装（推荐）

下载代码

```
  git clone https://github.com/java110/MicroCommunity.git
```

![下载代码](/images/git_code.jpg)


1. mysql 安装

切换目录到

```

cd MicroCommunity/java110-config/docker/mysql

docker build -t java110/docker-mysql .

```
![mysql](/images/mysql_01.jpg)

```
  docker run -ti --name mysql_test -e MYSQL_ROOT_PASSWORD=123456 -p3306:3306 -idt java110/docker-mysql:latest

  docker logs -f mysql_test

```
出现如下图安装成功

![mysql安装成功](/images/mysql_02.jpg)

用工具登录 如下图：

![mysql_login](/images/mysql_03.jpg)

![mysql_login](/images/mysql_04.jpg)

安装完成

2. redis 安装

```
docker pull redis
docker run -ti --name redis_test -p6379:6379 -idt redis:latest
docker logs -f redis_test
```
![redis download](/images/redis_01.jpg)
![redis install complate](/images/redis_02.jpg)

3. zookeeper 安装

```
docker pull zookeeper
docker run -ti --name zookeeper_test -p2181:2181 -idt zookeeper:latest
docker logs -f zookeeper_test
```
![zookeeper_download](/images/zookeeper_01.jpg)
![zookeeper install complate](/images/zookeeper_02.jpg)

4. kafka 安装

```
docker pull wurstmeister/kafka
docker run --name kafka_test -e HOST_IP=localhost -e KAFKA_ADVERTISED_PORT=9092 -e KAFKA_BROKER_ID=1 -e KAFKA_ZOOKEEPER_CONNECT=192.168.31.199:2181 -e KAFKA_ADVERTISED_HOST_NAME=localhost -e ZK=zk -p 9092 --link zookeeper_test:zk -tdi wurstmeister/kafka
docker logs -f kafka_test
```
![kafka install](/images/kafka_01.jpg)

注意 KAFKA_ZOOKEEPER_CONNECT=192.168.31.199:2181 换成自己主机的IP端口

5. 编译代码

配置hosts
dev.java110.com
dev.redis.java110.com
dev.zk.java110.com
dev.db.java110.com
dev.kafka.java110.com

修改 MicroCommunity/XXService/src/main/resources 下的application-dev(开发环境) application-prod(生产环境) application-test(测试环境) 中的数据库地址 kafka地址 zookeeper地址 redis地址等

XXService 代表 CenterService，CodeService，CommentService，ConsoleService，ShopService,StoreService,UserService 等

```
mvn clean install
```
![mvn code](/images/code_01.jpg)

第一次编译需要下载jar 时间有点长

6. 启动eureka 服务

```
  cd MicroCommunity/eureka/docker
  chmod u+x onStart.sh
  sh onStart.sh
```
![eureka 启动](/images/eureka_01.jpg)
![eureka 启动](/images/eureka_02.jpg)
![eureka 启动](/images/eureka_03.jpg)

注意：如果报 Cannot connect to the Docker daemon. Is the docker daemon running on this host?错，执行
下面代码

```
sudo gpasswd -a ${USER} docker
sudo service docker restart
```
退出后重新进入

7. 编码生成服务（CodeService）

```
  cd MicroCommunity/CodeService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![code install](/images/code_01.jpg)

8. 中心服务(CenterService)

```
  cd MicroCommunity/CenterService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![中心服务](/images/center_01.jpg)
![中心服务](/images/center_02.jpg)

9. 评论服务(CommentService)

```
  cd MicroCommunity/CommentService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![评论服务](/images/comment_01.jpg)
![评论服务](/images/comment_02.jpg)

10. 控制服务(ConsoleService)

```
  cd MicroCommunity/ConsoleService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![控制服务](/images/console_01.jpg)

11. 商品服务(ShopService)

```
  cd MicroCommunity/ShopService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![商品服务](/images/shop_01.jpg)

12. 商户服务(StoreService)

```
  cd MicroCommunity/StoreService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![商户服务](/images/store_01.jpg)

13. 用户服务(UserService)

```
  cd MicroCommunity/UserService/docker
  chmod u+x onStart.sh
  sh onStart.sh
```

![用户服务](/images/user_01.jpg)

目前为止全部启动完成

![启动完成](/images/all.jpg)

![docker](/images/docker_all.jpg)



## 普通安装


[>回到首页](home)
