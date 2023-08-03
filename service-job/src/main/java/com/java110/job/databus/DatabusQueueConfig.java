package com.java110.job.databus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队里 可以少量小区时 使用如果 小区数量比较大时 可以选择切换为mq
 *
 */
@Configuration
public class DatabusQueueConfig {


    @Bean
    public DatabusQueue databusQueue(){
        DatabusQueue databusQueue = new DatabusQueue();
        databusQueue.initExportQueue();
        return databusQueue;
    }
}
