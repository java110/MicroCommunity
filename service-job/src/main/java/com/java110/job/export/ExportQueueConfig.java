package com.java110.job.export;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExportQueueConfig {


    private void initExportQueue(){
        //启动导出数据线程处理器
        ExportDataExecutor.startExportDataExecutor();
    }


    @Bean
    public ExportQueueConfig exportQueueConfig(){
        ExportQueueConfig exportConfig = new ExportQueueConfig();
        exportQueueConfig().initExportQueue();
        return exportConfig;
    }
}
