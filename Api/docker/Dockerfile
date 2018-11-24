FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/Api.jar /root/target/

ADD bin/start_api.sh /root/


RUN chmod u+x /root/start_api.sh

CMD ["/root/start_api.sh","dev -Dcache"]