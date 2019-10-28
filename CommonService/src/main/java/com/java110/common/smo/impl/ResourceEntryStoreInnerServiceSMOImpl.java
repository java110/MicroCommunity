package com.java110.common.smo.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.common.IResourceEntryStoreInnerServiceSMO;
import com.java110.dto.resourceStore.ResourceOrderDto;
import com.java110.entity.audit.AuditUser;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service("resourceEntryStoreSMOImpl")
@RestController
public class ResourceEntryStoreInnerServiceSMOImpl extends BaseServiceSMO implements IResourceEntryStoreInnerServiceSMO {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    /**
     * 启动流程
     *
     * @return
     */
    public ResourceOrderDto startProcess(@RequestBody ResourceOrderDto resourceOrderDto) {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("resourceOrderDto", resourceOrderDto);
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("resourceEntry", variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);

        resourceOrderDto.setProcessInstanceId(processInstanceId);

        return resourceOrderDto;
    }

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    public List<ResourceOrderDto> getUserTasks(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery();
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = query.list();

        List<ResourceOrderDto> resourceOrderDtos = new ArrayList<>();

        for (Task task : list) {
            String id = task.getId();
            //System.out.println("tasks:" + JSONObject.toJSONString(task));
            ResourceOrderDto resourceOrderDto = (ResourceOrderDto) taskService.getVariable(id, "resourceOrderDto");
            resourceOrderDto.setTaskId(id);
            resourceOrderDtos.add(resourceOrderDto);
        }
        return resourceOrderDtos;
    }

    public boolean agreeCompleteTask(@RequestBody ResourceOrderDto resourceOrderDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditFlag", true);
        taskService.complete(resourceOrderDto.getTaskId(), variables);
        return true;
    }

    public boolean refuteCompleteTask(@RequestBody ResourceOrderDto resourceOrderDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditFlag", true);
        taskService.complete(resourceOrderDto.getTaskId(), variables);
        return true;
    }

    /**
     * 审核 当前任务
     *
     * @param resourceOrderDto 资源订单
     * @return
     */
    public boolean complete(@RequestBody ResourceOrderDto resourceOrderDto) {
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(resourceOrderDto.getTaskId());
        return true;
    }


}
