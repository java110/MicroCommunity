### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2020-03-20|蛋蛋的忧伤


### 一、	部署说明

1、	软硬件配置

硬件：云服务器最低配置4核16G

软件：需要安装 mysql + kafka + zookeeper + redis，docker可选

开源代码说明：

HC小区后端代码

https://gitee.com/wuxw7/MicroCommunity

HC小区前段代码

https://gitee.com/java110/MicrCommunityWeb

HC智慧家庭（业主版）

https://gitee.com/java110/WechatOwnerService

HC掌上物业（物业版）

https://gitee.com/java110/PropertyApp

分支说明（branch）：

分支管理说明，master 为前后端未分离代码，back 为前后端分离后端代码


2、	新建账号

添加hc、 mysql、 redis、 zk 、kafka 用户

    # 应用账户，安装docker，运行jar包
    sudo adduser hc

    # 数据库
    sudo adduser mysql

    sudo adduser redis
    sudo adduser zk
    sudo adduser kafka

修改密码

    passwd hc

密码应符合大小写英文字母+特殊符号+数字，最短8位较好

    Changing password for user hc.
    New password:

如 Yao@5937。

3、接下来赋予sudo权限，输入命令

    sudo visudo

此命令意思是vim /etc/sudoers，但是会有sudo的校验，所以推荐上面的用法。

在文件中，找到下面这段，在root下添加上面添加的5个用户

    ## Allow root to run any commands anywhere
    root    ALL=(ALL)       ALL

添加完后，如下

    ## Allow root to run any commands anywhere
    root    ALL=(ALL)       ALL
    hc      ALL=(ALL)       ALL
    mysql   ALL=(ALL)       ALL
    redis   ALL=(ALL)       ALL
    zk      ALL=(ALL)       ALL
    kafka   ALL=(ALL)       ALL

:wq 保存退出。

### 二、	安装docker

1、切换hc用户，登录服务器

2、安装docker

    sudo yum install docker -y

查看docker版本

    docker version

> 疑问：
>
> 看docker的安装文章都是用yum install docker-ce 安装的，是因为ce是免费版。
但是当前时间2020/03/19，这个命令已经无法安装。
>
> 那用 yum install docker -y 命令是安装的 CE 还是 EE 版本？

我的版本

    [root@hostone /]# docker version
    Client:
     Version:         1.13.1
     API version:     1.26
     Package version:
    Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?

有句奇怪的话，问你docker咋没运行?别急，启动下

    sudo systemctl start docker  #启动docker
    sudo systemctl enable docker #开机启动docker
    sudo systemctl status docker #查看docker状态

再用docker version 看下

    [root@hostone /]# docker version
    Client:
     Version:         1.13.1
     API version:     1.26
     Package version: docker-1.13.1-109.gitcccb291.el7.centos.x86_64
     Go version:      go1.10.3
     Git commit:      cccb291/1.13.1
     Built:           Tue Mar  3 17:21:24 2020
     OS/Arch:         linux/amd64

    Server:
     Version:         1.13.1
     API version:     1.26 (minimum version 1.12)
     Package version: docker-1.13.1-109.gitcccb291.el7.centos.x86_64
     Go version:      go1.10.3
     Git commit:      cccb291/1.13.1
     Built:           Tue Mar  3 17:21:24 2020
     OS/Arch:         linux/amd64
     Experimental:    false

ok，很简单地安装完成了。


最后，创建docker用户组，赋权给hc用户

    sudo groupadd docker

    sudo usermod -aG docker hc

切换到hc账户，查看docker状态

    sudo docker ps

然后需要安装一个docker-comose，来启动，停止和重启应用

    sudo yum install docker-compose


docker启动、停止命令

    # 启动
    sudo systemctl start docker

    # 守护进程重启
    sudo systemctl daemon-reload

    # 重启docker服务
    sudo systemctl restart docker

    # 重启docker服务
    sudo service docker restart

    # 关闭docker
    sudo systemctl stop docker


### 三、安装mysql

1、新开个mysql连接窗口，切换到mysql 用户。

 2、下载安装mysql


    # 下载5.7 安装包
    wget http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm

    # 配置安装依赖
    sudo yum localinstall -y mysql57-community-release-el7-8.noarch.rpm

    # 安装数据库
    sudo yum install -y mysql-community-server

    # 数据库启动
    sudo systemctl start mysqld

    # 查看数据库状态
    systemctl status mysqld

    # 数据库启动
    sudo systemctl enable mysqld

3、安装启动完成，查看密码

    # 密码在mysql日志中
    grep 'pass' /var/log/mysqld.log

    [mysql@iZ2zebthf35ejlps5v87ksZ ~]$ grep 'pass' /var/log/mysqld.log
    2020-03-14T12:28:57.051218Z 1 [Note] A temporary password is generated for root@localhost: <z?Shbek>8Gd

<z?Shbek>8Gd 就是我的初始密码了。

4、连接mysql

    # 连接mysql， 默认端口3306
    mysql -u root -p

输入密码<z?Shbek>8Gd，进入mysql命令行，修改root密码

    SET PASSWORD = PASSWORD('Db@369012');

    mysql> SET PASSWORD = PASSWORD('Db@369012');
    Query OK, 0 rows affected, 1 warning (0.00 sec)

5、创建hc用户，赋权

    create user 'TT'@'%' identified by 'TT@HCvvMM33';
    create user 'hc_community'@'%' identified by 'hc_community@HCvvMM33';
    flush privileges;
    CREATE DATABASE `TT` ;
    grant all privileges on `TT`.* to 'TT'@'%' ;
    CREATE DATABASE `hc_community` ;
    grant all privileges on `hc_community`.* to 'hc_community'@'%' ;
    flush privileges;

6、启动配置

如果是5.7版本的mysql，导入时可能报错，需要修改my.cnf

    sudo vim /etc/my.cnf

修改内容如下，在[mysqld]下添加

    # 导入大SQL文件
    max_allowed_packet=900M

    # 解决5.6的SQL在5.7的执行错误 ERROR 1067 (42000) Invalid default value for 'end_time'
    sql_mode = ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION


修改完后，重启mysql

    sudo systemctl restart mysqld

    systemctl status mysqld

7、导入最新的sql文件

连接TT用户，导入前后端分离版的SQL，文件名

> 分离版2020.3.18.sql

连接hc_community,导入SQL文件

> hc_community20200220.sql



四、安装java

1、切回hc用户登陆服务器

    su - hc

2、上传java至/home/hc

    # 创建java文件夹
    sudo mkdir –p /usr/local/java

    # 解压至/usr/local/java
    sudo tar zxvf jdk-8u131-linux-x64.tar.gz -C /usr/local/java

3、配置java环境

打开系统配置文件

    sudo vim /etc/profile

在文件最后添加内容如下：

    export JAVA_HOME=/usr/local/java/jdk1.8.0_131
    export JRE_HOME=$JAVA_HOME/jre
    export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
    export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH

记得刷新变量环境

    # 刷新变量环境
    source /etc/profile

查看是否生效

    [hc@iZ2zebthf35ejlps5v87ksZ java]$ echo $JAVA_HOME
    /usr/local/java/jdk1.8.0_131

五、安装redis

1、切换为redis用户，上传源码包至/home/redis

    # 查看当前路径
    [redis@iZ2zebthf35ejlps5v87ksZ ~]$ pwd
    /home/redis

    # 查看当前路径下文件
    [redis@iZ2zebthf35ejlps5v87ksZ ~]$ ll
    total 1684
    -rw-rw-r-- 1 redis redis 1723533 Mar 14 21:03 redis-4.0.6.tar.gz

2、解压

    tar zxvf redis-4.0.6.tar.gz

文件解压后，有个文件夹redis-4.0.6，如下：

    [redis@iZ2zebthf35ejlps5v87ksZ ~]$ ll
    total 1688
    drwxrwxr-x 6 redis redis    4096 Dec  5  2017 redis-4.0.6
    -rw-rw-r-- 1 redis redis 1723533 Mar 14 21:03 redis-4.0.6.tar.gz


3、编译安装

    cd redis-4.0.6

    sudo make prefix=/home/redis/redis-4.0.6 install

如果不用sudo，安装失败，信息如下

    Hint: It's a good idea to run 'make test' ;)

    INSTALL install
    install: cannot create regular file ‘/usr/local/bin/redis-server’: Permission denied
    make[1]: *** [install] Error 1
    make[1]: Leaving directory `/home/redis/redis-4.0.6/src'
    make: *** [install] Error 2

成功安装

    make[1]: Entering directory `/home/redis/redis-4.0.6/src'
    CC Makefile.dep
    make[1]: Leaving directory `/home/redis/redis-4.0.6/src'
    make[1]: Entering directory `/home/redis/redis-4.0.6/src'

    Hint: It's a good idea to run 'make test' ;)

        INSTALL install
        INSTALL install
        INSTALL install
        INSTALL install
        INSTALL install
    make[1]: Leaving directory `/home/redis/redis-4.0.6/src'

4、redis配置修改

备份reids.conf

    cp redis.conf redis.conf_hc

    # 修改redis配置
    vim redis.conf

修改内容如下

    # 任何机器都可以访问，生产环境需改成特点ip访问
    bind 0.0.0.0

    # 密码
    requirepass hc8866

    # 守护进程
    daemonize yes

:wq 保存退出。

5、启动redis

    ./src/redis-server ./redis.conf

启动信息如下

    [redis@iZ2zebthf35ejlps5v87ksZ redis-4.0.6]$ ./src/redis-server ./redis.conf
    9490:C 14 Mar 22:45:12.384 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
    9490:C 14 Mar 22:45:12.384 # Redis version=4.0.6, bits=64, commit=00000000, modified=0, pid=9490, just started
    9490:C 14 Mar 22:45:12.384 # Configuration loaded

6、客户端连接redis

    ./src/redis-cli -p 6379 -a hc8866

连接成功内容如下

    [redis@izbp117mtgmllet9ryobawz redis-4.0.6]$ ./src/redis-cli -p 6379 -a hc8866
    127.0.0.1:6379> keys *
    (empty list or set)
    127.0.0.1:6379>

推荐使用rdm等客户端工具查看redis数据。

六、安装zookeeper

1、切换为zk用户，上传源码包至/home/zk

2、解压

    tar zxvf zookeeper-3.4.6.tar.gz

3、进入zk目录

     cd zookeeper-3.4.6

4、准备启动conf

    cp ./conf/zoo_sample.cfg ./conf/zoo.cfg

5、启动zk

    ./bin/zkServer.sh start

启动信息如下

    [zk@iZ2zebthf35ejlps5v87ksZ zookeeper-3.4.6]$ ./bin/zkServer.sh start
    JMX enabled by default
    Using config: /home/zk/zookeeper-3.4.6/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED

七、部署项目

1、切回应用账户hc，安装git

    sudo yum install git -y

2、安装maven

    sudo yum install maven -y

3、修改maven源为阿里源

    sudo vim /usr/share/maven/conf/settings.xml

在mirrors标签下添加如下内容

    <mirror>
          <id>alimaven</id>
          <name>aliyun maven</name>
          <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
          <mirrorOf>central</mirrorOf>
     </mirror>

4、下载代码

    mkdir hcProject

    cd hcProject

    git clone https://gitee.com/wuxw7/MicroCommunity.git

5、编译代码

    cd /home/hc/hcProject/MicroCommunity

    mvn clean install

6、修改redis配置

打开hcConf项目下所有 application-dev.yml，修改redis账号密码

端口没有修改则不用更换

7、替换ip

打开所有docker-compose.yml，如hcConf\Api\docker\docker-compose.yml

> 这里推荐在windows下用nodePad++的文件查找替换功能，在目录下批量替换

> 如果喜欢在Linux修改的话，可以用sed 批量替换

将如下内容中的ip替换为对应的服务器ip

       extra_hosts:
       - "dev.java110.com:192.168.100.29"
       - "dev.db.java110.com:192.168.100.29"
       - "dev.zk.java110.com:192.168.100.29"
       - "dev.kafka.java110.com:192.168.100.29"
       - "dev.redis.java110.com:192.168.100.29"
       - "api.java110.com:192.168.100.29"

8、上传bin 和 hcConf 至目录/home/hc/hcProject 下



八、后端docker服务启动

1、下载修改后的yaoHcConf文件，解压后得到bin、hcConf目录，上传至hcProject

2、启动reset_all.sh

相比群里的原文件，增加了FrontService与eureka的服务。

> 吴老大说没必要每次都重启eureka服务，想想对于初次接触HC项目的小白们（比如我）来说，还是比价有难度的，所以把eureka加进去了，省事。

    cd /home/hc/hcProject/bin

    chmod +x *.sh

    sudo ./reset_all.sh

3、docker 常用命令

启动后查看服务是否启动成功

    # 查看所有正在运行容器
    sudo docker ps

    # containerId 是容器的ID
    sudo docker stop containerId

     # 查看所有容器
    sudo docker ps -a

    # 查看所有容器ID
    sudo docker ps -a -q

    # stop停止所有容器
    sudo docker stop $(sudo docker ps -a -q)
    sudo docker stop $(sudo docker ps -aq)

    # remove删除所有容器
    sudo docker  rm $(sudo docker ps -a -q)

    # 删除所有镜像
    sudo docker rmi $(sudo docker images -q)

    # 进入某个容器查看
    sudo docker exec -i -t [containerId] /bin/bash
    # 示例
    sudo docker exec -it [76ae97d878eb] /bin/bash


4、异常处理

* 1） network 异常

异常：

> ERROR: Network java110-net declared as external, but could not be found. Please create the network manually using `docker network create java110-net` and try again.

解决方法：

    docker network create java110-net

* 2）特殊字符问题

docker中容器未启动成功

    CONTAINER ID        IMAGE                             COMMAND                  CREATED             STATUS                         PORTS                    NAMES
    6632de00214f        docker_frontserivce               "/root/start_front..."   30 minutes ago      Restarting (1) 2 minutes ago                            frontserivce-1

使用docker logs 查看日志

    sudo docker logs -f --tail=100 76ae97d878eb

报错如下

    standard_init_linux.go:178: exec user process caused "no such file or directory"

先停止了这个服务，然后去服务下（/home/hc/hcProject/app/FrontService）看看是什么问题，怀疑是启动脚本问题

    sudo docker-compose -f /home/hc/hcProject/app/FrontService/docker/docker-compose.yml       down;

发现是frontserivce的docker下的onStart.sh、start_front.sh脚本中出现了^M字符,使用dos2unix工具转换下。

    cd /home/hc/hcProject/app/FrontService/docker

    sudo yum install dos2unix

    sudo dos2unix onStart.sh

    sudo dos2unix ./bin/start_front.sh

启动这个服务

    sudo docker-compose -f /home/hc/hcProject/app/FrontService/docker/docker-compose.yml       up -d --build --force-recreate;

如果没有down掉服务，也可使用重启命令

    sudo docker-compose -f /home/hc/hcProject/app/FrontService/docker/docker-compose.yml       restart;

在使用restart的脚本重启后发现又出现报错了，突然想到是源头脚本出了问题，restart的脚本又copy了一次hcConf下文件。

看了下果然如此，重新去除下特殊字符^M

    vi -b /home/hc/hcProject/hcConf/FrontService/docker/onStart.sh

    sudo dos2unix /home/hc/hcProject/hcConf/FrontService/docker/*.sh

    cd /home/hc/hcProject/bin

    sudo ./restart_all.sh


* 3） yml 格式问题

启动之后又发现问题

    2020-03-19 18:29:24.945 ERROR 8 --- [           main] o.s.boot.SpringApplication               : Application run failed
    java.lang.IllegalStateException: Failed to load property source from location 'classpath:/application-dev.yml'
    ……
    could not find expected ':' in 'reader', line 15, column 1:
    eureka:    ^
	at org.yaml.snakeyaml.scanner.ScannerImpl.stalePossibleSimpleKeys(ScannerImpl.java:466)

明显提示第15行的yml文件报错，查看了下，报错原文如下

    password:hc8866

是因为password第冒号后没有空一格输入密码。

* 4） hosts问题

命运多舛，又出了问题

    DiscoveryClient_FRONT-SERVICE/172.19.0.3:8012: registering service...

这次的问题是因为docker-compose.yml, 因为新版的前后端分离版本升级第关系，群文件的hcConf里没有FrontService，我从MicroCommunity拷贝了一个过去，但是没有启用extra_hosts，还是用第net_works导致。原文件片段如下：

    version: '2'
    services:
       frontserivce:
           container_name: frontserivce-1
           build:
              context: .
              dockerfile: Dockerfile
           restart: always
           ports:
           - "8020:8020"
           volumes:
           - ../target/FrontService.jar:/root/target/FrontService.jar
           networks:
           - java110-net
    #       mem_limit: 1024m
    #       extra_hosts:
    #       - "dev.java110.com:192.168.1.18"
    #       - "dev.db.java110.com:192.168.1.18"
    #       - "dev.zk.java110.com:192.168.1.18"
    #       - "dev.kafka.java110.com:192.168.1.18"
    #       - "dev.redis.java110.com:192.168.1.18"
    #       - "api.java110.com:92.168.1.18"
    networks:
      java110-net:
        external: true

修改后如下

    version: '2'
    services:
       frontserivce:
           container_name: frontserivce-1
           build:
              context: .
              dockerfile: Dockerfile
           restart: always
           ports:
           - "8020:8020"
           volumes:
           - ../target/FrontService.jar:/root/target/FrontService.jar

           mem_limit: 1536m
           extra_hosts:
           - "dev.java110.com:47.96.141.41"
           - "dev.db.java110.com:47.96.141.41"
           - "dev.zk.java110.com:47.96.141.41"
           - "dev.kafka.java110.com:47.96.141.41"
           - "dev.redis.java110.com:47.96.141.41"
           - "api.java110.com:47.96.141.41"

47.96.141.41为docker服务器宿主机地址。

* 5） docker启动脚本问题

发现docker的8012端口启动了，但是其实真正的服务没有启动，log也没有报错，停留在

    2020-03-20 14:22:55.501  INFO 1317 --- [ost-startStop-1] c.netflix.config.DynamicPropertyFactory  : DynamicPropertyFactory is initialized with configuration sources: com.netflix.config.ConcurrentCompositeConfiguration@616da524

进入docker内部的bash看看

    sudo docker exec -it frontserivce-1 /bin/bash

进入bash后，查看进程

    ps -aux

发现并没有java -jar进程，在docker容器内手动启动下试试

    java -jar -Dspring.profiles.active=dev -Xms512m -Xmx1024m target/FrontService.jar

竟然启动起来了，启动后内容如下

    root@9018e70e1220:~# ps -aux
    USER       PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
    root         1  0.0  0.0  21108  1508 ?        Ss   14:28   0:00 /bin/bash /root/start_front.sh dev
    root         8  0.0  0.0   7456   632 ?        S    14:28   0:00 tail -100f front.log
    root        38  0.0  0.0  21324  2148 ?        Ss   14:36   0:00 /bin/bash
    root        66  9.4  4.5 4757012 733244 ?      Sl+  14:41   1:14 java -jar -Dspring.profiles.active=dev -Xms512m -Xmx1024m target/FrontService.jar


估计还是启动脚本有问题，需要回头排查下。

八、前端服务部署

1、down下来前端项目

    git clone  https://gitee.com/java110/MicrCommunityWeb.git

2、安装nodeJs环境

3、打开项目，修改app.js

    app.use('/callComponent',proxy('http://后端服务ip:8012',opts));

修改ip端口为FrontService的服务端口

4、启动

与app.js统计目录下，输入命令编译启动

    npm install

    npm start

打开ip:3000端口，查看前端页面

端口可以修改，在项目的 bin/www 文件中

    var port = normalizePort(process.env.PORT || '3000');





