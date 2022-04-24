[english](Readme_en.md) [中文](Readme_cn.md)
## 说明
 HC小区管理系统是一套saas物业管理的系统，包括 房产、业主、 费用 （可以线上缴费）、报修（可以线上报修）、投诉建议、采购、巡检、停车、门径、道闸、监控、工作流、问卷和公告等功能。
 
 相关代码：<br/>
 1、[物业系统前端](https://gitee.com/java110/MicroCommunityWeb) : 物业员工使用电脑端<br/>
 2、[物业系统后端](https://gitee.com/wuxw7/MicroCommunity): 核心业务处理端<br/>
 3、[业主手机端](https://gitee.com/java110/WechatOwnerService): 提供用户使用，包括投诉 建议，缴费、报修 等<br/>
 4、[物业手机版](https://gitee.com/java110/PropertyApp): 物业员工使用手机端<br/>

## 如何安装

1、下载代码 <br/><br/>
git clone https://github.com/java110/MicroCommunity.git <br/>
执行 mvn clean package  和 mvn clean install<br/><br/>
2、添加hosts<br/><br/>
127.0.0.1 dev.db.java110.com <br/>
127.0.0.1 dev.zk.java110.com <br/>
127.0.0.1 dev.kafka.java110.com <br/>
127.0.0.1 dev.redis.java110.com <br/>
127.0.0.1 api.java110.com <br/>
127.0.0.1 dev.java110.com <br/><br/>
3、安装mysql 导入TT.sql 和 hc_community.sql （文件在docs/db下）<br/><br/>
4、安装redis 指定redis密码<br/><br/>
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
dataSource.yml 中修改password密码<br/>
注意 比如 service-api服务中是没有dataSource.yml 文件<br/>
6、启动模块service-开头服务下的*ServiceApplicationStart.java文件中的main方法<br/>
注意启动是没有顺序的，但是我们优先推荐您启动service-eureka<br/>
7、下载前段代码 <br/>
git clone https://github.com/java110/MicroCommunityWeb.git <br/>
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







### 操作文档

[操作文档](http://www.homecommunity.cn/operateDoc)

### 技术文档

[技术文档](http://www.homecommunity.cn/devDoc)

    
### 产品

   ![image](docs/img/hc.png)

    
### 系统功能（function） 

   ![image](docs/img/func.png)
   
   主要系统功能请查看
     
   [http://www.homecommunity.cn/devDoc/#/func/funcation](http://www.homecommunity.cn/devDoc/#/func/funcation)
  

### 演示地址（demo）

[http://www.homecommunity.cn/](http://www.homecommunity.cn/)

物业 账号/密码：wuxw/admin

运维团队 账号/密码：admin/admin

开发团队 账号/密码：dev/(由于开发者权限较大，删除数据会影响稳定性，查看具体功能，可以单独部署在u_user 表中修改)

### 运行效果（view）
1.在浏览器输入 http://localhost:3000/ 如下图

![image](docs/img/login.png)

    用户名为 wuxw 密码为 admin  如下图

    点击登录，进入如下图：

![image](docs/img/0004.png)

![image](docs/img/index.png)

![image](docs/img/owner.png)


### 加入我们（join）


加入微小区交流群随时了解项目进度，和java110开发者零距离沟通 qq群号 857791253、1038870655、770542020、274026637 邮箱：928255095@qq.com

![image](docs/img/qq.png)



