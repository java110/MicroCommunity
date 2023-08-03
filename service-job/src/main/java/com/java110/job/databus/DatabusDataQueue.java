package com.java110.job.databus;

import com.java110.core.log.LoggerFactory;
import com.java110.dto.data.DatabusQueueDataDto;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DatabusDataQueue {
    private static final Logger log = LoggerFactory.getLogger(DatabusDataQueue.class);

    private static final BlockingQueue<DatabusQueueDataDto> msgs = new LinkedBlockingQueue<DatabusQueueDataDto>(100);

    /**
     * 添加导出数据消息
     *
     * @param databusQueueDataDto
     */
    public static void addMsg(DatabusQueueDataDto databusQueueDataDto) {
        try {

            msgs.offer(databusQueueDataDto, 3, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("写入队列失败", e);
            e.printStackTrace();
        }
    }

    public static DatabusQueueDataDto getData() {
        try {
            return msgs.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
