### 变更历史（change history）
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw


### 代码分享视频

1、[安装视频](http://www.iqiyi.com/w_19s9dzcnp9.html)

2、[小区管理系统源码讲解](http://www.iqiyi.com/w_19s7u6le2p.html#vfrm=16-1-1-1)

3、[小区管理系统服务端开发视频](http://www.iqiyi.com/w_19s7omicex.html#curid=38432723509_ef5ddb9c572fa848bcdd80f193d78eeb)

4、[长期更新视频](https://space.bilibili.com/403702784)

### 分支说明（branch）

分支管理说明，test为最新代码未测试代码（主要为了防止本地代码遗失），master 为最新测试过代码（待产品化），product 为产品化代码

### 系统简介（introduction）
1. 开发工具：

java1.8 + idea/eclipse + mysql 

2. 技术架构：

Java + spring cloud + mybatis + mysql + kafka + redis

3. 服务依赖关系

![image](dependencies.png)

4. 硬件要求:

   测试环境基本要求：4个CPU 、16G内存和80G硬盘
   
5. 软件要求:

    需要自行安装 mysql5.6、zookeeper、Redis和kafka
    
    需要启动的服务，可以查看 reBuildAll.sh,目前基本为：eureka、Api、OrderService、CommunityService、StoreService、UserService
    WebService、FeeService和CommonService
    
    推荐用docker部署管理，需要安装docker、docker-compose （如果夸主机部署请用docker swarm 自行创建java110-net network），
    如果是单机推荐直接运行 reBuildAll.sh
    
    

### 演示地址（demo）

[http://www.homecommunity.cn](http://www.homecommunity.cn)

物业 账号/密码：wuxw/admin

代理商 账号/密码：dails/admin

运维团队 账号/密码：admin/admin

开发团队 账号/密码：dev/(由于开发者权限较大，删除数据会影响稳定性，查看具体功能，可以单独部署查看)

### 如何开始（how to start）

[http://www.homecommunity.cn](http://www.homecommunity.cn)

### 如何安装（how to install）

[安装文档](http://www.homecommunity.cn/document/#/start/dev_install)

### 解决方案

[解决方案](https://docs.qq.com/doc/DQW9XWW50R3NjWmN6) 不断完善中

### 硬件接入

目前对接锐目门禁系统，如果您想将您的硬件对接到此项目，请加qq群号827669685 联系我们

### 二次开发视频

[视频](http://www.homecommunity.cn/document/#/start/vedio)

### 接口协议

1. [Api协议](http://www.homecommunity.cn/document/#/api/user/register)

2. [后台协议](http://www.homecommunity.cn/document/#/dictionary)

### 运行效果（view）
1.在浏览器输入 https://localhost:8443/ 如下图

![image](WebService/doc/img/login.png)

    用户名为 wuxw 密码为 admin  如下图

    点击登录，进入如下图：
    
![image](WebService/doc/img/staff.png)

![image](WebService/doc/img/community.png)

2.数据模型图
![image](dataModel.png)

### 加入我们（join）

加入微小区交流群随时了解项目进度，和java110开发者零距离沟通 qq群号 827669685，邮箱：928255095@qq.com

![image](MicroCommunity_qq.png)

### 成为开发者

如果您对小区 物业 有较深的理解，也致力于开发一套系统方便与物业，业主沟通交流，如果您还有空闲的时间，不怕吃苦，hc小区开发团队欢迎您的加入！
qq群号 992420128  微信：syj15309714817 （添加时请备注HC小区开发）

![image](join_me.JPG)


