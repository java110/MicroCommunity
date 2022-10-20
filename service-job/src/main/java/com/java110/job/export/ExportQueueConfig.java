package com.java110.job.export;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExportQueueConfig {


    @Bean
    public ExportQueue exportQueue(){
        ExportQueue exportQueue = new ExportQueue();
        exportQueue.initExportQueue();
        return exportQueue;
    }
}
