package com.java110.api.listener.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.job.ITaskInnerServiceSMO;
import com.java110.dto.task.TaskDto;
import com.java110.vo.ResultVo;
import com.java110.utils.constant.ServiceCodeTaskConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("stopTaskListener")
public class StopTaskListener extends AbstractServiceApiPlusListener {

    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskName");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
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
        return;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeTaskConstant.STOP_TASK;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
