FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/service-comment.jar /root/target/

ADD bin/start_comment.sh /root/


RUN chmod u+x /root/start_comment.sh

CMD ["/root/start_comment.sh","dev"]