## 编码生成服务（ID生成）

### 1、说明

微小区平台的 主键生成全部交CodeService(编码生成服务) 同一生成，依赖redis，kafka，zookeeper，java8开源工具

### 2、开发工具

java1.8 + idea/eclipse + mysql

### 3、服务安装说明

a、安装mysql，kafka，redis,git ,maven （详细安装请Google或百度）

b、git clone https://github.com/java110/MicroCommunity.git 下载代码

c、在相应服务下（如：CenterService 服务） src/main/application.yml 中修改mysql地址、用户名和密码、kafka地址、redis地址

d、进入MicroCommunity 目录 执行 mvn clean install 打包

e、启动

（1） 启动 eureka 命令为 java -jar eureka.jar
（2） 启动 CodeService 命令为 java -jar CodeService.jar

f、协议 访问[这里](MicroCommunity/wiki/系统ID生成协议)


