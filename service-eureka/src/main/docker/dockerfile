FROM java:8
MAINTAINER wuxw
VOLUME /tmp
ADD microservice-discovery-eureka-0.0.1-SNAPSHOT.jar /root/app.jar
RUN bash -c 'touch /root/app.jar'
EXPOSE 8761
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/root/app.jar"]