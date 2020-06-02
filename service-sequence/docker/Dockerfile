FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/service-sequence.jar /root/target/

ADD bin/start_code.sh /root/


RUN chmod u+x /root/start_code.sh

CMD ["/root/start_code.sh","dev"]