#### 1. 安装 Java

```
# cd /opt
# wget https://mirrors.huaweicloud.com/java/jdk/8u202-b08/jdk-8u202-linux-x64.tar.gz
# tar zxfv jdk-8u202-linux-x64.tar.gz
# mkdir –p /usr/local/java
# mv /opt/jdk1.8.0_202 /usr/local/java/1.8.0_202
# vim /etc/profile

export JAVA_HOME=/usr/local/java/1.8.0_202
export JRE_HOME=$JAVA_HOME/jre 
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH 
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH

# source /etc/profile
# java -version
java version "1.8.0_202"
Java(TM) SE Runtime Environment (build 1.8.0_202-b08)
Java HotSpot(TM) 64-Bit Server VM (build 25.202-b08, mixed mode)
```

#### 2. 安装 Maven

```
# cd /opt
# wget https://mirror.bit.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
# tar zxfv apache-maven-3.6.3-bin.tar.gz
# mkdir –p /usr/local/maven
# mv apache-maven-3.6.3 /usr/local/maven/3.6.3
# vim /etc/profile

export MAVEN_HOME=/usr/local/maven/3.6.3
export PATH=$MAVEN_HOME/bin:$PATH

# source /etc/profile
# mvn -v
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /usr/local/maven/3.6.3
Java version: 1.8.0_202, vendor: Oracle Corporation, runtime: /usr/local/java/1.8.0_202/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.10.0-1127.el7.x86_64", arch: "amd64", family: "unix"
```

配置镜像

```
# vim /usr/local/maven/3.6.3/conf/settings.xml

<mirrors>
	<mirror>
		<id>nexus-aliyun</id>
		<mirrorOf>central</mirrorOf>
		<name>Nexus aliyun</name>
		<url>http://maven.aliyun.com/nexus/content/groups/public</url>
	</mirror>
</mirrors>
```

#### 3. 安装 ZooKeeper

```
# cd /opt
# wget https://downloads.apache.org/zookeeper/zookeeper-3.4.14/zookeeper-3.4.14.tar.gz
# tar zxfv zookeeper-3.4.14.tar.gz
# mkdir -p /usr/local/zookeeper
# mv zookeeper-3.4.14 /usr/local/zookeeper/3.4.14
```

**注意**：3.5.x 依赖 Java 9

添加用户和目录

```
# useradd -r -s /sbin/nologin zookeeper
# mkdir -p /data/zookeeper
# chown -R zookeeper:zookeeper /data/zookeeper
# mkdir -p /etc/zookeeper
# cp /usr/local/zookeeper/3.4.14/conf/zoo_sample.cfg /etc/zookeeper/zoo.cfg
```

修改 /etc/zookeeper/zoo.cfg

```
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=/data/zookeeper
```

配置环境变量

```
# echo 'export PATH=/usr/local/zookeeper/3.4.14/bin:$PATH' >> /etc/profile
# source /etc/profile
```

配置 systemd

```
# vim /usr/lib/systemd/system/zookeeper.service

[Unit]
Description=ZooKeeper Server
Documentation=https://zookeeper.apache.org/
After=network.target

[Service]
Type=forking
User=zookeeper
Group=zookeeper
Environment=JAVA_HOME=/usr/local/java/1.8.0_202
Environment=ZOO_LOG_DIR=/data/zookeeper/logs
Environment=ZOO_LOG4J_PROP=INFO,CONSOLE
ExecStart=/usr/local/zookeeper/3.4.14/bin/zkServer.sh start /etc/zookeeper/zoo.cfg
ExecStop=/usr/local/zookeeper/3.4.14/bin/zkServer.sh stop /etc/zookeeper/zoo.cfg
KillMode=none
PIDFile=/data/zookeeper/zookeeper_server.pid

[Install]
WantedBy=multi-user.target
```

**注意**: Environment=JAVA_HOME=jdk-install-dir

启动服务器

```
# systemctl daemon-reload
# systemctl start zookeeper
# systemctl enable zookeeper
```

#### 4. 安装 Redis

```
# cd /opt
# wget http://download.redis.io/releases/redis-4.0.14.tar.gz
# tar xzf redis-4.0.14.tar.gz
# cd redis-4.0.14
# make -j4 && make install
# ll /usr/local/bin/redis*
-rwxr-xr-x. 1 root root 2452112 May  2 21:40 /usr/local/bin/redis-benchmark
-rwxr-xr-x. 1 root root 5777688 May  2 21:40 /usr/local/bin/redis-check-aof
-rwxr-xr-x. 1 root root 5777688 May  2 21:40 /usr/local/bin/redis-check-rdb
-rwxr-xr-x. 1 root root 2618136 May  2 21:40 /usr/local/bin/redis-cli
lrwxrwxrwx. 1 root root      12 May  2 21:40 /usr/local/bin/redis-sentinel -> redis-server
-rwxr-xr-x. 1 root root 5777688 May  2 21:40 /usr/local/bin/redis-server
```

添加用户和目录

```
# useradd -r -s /sbin/nologin redis
# mkdir -p /data/redis
# chown -R redis:redis /data/redis
# mkdir -p /etc/redis
# cp redis.conf /etc/redis/
```

修改 redis.conf 

```
# vim /etc/redis/redis.conf 

# If you run Redis from upstart or systemd, Redis can interact with your
# supervision tree. Options:
#   supervised no      - no supervision interaction
#   supervised upstart - signal upstart by putting Redis into SIGSTOP mode
#   supervised systemd - signal systemd by writing READY=1 to $NOTIFY_SOCKET
#   supervised auto    - detect upstart or systemd method based on
#                        UPSTART_JOB or NOTIFY_SOCKET environment variables
# Note: these supervision methods only signal "process is ready."
#       They do not enable continuous liveness pings back to your supervisor.
supervised systemd

# The working directory.
#
# The DB will be written inside this directory, with the filename specified
# above using the 'dbfilename' configuration directive.
#
# The Append Only File will also be created inside this directory.
#
# Note that you must specify a directory here, not a file name.
dir /data/redis

# By default, if no "bind" configuration directive is specified, Redis listens
# for connections from all the network interfaces available on the server.
# It is possible to listen to just one or multiple selected interfaces using
# the "bind" configuration directive, followed by one or more IP addresses.
#
# Examples:
#
# bind 192.168.1.100 10.0.0.1
# bind 127.0.0.1 ::1
#
# ~~~ WARNING ~~~ If the computer running Redis is directly exposed to the
# internet, binding to all the interfaces is dangerous and will expose the
# instance to everybody on the internet. So by default we uncomment the
# following bind directive, that will force Redis to listen only into
# the IPv4 lookback interface address (this means Redis will be able to
# accept connections only from clients running into the same computer it
# is running).
#
# IF YOU ARE SURE YOU WANT YOUR INSTANCE TO LISTEN TO ALL THE INTERFACES
# JUST COMMENT THE FOLLOWING LINE.
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bind 127.0.0.1							# Optional. Such as: 0.0.0.0.

# Require clients to issue AUTH <PASSWORD> before processing any other
# commands.  This might be useful in environments in which you do not trust
# others with access to the host running redis-server.
#
# This should stay commented out for backward compatibility and because most
# people do not need auth (e.g. they run their own servers).
#
# Warning: since Redis is pretty fast an outside user can try up to
# 150k passwords per second against a good box. This means that you should
# use a very strong password otherwise it will be very easy to break.
#
# requirepass password			# Optional.
```

配置 systemd

```
# vim /usr/lib/systemd/system/redis.service

[Unit]
Description=Redis Server
Documentation=https://redis.io/
After=network.target

[Service]
Type=simple
User=redis
Group=redis
ExecStart=/usr/local/bin/redis-server /etc/redis/redis.conf
ExecStop=/usr/local/bin/redis-cli shutdown

[Install]
WantedBy=multi-user.target
```

启动服务器

```
# systemctl daemon-reload
# systemctl start redis
# systemctl enable redis
```

#### 5. 安装 Docker

```
# yum install -y yum-utils device-mapper-persistent-data lvm2
# yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# yum install -y docker
# groupadd docker
# systemctl start docker
# systemctl enable docker
# docker run hello-world
```

安装 docker-compose

```
# curl -L "https://github.com/docker/compose/releases/download/1.25.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
# chmod +x /usr/local/bin/docker-compose
# docker-compose --version
docker-compose version 1.25.5, build 8a1c60f6
```

#### 6. 安装 MySQL

```
# cd /opt
# wget https://nchc.dl.sourceforge.net/project/boost/boost/1.59.0/boost_1_59_0.tar.gz
# wget https://cdn.mysql.com//Downloads/MySQL-5.7/mysql-5.7.30.tar.gz
# tar zxfv boost_1_59_0.tar.gz
# tar zxfv mysql-5.7.30.tar.gz
# yum install -y autoconf automake m4 libtool make cmake bison gcc openssl-devel ncurses-devel
# cmake \
    -DDOWNLOAD_BOOST=1 \
    -DWITH_BOOST=/opt/boost_1_59_0 \
    -DCMAKE_INSTALL_PREFIX=/usr/local/mysql \
    -DMYSQL_DATADIR=/data/mysql/data \
    -DDEFAULT_CHARSET=utf8 \
    -DDEFAULT_COLLATION=utf8_general_ci \
    -DEXTRA_CHARSETS=all
# make -j4 && make install

```

添加用户和目录

```
# useradd -r -s /sbin/nologin mysql
# mkdir -p /data/mysql
# chown mysql:mysql /data/mysql
```

修改 my.cnf

```
# vim /etc/my.cnf

- 关闭自动注释
:set fo-=r 
- 关闭自动缩进
:set noautoindent
- 粘贴下面内容

# ~
# FROM: https://gist.github.com/fevangelou/fb72f36bbe333e059b66.js
#
# Optimized my.cnf configuration for MySQL/MariaSQL (on Ubuntu, CentOS etc. servers)
#
# by Fotis Evangelou, developer of Engintron (engintron.com)
#
# ~ Updated January 2020 ~
#
#
# The settings provided below are a starting point for a 2GB - 4GB RAM server with 2-4 CPU cores.
# If you have different resources available you should adjust accordingly to save CPU, RAM & disk I/O usage.
#
# The settings marked with a specific comment or the word "UPD" (after the value)
# should be adjusted for your system by using database diagnostics tools like:
#
# https://github.com/major/MySQLTuner-perl
# or
# https://github.com/RootService/tuning-primer (supports MySQL up to v5.7)
# ~

[mysql]
port                          	= 3306
socket                          = /var/run/mysqld/mysqld.sock

[mysqld]
# Required Settings
basedir                         = /usr/local/mysql
bind_address                    = 0.0.0.0 # Change to 127.0.0.0 to allow local connections only
datadir                         = /data/mysql
max_allowed_packet              = 256M
max_connect_errors              = 1000000
pid_file                        = /var/run/mysqld/mysqld.pid
port                            = 3306
skip_external_locking
skip_name_resolve
socket                          = /var/run/mysqld/mysqld.sock

# Enable for b/c with databases created in older MySQL/MariaDB versions (e.g. when using null dates)
#sql_mode                       = ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES

tmpdir                          = /tmp
user                            = mysql

# InnoDB Settings
default_storage_engine          = InnoDB
innodb_buffer_pool_instances    = 2     # Use 1 instance per 1GB of InnoDB pool size
innodb_buffer_pool_size         = 2G    # Use up to 70-80% of RAM
innodb_file_per_table           = 1
innodb_flush_log_at_trx_commit  = 0
innodb_flush_method             = O_DIRECT
innodb_log_buffer_size          = 16M
innodb_log_file_size            = 512M
innodb_stats_on_metadata        = 0

#innodb_temp_data_file_path     = ibtmp1:64M:autoextend:max:20G # Control the maximum size for the ibtmp1 file
#innodb_thread_concurrency      = 4     # Optional: Set to the number of CPUs on your system (minus 1 or 2) to better
                                        # contain CPU usage. E.g. if your system has 8 CPUs, try 6 or 7 and check
                                        # the overall load produced by MySQL/MariaDB.
innodb_read_io_threads          = 64
innodb_write_io_threads         = 64

# MyISAM Settings
query_cache_limit               = 4M    # UPD - Option supported by MariaDB & up to MySQL 5.7, remove this line on MySQL 8.x
query_cache_size                = 64M   # UPD - Option supported by MariaDB & up to MySQL 5.7, remove this line on MySQL 8.x
query_cache_type                = 1     # Option supported by MariaDB & up to MySQL 5.7, remove this line on MySQL 8.x

key_buffer_size                 = 32M   # UPD

low_priority_updates            = 1
concurrent_insert               = 2

# Connection Settings
max_connections                 = 100   # UPD

back_log                        = 512
thread_cache_size               = 100
thread_stack                    = 192K

interactive_timeout             = 180
wait_timeout                    = 180

# For MySQL 5.7+ only (disabled by default)
#max_execution_time             = 30000 # Set a timeout limit for SELECT statements (value in milliseconds).
                                        # This option may be useful to address aggressive crawling on large sites,
                                        # but it can also cause issues (e.g. with backups). So use with extreme caution and test!
                                        # More info at: https://dev.mysql.com/doc/refman/5.7/en/server-system-variables.html#sysvar_max_execution_time

# For MariaDB 10.1.1+ only (disabled by default)
#max_statement_time             = 30    # The equivalent of "max_execution_time" in MySQL 5.7+ (set above)
                                        # The variable is of type double, thus you can use subsecond timeout.
                                        # For example you can use value 0.01 for 10 milliseconds timeout.
                                        # More info at: https://mariadb.com/kb/en/aborting-statements/

# Buffer Settings
join_buffer_size                = 4M    # UPD
read_buffer_size                = 3M    # UPD
read_rnd_buffer_size            = 4M    # UPD
sort_buffer_size                = 4M    # UPD

# Table Settings
# In systemd managed systems like Ubuntu 16.04+ or CentOS 7+, you need to perform an extra action for table_open_cache & open_files_limit
# to be overriden (also see comment next to open_files_limit).
# E.g. for MySQL 5.7, please check: https://dev.mysql.com/doc/refman/5.7/en/using-systemd.html
# and for MariaDB check: https://mariadb.com/kb/en/library/systemd/
table_definition_cache          = 40000 # UPD
table_open_cache                = 40000 # UPD
open_files_limit                = 60000 # UPD - This can be 2x to 3x the table_open_cache value or match the system's
                                        # open files limit usually set in /etc/sysctl.conf or /etc/security/limits.conf
                                        # In systemd managed systems this limit must also be set in:
                                        # /etc/systemd/system/mysqld.service.d/override.conf (for MySQL 5.7+) and
                                        # /etc/systemd/system/mariadb.service.d/override.conf (for MariaDB)

max_heap_table_size             = 128M
tmp_table_size                  = 128M

# Search Settings
ft_min_word_len                 = 3     # Minimum length of words to be indexed for search results

# Logging
log_error                       = /data/mysql/mysql_error.log
log_queries_not_using_indexes   = 1
long_query_time                 = 5
slow_query_log                  = 1     # Disabled for production
slow_query_log_file             = /data/mysql/mysql_slow.log

[mysqldump]
# Variable reference
# For MySQL 5.7: https://dev.mysql.com/doc/refman/5.7/en/mysqldump.html
# For MariaDB:   https://mariadb.com/kb/en/library/mysqldump/
quick
quote_names
max_allowed_packet              = 64M
```

配置环境变量

```
# echo 'PATH=/usr/local/mysql/bin:$PATH' >> /etc/profile
# source /etc/profile
```

初始化数据库

```
# /usr/local/mysql/bin/mysqld --initialize --user=mysql
```

获取初始密码

```
# cat /data/mysql/mysql_error.log | grep root@localhost
```

启动服务器

```
# cp /opt/mysql-5.7.30/support-files/mysql.server /usr/local/bin/
# chmod ugo+x /usr/local/bin/mysql.server
# mkdir -p /var/run/mysqld
# chown -R mysql:mysql /var/run/mysqld
# mysql.server start
```

登陆服务器

```
# mysql -uroot -p'NT!gwh.%c21N'
```

修改 root 密码

```
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
```

创建数据库

```
mysql> CREATE DATABASE `TT` CHARACTER SET `utf8` COLLATE `utf8_general_ci`;
mysql> CREATE DATABASE `hc_community` CHARACTER SET `utf8` COLLATE `utf8_general_ci`;
```

创建用户

```
mysql> CREATE USER 'TT'@'%' IDENTIFIED WITH mysql_native_password BY 'password';
mysql> GRANT ALL PRIVILEGES ON `TT`.* TO 'TT'@'%';
mysql> CREATE USER 'hc_community'@'%' IDENTIFIED WITH mysql_native_password BY 'password';
mysql> GRANT ALL PRIVILEGES ON `hc_community`.* TO 'hc_community'@'%';
mysql> FLUSH PRIVILEGES;
```

配置 systemd

```
# vim /usr/lib/systemd/system/mysql.service

[Unit]
Description=MySQL Server
Documentation=https://www.mysql.com/
After=network.target

[Service]
Type=forking
ExecStartPre=/usr/bin/mkdir -p /var/run/mysqld
ExecStartPre=/usr/bin/chown -R mysql:mysql /var/run/mysqld
ExecStart=/usr/local/bin/mysql.server start
ExecReload=/usr/local/bin/mysql.server reload
ExecStop=/usr/local/bin/mysql.server stop

[Install]
WantedBy=multi-user.target
```

启动服务器

```
# systemctl daemon-reload
# systemctl start mysql
# systemctl enable mysql
```

#### 7. 安装 HC 后端

导入数据库

```
# mysql -uTT -ppassword
mysql> use TT;
mysql> source /opt/TT(分离版)20200420.sql;
```

```
# mysql -uhc_communit -ppassword
mysql> use hc_community;
mysql> source /opt/hc_community(分离版)20200420.sql;
```

安装环境

```
# useradd hc
# usermod -aG docker hc
# mkdir /data/hc
# chown -R hc:hc /data/hc
# su hc
$ cd /data/hc
$ git clone https://gitee.com/wuxw7/MicroCommunity.git
```

修改配置

```
# vim /etc/hosts

192.168.0.114 dev.java110.com
192.168.0.114 dev.db.java110.com
192.168.0.114 dev.zk.java110.com
192.168.0.114 dev.kafka.java110.com
192.168.0.114 dev.redis.java110.com
192.168.0.114 api.java110.com
```

备份配置

* 使用下面脚本备份配置文件

```
$ cd /data/hc/MicroCommunity
$ vim backup_config.sh

#!/bin/sh

mkdir -p ../MicroCommunity-config

sourceDirs=(Api/docker \
CodeService/src/main/resources \
CommentService/src/main/resources \
CommonService/docker \
CommonService/src/main/resources \
CommunityService/docker \
CommunityService/src/main/resources \
FeeService/docker \
FeeService/src/main/resources \
FrontService/docker \
HardwareAdapationService/docker \
HardwareAdapationService/src/main/resources \
JobService/src/main/resources \
LogService/src/main/resources \
OrderService/docker \
OrderService/src/main/resources \
ReportService/docker \
ReportService/src/main/resources \
StoreService/docker \
StoreService/src/main/resources \
UserService/docker \
UserService/src/main/resources \
eureka/docker)

targetPrefix="../MicroCommunity-config"

targetDirs=(Api/ \
 CodeService/src/main/ \
 CommentService/src/main/ \
 CommonService/ \
 CommonService/src/main/ \
 CommunityService/ \
 CommunityService/src/main/ \
 FeeService/ \
 FeeService/src/main/ \
 FrontService/ \
 HardwareAdapationService/ \
 HardwareAdapationService/src/main/ \
 JobService/src/main/ \
 LogService/src/main/ \
 OrderService/ \
 OrderService/src/main/ \
 ReportService/ \
 ReportService/src/main/ \
 StoreService/ \
 StoreService/src/main/ \
 UserService/ \
 UserService/src/main/ \
 eureka/)

sz=${#targetDirs[@]}

for ((i=0; i<$sz; i+=1))
do
    echo "copy" ${sourceDirs[i]} "to" $targetPrefix/${targetDirs[i]}
    mkdir -p $targetPrefix/${targetDirs[i]}
    cp -R ${sourceDirs[i]} $targetPrefix/${targetDirs[i]}
done

```

```
$ sh backup_config.sh
$ tree /data/hc/MicroCommunity-config
├── Api
│   └── docker
│       ├── docker-compose.yml
├── CodeService
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── CommentService
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── CommonService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── CommunityService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── eureka
│   └── docker
│       ├── docker-compose.yml
├── FeeService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── FrontService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
├── HardwareAdapationService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── JobService
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── LogService
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
├── OrderService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── ReportService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
├── StoreService
│   ├── docker
│   │   ├── docker-compose.yml
│   └── src
│       └── main
│           └── resources
│               ├── application-dev.yml
│               ├── dataSource.yml
└── UserService
    ├── docker
    │   ├── docker-compose.yml
    └── src
        └── main
            └── resources
                ├── application-dev.yml
                ├── dataSource.yml
```

修改配置

* 按下面方式修改上面的所有配置文件

```
$ vim OrderService/docker/docker-compose.yml

- 替换本机地址
:%s/114/本机地址/g

version: '2'
services:
   centerservice:
       container_name: orderservice-1
       build:
          context: .
          dockerfile: Dockerfile
       restart: always
       ports:
       - "8001:8001"
       volumes:
       - ../target/OrderService.jar:/root/target/OrderService.jar
       extra_hosts:
         - "dev.java110.com:192.168.0.114"
         - "dev.db.java110.com:192.168.0.114"
         - "dev.zk.java110.com:192.168.0.114"
         - "dev.kafka.java110.com:192.168.0.114"
         - "dev.redis.java110.com:192.168.0.114"
         - "api.java110.com:192.168.0.114"
```

```
$ vim OrderService/src/main/resources/dataSource.yml

- 替换 MySQL 用户和密码

dataSources:
  ds0: !!com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://dev.db.java110.com:3306/hc_community?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: mysql_username
    password: mysql_password
  ds1: !!com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://dev.db.java110.com:3306/TT?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: mysql_username
    password: mysql_username
```

```
$ vim OrderService/src/main/resources/application-dev.yml

- 替换 MySQL 用户和密码
- 替换 Redis 密码

spring:
  redis:
    password: redis_password
  datasource:
    url: jdbc:mysql://dev.db.java110.com:3306/TT?useUnicode=true&characterEncoding=utf-8&useSSL=false
    password: mysql_password
    username: mysql_username
```

恢复配置

* 使用下面脚本恢复配置文件

```
$ cd /data/hc/MicroCommunity
$ vim restore_config.sh

#!/bin/sh

outputPrefix="../MicroCommunity-config"

configDirs="Api \
CodeService \
CommentService \
CommonService \
CommunityService \
FeeService \
FrontService \
HardwareAdapationService \
JobService \
LogService \
OrderService \
ReportService \
StoreService \
UserService \
eureka"

for configDir in $configDirs
do
    echo "copy" $outputPrefix/$configDir "to" $configDir
    cp -R $outputPrefix/$configDir .
done
```

```
$ sh restore_config.sh
```

配置脚本

```
$ cd /data/hc/MicroCommunity
```

```
$ vim rebuild_services.sh

#!/bin/sh

mvn clean install -Dmaven.test.skip=true

args="up --build --no-start --force-recreate"
services="eureka Api OrderService CommunityService CommonService StoreService UserService FrontService FeeService"

for service in $services
do
    docker-compose -f $service/docker/docker-compose.yml $args
done
```

```
$ vim restart_containers.sh

#!/bin/sh

sh reaction_containers.sh restart
```

```
$ vim stop_containers.sh

#!/bin/sh

sh reaction_containers.sh stop
```

```
$ vim down_containers.sh

#!/bin/sh

sh reaction_containers.sh down
```

```
$ vim reaction_containers.sh

#!/bin/sh

args=$1
services="eureka Api OrderService CommunityService StoreService UserService FrontService FeeService"

case "$args" in
    "restart")
        ;;
    "stop")
        ;;
    "down")
        ;;
    *)
        echo "Usage: sh reaction_containers.sh restart/stop/down"
        exit
        ;;
esac

for service in $services
do
    sh reaction_container.sh $service $args
done

docker ps
```

```
$ vim reaction_container.sh

#!/bin/sh

service=$1
args=$2

if [ "$service" == "" ] || [ "$args" == "" ]
then
    echo "Usage: sh reaction_container.sh service restart/stop/down"
    exit
fi

docker-compose -f $service/docker/docker-compose.yml $args
```

构建镜像

```
$ sh rebuild_services.sh
```

启动容器

```
# 启动所有容器
$ sh restart_containers.sh
# 启动单个容器
$ sh reaction_container.sh Api restart
```

停止容器

```
# 停止所有容器
$ sh stop_containers.sh
# 停止单个容器
$ sh reaction_container.sh Api stop
```

卸载容器

```
# 卸载所有容器
$ sh down_containers.sh
# 卸载单个容器
$ sh reaction_container.sh Api down
```

#### 8. 安装 HC 前端

安装环境

```
# curl -sL https://rpm.nodesource.com/setup_10.x | bash -
# yum install –y nodejs
# su hc
$ cd /data/hc
$ git clone https://gitee.com/java110/MicroCommunityWeb.git
```

启动前端

```
$ cd /data/hc/MicroCommunityWeb
$ npm install
$ npm start
```


