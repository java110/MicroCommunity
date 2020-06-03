package com.java110.job.task;

import com.java110.dto.task.TaskDto;
import com.java110.job.quartz.TaskSystemQuartz;
import org.springframework.stereotype.Component;

/**
 * @ClassName TransalateOwnerPhotoToMachine
 * @Description TODO
 * @Author wuxw
 * @Date 2020/6/3 20:59
 * @Version 1.0
 * add by wuxw 2020/6/3
 **/
@Component
public class TranslateOwnerPhotoToMachine extends TaskSystemQuartz {
    @Override
    protected void process(TaskDto taskDto) throws Exception {

    }
}
