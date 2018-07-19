FROM docker pull registry.cn-beijing.aliyuncs.com/sxd/ubuntu-java8:1.0
MAINTAINER wuxw <wuxw7@asiainfo.com>

ADD ../target/eureka.jar /usr/local/

ADD ../bin/start_eureka.sh /usr/local/


RUN chmod u+x /usr/local/start_eureka.sh

CMD ["/usr/local/start_eureka.sh"]