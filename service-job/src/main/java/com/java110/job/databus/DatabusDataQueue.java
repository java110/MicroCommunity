package com.java110.job.databus;

import com.java110.core.factory.GenerateCodeFactory;
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
    public static void addMsg(DatabusQueueDataDto databusQueueDataDto) throws Exception {
        msgs.offer(databusQueueDataDto, 3, TimeUnit.SECONDS);

    }

    public static DatabusQueueDataDto getData() throws Exception {
        return msgs.take();

    }

}
