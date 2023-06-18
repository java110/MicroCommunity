package com.java110.job.importData;

public class ImportQueue {

    public void initExportQueue(){
        //启动导出数据线程处理器
        ImportDataExecutor.startExportDataExecutor();
    }
}
