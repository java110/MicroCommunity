FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/WebService.jar /root/target/

ADD bin/start_web.sh /root/


RUN chmod u+x /root/start_web.sh

CMD ["/root/start_web.sh","dev"]