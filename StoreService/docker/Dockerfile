FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/StoreService.jar /root/target/

ADD bin/start_store.sh /root/


RUN chmod u+x /root/start_store.sh

CMD ["/root/start_store.sh","dev"]