FROM registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <wuxw7@asiainfo.com>

ADD target/CenterService.jar /root/target/

ADD bin/start_eureka.sh /root/


RUN chmod u+x /root/start_centerService.sh

CMD ["/root/start_centerService.sh dev"]