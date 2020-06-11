
## 介绍

HC小区物业管理系统是由java110团队于2017年4月份发起的前后端分离、分布式架构开源项目，目前我们的代码开源在github 和gitee上，
开源项目由HC小区管理系统后端，HC小区管理系统前端，HC小区管理系统业主手机版和HC小区管理系统物业手机版

## 源码下载

[https://gitee.com/java110](https://gitee.com/java110)

## 在线体验

地址：[https://hc.demo.winqi.cn/](https://hc.demo.winqi.cn/)

物业 账号/密码：wuxw/admin

代理商 账号/密码：dails/admin

运维团队 账号/密码：admin/admin

开发团队 账号/密码：dev/(由于开发者权限较大，删除数据会影响稳定性，查看具体功能，可以单独部署在u_user 表中修改)

## 系统要求

### 开发工具

java1.8 + idea/eclipse + mysql + redis

### 硬件要求

测试环境基本要求：4个CPU 、16G内存和80G硬盘

### 软件要求

需要自行安装 mysql5.6、zookeeper、Redis和kafka

需要启动 service-eureka、service-api、service-order、service-community、
service-store、service-user、service-front、service-fee、service-common和service-job


## 项目结构说明

     java110-bean      #存放实体bean Vo对象（前台返回对象） Dto对象（数据传输对象） Po对象（数据库持久层对象）
     java110-config    #存放系统配置类，公共配置文件如logback.xml 日志配置文件
     java110-core      #存放系统核心类，各个微服务接口类，上下文对象，工厂类和事件相关类
     java110-db        #存放整套系统的SQL文件mapper
     java110-generator #代码生成器
     java110-service   #存放服务基础信息，基本存放基类和自研动态SQL解析引擎
     java110-util      #存放工具类，如时间工具类，日志工具类 常量类 异常处理类
     service-api       #统一接口封装服务，对外提供统一场景化接口能力
     service-comment   #评论信息存放服务
     service-common    #配置公共存放服务，包括图片上传ftp, 工作流，设备信息
     service-community #小区资产信息 存放服务 楼栋 单元 房屋 巡检 通知 停车位 报修 访客等功能
     service-eureka    #微服务管理服务
     service-fee       #费用处理微服务 费用项功能 缴费 退费功能
     service-front     #统一对外接入服务，主要处理 登录验证 权限验证，对接微信 ，app 等
     service-job       #系统定时任务服务 人脸同步设备 员工同步考勤机和费用定时出账
     service-log       #系统日志记录服务
     service-order     #系统服务调度服务,主要保证事务一致性，和业务轨迹记录
     service-report    #报表服务
     service-rule      #业务规则服务
     service-sequence  #序列生成服务，采用 google 雪花算法
     service-store     #商户服务，系统中 物业 代理商 开发者 运营团队 都属于商户
     service-user      #用户服务，用户信息 业主信息 家庭成员

## 联系我们

HC小区管理系统交流群 827669685

作者：928255095