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
    
### 系统功能（function） 

1、一期功能

功能大类|功能点|描述|完成情况
:-: | :-: | :-: | :-:
小区管理|添加小区|小区是由代理商统一添加，运维团队去审核|已完成
小区管理|小区审核|代理商添加小区后由运维团队去审核|已完成
小区管理|入驻小区|入驻小区是由各个商户去入驻小区|已完成
小区管理|入驻审核|小区入驻审核是由代理商审核，审核通过后才能入驻到该小区|已完成
资产管理|楼栋管理|楼信息添加，修改，删除功能|已完成
资产管理|单元管理|单元信息添加，修改，删除功能|已完成
资产管理|房屋管理|房屋信息添加，修改，删除功能|已完成
资产管理|车位管理|车位的添加 修改 删除功能|已完成
业主管理|业主信息|业主增删改查，通过业主信息，通过房屋车位信息定位业主，做二次业务|已完成
业主管理|业主成员|添加修改删除业主家庭成员，并且将人像照片同步门禁等设备|已完成
智慧服务|公告管理|发布和编辑公告信息|已完成
智慧服务|访客登记|将访客信息进行登记|已完成
智慧服务|投诉意见|通过电话投诉登记，或小程序投诉登记|已完成
智慧服务|审核投诉单|投诉单处理和审核功能|已完成
智慧服务|投诉历史单|投诉历史单为处理过的投诉单|已完成
报修管理|报修登记|报修通过电话和小程序方式报修登记，电话登记时，进入业主页面，定位业主进行报修登记|已完成
报修管理|派单信息|报修通过电话和小程序方式报修登记手工派单方式派单给相应的员工人员|已完成
报修管理|我的报修|员工处理过的报修工单页面|已完成
采购管理|物品信息|物品管理页面，增删改功能|已完成
采购管理|审核人员|审核人员定义|已完成
采购管理|采购申请|采购申请，由采购人员填写采购单申请采购|已完成
采购管理|物品出库|物品出库管理|已完成
组织管理|组织信息|组织信息，添加 删除组织管理|已完成
组织管理|员工信息|添加删除编辑员工信息|已完成
报表管理|缴费清单|显示物业费停车费缴费清单|已完成
报表管理|员工收费|各个工号收费报表页面|已完成
设备管理|设备信息|小区设备，包括门禁，车辆门禁等管理|已完成


  

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

![流程图](docs/document/images/machineFlow.png)

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


