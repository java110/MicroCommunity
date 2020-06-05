package com.java110.api.listener.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.job.ITaskInnerServiceSMO;
import com.java110.core.smo.job.ITaskAttrInnerServiceSMO;
import com.java110.dto.task.TaskDto;
import com.java110.dto.taskAttr.TaskAttrDto;
import com.java110.result.ResultVo;
import com.java110.utils.constant.ServiceCodeTaskConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listTasksListener")
public class ListTasksListener extends AbstractServiceApiListener {

    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;

    @Autowired
    private ITaskAttrInnerServiceSMO taskAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeTaskConstant.LIST_TASKS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ITaskInnerServiceSMO getTaskInnerServiceSMOImpl() {
        return taskInnerServiceSMOImpl;
    }

    public void setTaskInnerServiceSMOImpl(ITaskInnerServiceSMO taskInnerServiceSMOImpl) {
        this.taskInnerServiceSMOImpl = taskInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        TaskDto taskDto = BeanConvertUtil.covertBean(reqJson, TaskDto.class);

        int count = taskInnerServiceSMOImpl.queryTasksCount(taskDto);

        List<TaskDto> taskDtos = null;

        if (count > 0) {
            taskDtos = taskInnerServiceSMOImpl.queryTasks(taskDto);
            freshTaskAttr(taskDtos);
        } else {
            taskDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, taskDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 查询属性
     *
     * @param taskDtos
     */
    private void freshTaskAttr(List<TaskDto> taskDtos) {

        for (TaskDto taskDto : taskDtos) {
            TaskAttrDto taskAttrDto = new TaskAttrDto();
            taskAttrDto.setTaskId(taskDto.getTaskId());
            List<TaskAttrDto> taskAttrDtos = taskAttrInnerServiceSMOImpl.queryTaskAttrs(taskAttrDto);
            taskDto.setTaskAttr(taskAttrDtos);
        }


    }
}
