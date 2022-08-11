## 如何切换为springboot方式启动

#### 为什么要 spring boot 方式启动

HC小区管理系统之前一直是以spring cloud 方式启动 它的优点是等业务量上来后可以 很方便的横向扩展，但是前期对于一些推广的小伙伴来说 也是增加了服务器成本，基于这个原因
我们期间也尝试过单独拉一个版本 出来为单机版 只包含 费用 报修 报表等基础功能，但是这样对于客户难以选择，其二是我们需要维护两套代码 工作量方便也是很大的挑战，
于是通过团队以及推广的小伙伴们的探讨 维护一套代码 但是支持两种启动方式（spring boot 或者spring cloud 方式），目前spring boot 方式启动只需要 2G内存就可以运行
但是生产环境我们还是推荐 您用2核4G内存 服务器运行

#### 切换为springboot方式启动

1、运行 切换脚本

如果是window 请运行 changeBootEnvWindow.bat

如果是mac 或者linux 请运行 changeBootEnvLinux.sh

脚本主要是实现将service服务下的 pom-boot.xml 的内容 写到 pom.xml 文件中

2、跟目录下执行 编译

mvn clean install

mvn clean package

3、spring boot 服务启动

spring boot 我们只关注 springboot 这个服务就可以 service开头的服务可以不用关注 也不用启动，因为在编译时 自动编译到这个服务里面了

你可以 java -jar springboot\target\MicroCommunityBoot.jar 启动
或者
运行 BootApplicationStart.java

4、部署前段访问

前段部署 没有差别

