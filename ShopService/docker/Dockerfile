FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <928255095@qq.com>

ADD target/ShopService.jar /root/target/

ADD bin/start_shop.sh /root/


RUN chmod u+x /root/start_shop.sh

CMD ["/root/start_shop.sh","dev"]