微小区平台

分支说明：

分支管理说明，test为最新代码未测试代码（主要为了防止本地代码遗失），master 为最新测试过代码（待产品化），product 为产品化代码

开发工具：

java1.8 + idea/eclipse + mysql 

技术架构：

Java + spring cloud + mybatis + mysql + kafka + redis



服务依赖关系

![image](dependencies.png)


服务安装说明：

1、安装点[这里](/wiki/install)

2、用post工具属性缓存 地址为：http://yourIp:8001/cacheApi/flush 如下图：

![image](ConsoleService/doc/img/20180507231201.jpg)



3、在浏览器输入 http://yourIp:6001/ 如下图

![image](ConsoleService/doc/img/20180507230124.jpg)

    点击高级，继续前往。

4、登录系统，用户名为 admin 密码为 admin  如下图

![image](ConsoleService/doc/img/20180507230330.jpg)

    点击登录，进入如下图：
    
  ![image](ConsoleService/doc/img/20180507231045.jpg)
  
  ![image](ConsoleService/doc/img/20180509223107.jpg)
  
  ![image](ConsoleService/doc/img/20180511010848.jpg)


5、统一接口地址：http://ip:8001/httpApi/service

加入微小区交流群随时了解项目进度，和java110开发者零距离沟通 qq群号 827669685，邮箱：928255095@qq.com

![image](MicroCommunity_qq.png)


