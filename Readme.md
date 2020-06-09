# HC小区管理系统是前后端完全开源并免费商用的小区物业管理系统，包含小区后端项目，小区前段项目，物业版uni-app和业主版uni-app,最新SQL文件加群获取827669685

## 开源代码说明

### HC小区后端代码
 [https://gitee.com/wuxw7/MicroCommunity](https://gitee.com/wuxw7/MicroCommunity)
### HC小区前段代码
[https://gitee.com/java110/MicroCommunityWeb](https://gitee.com/java110/MicroCommunityWeb)
### HC智慧家庭（业主版）
[https://gitee.com/java110/WechatOwnerService](https://gitee.com/java110/WechatOwnerService)
### HC掌上物业（物业版uni-app）
[https://gitee.com/java110/PropertyApp](https://gitee.com/java110/PropertyApp)

### 分支说明（branch）

分支管理说明，master 为主分支，0.2-stable 为最新稳定分支（推荐使用） 0.1-stable 为前后端不分离版本（不推荐）


### 如何开始（how to start）

[http://www.homecommunity.cn/](http://www.homecommunity.cn/)

### 如何安装（how to install）

方案一、centos7.6一键式自动安装

> 链接：https://pan.baidu.com/s/1sIPrxnwipF6ey_8k4rncWA
  提取码：2e47

如果下载不下来，请加群827669685在群文件中【HC系统部署文档】中获取hcAutoInstall.tar 自动部署shell脚本部署

> tar -xvf hcAutoInstall.tar

> 修改env.sh 中的相应IP和密码

> sh install.sh

[参考视频](https://www.bilibili.com/video/BV1Dk4y19728/)

方案二、根据需求自助部署

[参考视频](https://www.bilibili.com/video/BV1N7411E7rf/)


### 系统简介（introduction）
1. 开发工具：

java1.8 + idea/eclipse + mysql + redis

2. 官方网站
   http://www.homecommunity.cn
   
   技术业务交流群QQ: 827669685

3. 硬件要求:

   测试环境基本要求：4个CPU 、16G内存和80G硬盘
   
4. 软件要求:

    需要自行安装 mysql5.6、zookeeper、Redis和kafka
    
    需要启动的服务，可以查看 reBuildAll.sh,目前基本为：eureka、Api、OrderService、CommunityService、StoreService、UserService
    FrontService、FeeService和CommonService
    
    推荐用docker部署管理，需要安装docker、docker-compose （如果夸主机部署请用docker swarm 自行创建java110-net network），
    如果是单机推荐直接运行 reBuildAll.sh
    
### 业主小程序

   ![image](docs/images/hcOwner.png)
    
   github: [https://github.com/java110/WechatOwnerService](https://github.com/java110/WechatOwnerService)
   
   gitee: [https://gitee.com/java110/WechatOwnerService](https://gitee.com/java110/WechatOwnerService)
    
### 系统功能（function） 

   ![image](docs/images/hc_function.png)
   
   主要系统功能请查看
     
   [http://www.homecommunity.cn//document/#/func/funcation](http://www.homecommunity.cn//document/#/func/funcation)
  

### 演示地址（demo）

[http://www.homecommunity.cn/](http://www.homecommunity.cn/)

物业 账号/密码：wuxw/admin

代理商 账号/密码：dails/admin

运维团队 账号/密码：admin/admin

开发团队 账号/密码：dev/(由于开发者权限较大，删除数据会影响稳定性，查看具体功能，可以单独部署在u_user 表中修改)

### 运行效果（view）
1.在浏览器输入 http://localhost:3000/ 如下图

![image](docs/img/login.png)

    用户名为 wuxw 密码为 admin  如下图

    点击登录，进入如下图：
    
![image](docs/img/index.png)

![image](docs/img/owner.png)

2.数据模型图
![image](dataModel.png)

### 加入我们（join）

加入微小区交流群随时了解项目进度，和java110开发者零距离沟通 qq群号 827669685，邮箱：928255095@qq.com

![image](docs/images/MicroCommunity_qq.png)



