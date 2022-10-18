package com.java110.job.export;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.ExportDataDto;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ExportDataQueue {
    private static final Logger log = LoggerFactory.getLogger(ExportDataQueue.class);

    private static final BlockingQueue<ExportDataDto> msgs = new LinkedBlockingQueue<ExportDataDto>(100);

    /**
     * 添加导出数据消息
     *
     * @param exportDataDto
     */
    public static void addMsg(ExportDataDto exportDataDto) {
        try {

            msgs.offer(exportDataDto,3, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("写入队列失败", e);
            e.printStackTrace();
        }
    }

    public static ExportDataDto getData() {
        try {
            return msgs.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
