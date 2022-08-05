package com.java110.dev.cmd.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.task.TaskDto;
import com.java110.intf.job.ITaskInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "task.stopTask")
public class StopTaskCmd extends Cmd {
    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskName");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(reqJson.getString("taskId"));
        List<TaskDto> taskDtos = taskInnerServiceSMOImpl.queryTasks(taskDto);
        ResultVo resultVo = null;
        if (taskDtos == null || taskDtos.size() < 1) {
            resultVo = new ResultVo(ResultVo.ORDER_ERROR, "传入任务ID错误");
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

            context.setResponseEntity(responseEntity);
            return;
        }
        int state = taskInnerServiceSMOImpl.stopTask(taskDtos.get(0));

        if(state >0){
            resultVo = new ResultVo(ResultVo.CODE_OK, "停止成功");
        }else{
            resultVo = new ResultVo(ResultVo.ORDER_ERROR, "停止失败");
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
