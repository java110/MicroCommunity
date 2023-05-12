package com.java110.job.cmd.job;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.task.TaskDto;
import com.java110.intf.dev.ITaskV1InnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 手工运行job
 */
@Java110Cmd(serviceCode = "job.runJob")
public class RunJobCmd extends Cmd {

    @Autowired
    private ITaskV1InnerServiceSMO taskV1InnerServiceSMOImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(reqJson.getString("taskId"));
        List<TaskDto> taskDtos = taskV1InnerServiceSMOImpl.queryTasks(taskDto);

        Assert.listOnlyOne(taskDtos,"任务不存在");



        TaskSystemQuartz taskSystemQuartz = (TaskSystemQuartz) ApplicationContextFactory.getBean(taskDtos.get(0).getClassBean());
        try {
            taskSystemQuartz.startTask(taskDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CmdException(e.getMessage());
        }

    }
}
