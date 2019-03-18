FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/ConsoleService.jar /root/target/

ADD bin/start_console.sh /root/


RUN chmod u+x /root/start_console.sh

CMD ["/root/start_console.sh","dev"]