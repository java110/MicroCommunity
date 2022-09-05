## 如何切换为springcloud方式启动

#### 主机要求

4核16G内存


#### 切换为springcloud方式启动

1、运行 切换脚本

如果是window 请运行 changeCloudEnvWindow.bat

如果是mac 或者linux 请运行 changeCloudEnvLinux.sh

脚本主要是实现将service服务下的 pom-cloud.xml 的内容 写到 pom.xml 文件中

2、跟目录下执行 编译

mvn clean install

mvn clean package

3、spring cloud 服务启动

spring cloud  service-开头的服务都需要启动

你可以 java -jar service-*\target\service-*.jar 启动 注意*换成相应服务的名称


4、部署前段访问

前段部署 没有差别

