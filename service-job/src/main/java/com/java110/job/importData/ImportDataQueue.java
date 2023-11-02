package com.java110.job.importData;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.data.ImportDataDto;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ImportDataQueue {
    private static final Logger log = LoggerFactory.getLogger(ImportDataQueue.class);

    private static final BlockingQueue<ImportDataDto> msgs = new LinkedBlockingQueue<ImportDataDto>(100);

    /**
     * 添加导出数据消息
     *
     * @param importDataDto
     */
    public static void addMsg(ImportDataDto importDataDto) {
        try {

            msgs.offer(importDataDto, 3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("写入队列失败", e);
            e.printStackTrace();
        }
    }

    public static ImportDataDto getData() {
        try {
            return msgs.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
