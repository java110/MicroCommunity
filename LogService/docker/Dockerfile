FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/LogService.jar /root/target/

ADD bin/start_log.sh /root/


RUN chmod u+x /root/start_log.sh

CMD ["/root/start_log.sh","dev"]