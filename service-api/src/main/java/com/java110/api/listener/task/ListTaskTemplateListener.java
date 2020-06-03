package com.java110.api.listener.task;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.task.ITaskInnerServiceSMO;
import com.java110.dto.task.TaskDto;
import com.java110.dto.task.TaskTemplateDto;
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
 * 查询任务模板
 */
@Java110Listener("listTasksListener")
public class ListTaskTemplateListener extends AbstractServiceApiListener {

    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeTaskConstant.LIST_TASK_TEMPLATE;
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

        TaskTemplateDto taskTemplateDto = BeanConvertUtil.covertBean(reqJson, TaskTemplateDto.class);

        int count = taskInnerServiceSMOImpl.queryTaskTemplateCount(taskTemplateDto);

        List<TaskTemplateDto> taskTemplateDtos = null;

        if (count > 0) {
            taskTemplateDtos = taskInnerServiceSMOImpl.queryTaskTemplate(taskTemplateDto);
        } else {
            taskTemplateDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, taskTemplateDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
