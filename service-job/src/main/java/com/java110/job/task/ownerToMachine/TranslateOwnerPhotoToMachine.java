package com.java110.job.task.ownerToMachine;

import com.java110.dto.task.TaskDto;
import com.java110.job.quartz.TaskSystemQuartz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName TransalateOwnerPhotoToMachine
 * @Description TODO 传输业主图片到门禁 任务
 * @Author wuxw
 * @Date 2020/6/3 20:59
 * @Version 1.0
 * add by wuxw 2020/6/3
 **/
@Component
public class TranslateOwnerPhotoToMachine extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(TranslateOwnerPhotoToMachine.class);

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        logger.debug("任务在执行" + taskDto.toString());

    }
}
