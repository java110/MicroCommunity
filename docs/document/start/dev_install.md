### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2019-06-17|wuxw

### 本页内容

说明如何搭建HC小区管理系统开发环境。


### IDEA开发工具搭建HC小区管理系统

#### 安装maven

这里略过,请自行百度，maven的settings.xml 中的中央仓库地址建议修改为国内仓库地址，如阿里云的，这样有利于项目编译和打包速度

```
<mirrors>
        <mirror>
              <id>alimaven</id>
              <name>aliyun maven</name>
              <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
              <mirrorOf>central</mirrorOf>
        </mirror>

</mirrors>
```

#### 安装git

这里略过,请自行百度

#### 安装 idea 开发工具

社区版和收费版都可以, 百度自行下载安装

#### 下载代码

选择项目保存文件夹如 C:\Users\Administrator\Documents\project\hc 目录下，执行

```
   git clone https://github.com/java110/MicroCommunity.git

```

或者 备份版本代码，主要是下载速度比较快

 ```
   git clone https://gitee.com/wuxw7/MicroCommunity.git

 ```

#### 编译代码

进入MicroCommunity目录下执行命令

```
    mvn clean install

```

和

```
    mvn clean package

```

#### idea 打开代码

用maven的方式打开项目，在idea的设置中修改第一步安装的maven地址和settings.xml地址，点击OK，然后在idea的右上侧点击Maven选项，点击Reimport All Maven Projects,重新导入maven依赖jar包

#### 安装mysql

推荐安装 5.6 版本mysql ，安装过程请自行百度，将 MicroCommunity文件下的 MicroCommunity.sql 文件导入到mysql中，有可能这个sql不是最新的，你也可以加入群827669685，获取最新的sql文件

#### 安装kafka

请自行百度安装

#### 安装 zookeeper

请自行百度安装

#### 安装Redis

请自行百度安装

#### 修改hosts

如果本地没有安装kafka zookeeper Redis mysql 等开源中间件，也可以用演示环境的 kafka zookeeper mysql 和Redis，这样只需要在hosts 中加入

```
api.java110.com 127.0.0.1
dev.java110.com 127.0.0.1

```

如果你本地安装了 kafka zookeeper Redis mysql 开源中间件 则将对应域名改为对应IP

```
dev.db.java110.com 这里写mysql的IP
dev.zk.java110.com 这里写zookeeper的IP
dev.kafka.java110.com 这里写kafka的IP
api.java110.com 127.0.0.1
dev.java110.com 127.0.0.1

```

#### 启动相应服务

需要启动的服务有 eureka、 Api、OrderService、CommunityService、StoreService、UserService、WebService、FeeService

找到服务下 src目录下 main/java/com/java110/您的服务名/XXApplicationStart.java 类打开 点击运行main方法就可以启动

注意：由于OrderService 启动需要刷缓存所以比起其他服务 需要传 -Dcache 参数才可以，具体参考地址 https://blog.csdn.net/u013713294/article/details/53020293

