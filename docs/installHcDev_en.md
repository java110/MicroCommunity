## HC Community Management System Installation Tutorial (Development Environment)

1、download code <br/>
git clone https://github.com/java110/MicroCommunity.git <br/>
Execute mvn clean package and mvn clean install<br/><br/>
2、Add hosts<br/>
127.0.0.1 dev.db.java110.com <br/>
127.0.0.1 dev.zk.java110.com <br/>
127.0.0.1 dev.kafka.java110.com <br/>
127.0.0.1 dev.redis.java110.com <br/>
127.0.0.1 api.java110.com <br/>
127.0.0.1 dev.java110.com <br/>
3、Install mysql and import TT.sql and hc_community.sql (files are under docs/db)<br/>
4、Install redis and specify the redis password<br/>
5、Modify application-dev.yml and dataSource.yml under the service at the beginning of the module service-<br/>
Mainly modify the redis password in application-dev.yml

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
Change password: hc in hc to the password specified by redis
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
Change password: hc in hc to the password specified by redis<br/>
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
Modify password in dataSource.yml<br/>
Note that for example, there is no dataSource.yml file in the service-api service<br/>
6、Start the main method in the *ServiceApplicationStart.java file under the service-start service-start module<br/>
Note that there is no order to start, but we recommend that you start service-eureka first<br/>
7、Download the previous code <br/>
git clone https://github.com/java110/MicroCommunityWeb.git <br/>
8、Modify the address in app.js<br/>
```shell script
app.use('/callComponent', proxy('http://192.168.100.108:8008', opts));
app.use('/app', proxy('http://192.168.100.108:8008', opts));
```
Change 192.168.100.108 to the ip of your backend<br/>
9、start and access<br/>
npm start
```shell script
PS C:\project\vip\MicroCommunityWeb> npm start
> micrcommunityweb@0.0.0 start C:\project\vip\MicroCommunityWeb
> node ./bin/www
```
Indicates that the startup was successful<br/>
address：http://localhost:3000