## HC小区管理系统安装教程（开发环境）

1、下载代码 <br/>
git clone https://gitee.com/wuxw7/MicroCommunity.git <br/>
执行 mvn clean package  和 mvn clean install<br/><br/>
2、添加hosts<br/>
127.0.0.1 dev.db.java110.com <br/>
127.0.0.1 dev.zk.java110.com <br/>
127.0.0.1 dev.kafka.java110.com <br/>
127.0.0.1 dev.redis.java110.com <br/>
127.0.0.1 api.java110.com <br/>
127.0.0.1 dev.java110.com <br/>
3、安装mysql 导入TT.sql 和 hc_community.sql （文件在docs/db下）<br/>
4、安装redis 指定redis密码<br/>
5、修改模块service-开头的服务下的 application-dev.yml 和 dataSource.yml<br/>
application-dev.yml 中主要修改redis密码

```yaml
jedis:
  pool:
    config:
      maxTotal: 100
      maxIdle: 20
      maxWaitMillis: 20000
    host: dev.redis.java110.com
    port: 6379
    timeout: 3000
    password: hc
```
将 password: hc 中的hc 修改为redis 指定的密码
```yaml
spring:
  profiles:
    active: share
  http:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  application:
    name: community-service
  redis:
    database: 0
    host: dev.redis.java110.com
    port: 6379
    password: hc
    pool:
      max-active: 300
      max-wait: 10000
      max-idle: 100
      min-idle: 0
      timeout: 0
```
将 password: hc 中的hc 修改为redis 指定的密码<br/>
```yaml
dataSources:
  ds0: !!com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://dev.db.java110.com:3306/hc_community?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    username: hc_community
    password: hc_community@12345678
    minIdle: 5
    validationQuery: SELECT 1 FROM DUAL
    initialSize: 5
    maxWait: 60000
    filters: stat,wall,log4j
    poolPreparedStatements: true
  ds1: !!com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://dev.db.java110.com:3306/TT?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    username: TT
    password: TT@12345678
    minIdle: 5
    validationQuery: SELECT 1 FROM DUAL
    initialSize: 5
    maxWait: 60000
    filters: stat,wall,log4j
    poolPreparedStatements: true
```
dataSource.yml 中修改password密码<br/>
注意 比如 service-api服务中是没有dataSource.yml 文件<br/>
6、启动模块service-开头服务下的*ServiceApplicationStart.java文件中的main方法<br/>
注意启动是没有顺序的，但是我们优先推荐您启动service-eureka<br/>
7、下载前段代码 <br/>
git clone https://gitee.com/java110/MicroCommunityWeb.git <br/>
8、修改app.js 中的地址<br/>
```shell script
app.use('/callComponent', proxy('http://192.168.100.108:8008', opts));
app.use('/app', proxy('http://192.168.100.108:8008', opts));
```
将192.168.100.108 修改为你后端的ip<br/>
9、启动并且访问<br/>
npm start
```shell script
PS C:\project\vip\MicroCommunityWeb> npm start
> micrcommunityweb@0.0.0 start C:\project\vip\MicroCommunityWeb
> node ./bin/www
```
表示启动成功<br/>
访问地址：http://localhost:3000