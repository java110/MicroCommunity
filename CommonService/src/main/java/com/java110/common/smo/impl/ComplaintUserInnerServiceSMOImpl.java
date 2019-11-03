package com.java110.common.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.complaint.IComplaintUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.complaint.ComplaintDto;
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
public class ComplaintUserInnerServiceSMOImpl extends BaseServiceSMO implements IComplaintUserInnerServiceSMO {

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
    public ComplaintDto startProcess(@RequestBody ComplaintDto complaintDto) {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("complaintDto", complaintDto);
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("complaint", variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);

        complaintDto.setProcessInstanceId(processInstanceId);

        //处理流程任务
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(complaintDto.getTaskId());


        return complaintDto;
    }

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    public long getUserTaskCount(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey("complaint");
        query.taskAssignee(user.getUserId());
        return query.count();
    }

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    public List<ComplaintDto> getUserTasks(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey("complaint");;
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = null;
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage(user.getPage(), user.getRow());
        }else{
            list = query.list();
        }

        List<ComplaintDto> complaintDtos = new ArrayList<>();

        for (Task task : list) {
            String id = task.getId();
            //System.out.println("tasks:" + JSONObject.toJSONString(task));
            ComplaintDto complaintDto = (ComplaintDto) taskService.getVariable(id, "complaintDto");
            complaintDto.setTaskId(id);
            complaintDtos.add(complaintDto);
        }
        return complaintDtos;
    }

    public boolean agreeCompleteTask(@RequestBody ComplaintDto complaintDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", complaintDto.getAuditCode());
        taskService.complete(complaintDto.getTaskId(), variables);
        return true;
    }

    public boolean refuteCompleteTask(@RequestBody ComplaintDto complaintDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", complaintDto.getAuditCode());
        taskService.complete(complaintDto.getTaskId(), variables);
        return true;
    }

    /**
     * 审核 当前任务
     *
     * @param complaintDto 资源订单
     * @return
     */
    public boolean complete(@RequestBody ComplaintDto complaintDto) {
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(complaintDto.getTaskId());
        return true;
    }


}
