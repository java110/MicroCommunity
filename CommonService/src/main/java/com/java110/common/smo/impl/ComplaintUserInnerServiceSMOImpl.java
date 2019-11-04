package com.java110.common.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.complaint.IComplaintUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.entity.audit.AuditUser;
import com.java110.utils.util.Assert;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;
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
        variables.put("userId", complaintDto.getCurrentUserId());
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("complaint", complaintDto.getComplaintId(), variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);

        complaintDto.setProcessInstanceId(processInstanceId);
        //第一个节点自动提交
        autoFinishFirstTask(complaintDto);
        return complaintDto;
    }

    /**
     * 自动提交第一步
     */
    private void autoFinishFirstTask(ComplaintDto complaintDto){
        Task task = null;
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(complaintDto.getCurrentUserId()).active();
        List<Task> todoList = query.list();//获取申请人的待办任务列表
        for (Task tmp : todoList) {
            if(tmp.getProcessInstanceId().equals(complaintDto.getProcessInstanceId())){
                task = tmp;//获取当前流程实例，当前申请人的待办任务
                break;
            }
        }
        Assert.notNull(task, "未找到当前用户任务userId = "+ complaintDto.getCurrentUserId());
        complaintDto.setTaskId(task.getId());
        complaintDto.setAuditCode("10000");
        complaintDto.setAuditMessage("提交");
        completeTask(complaintDto);

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
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey("complaint");
        ;
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = null;
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage(user.getPage(), user.getRow());
        } else {
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

    public boolean completeTask(@RequestBody ComplaintDto complaintDto) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(complaintDto.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(complaintDto.getCurrentUserId());
        taskService.addComment(complaintDto.getTaskId(), processInstanceId, complaintDto.getAuditMessage());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", complaintDto.getAuditCode());
        taskService.complete(complaintDto.getTaskId(), variables);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (pi == null) {
            return true;
        }
        return false;
    }

    public List<AuditMessageDto> getAuditMessage(@RequestBody ComplaintDto complaintDto) {

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(complaintDto.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        List<AuditMessageDto> auditMessageDtos = new ArrayList<>();
        if (comments == null || comments.size() < 1) {
            return auditMessageDtos;
        }
        AuditMessageDto messageDto = null;
        for (Comment comment : comments) {
            messageDto = new AuditMessageDto();
            messageDto.setCreateTime(comment.getTime());
            messageDto.setUserId(comment.getUserId());
            messageDto.setMessage(comment.getFullMessage());
        }

        return auditMessageDtos;
    }


}
