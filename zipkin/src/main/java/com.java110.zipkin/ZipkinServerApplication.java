package com.java110.zipkin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@SpringBootApplication
@EnableZipkinStreamServer//配置可以作为zipkinserver
public class ZipkinServerApplication {

    private final static Logger logger = LoggerFactory.getLogger(ZipkinServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServerApplication.class,args);
    }
}