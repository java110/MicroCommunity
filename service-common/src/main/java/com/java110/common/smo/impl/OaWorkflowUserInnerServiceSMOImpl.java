package com.java110.common.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IOaWorkflowUserInnerServiceSMO;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.query.Query;
import org.activiti.engine.runtime.ProcessInstance;
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
public class OaWorkflowUserInnerServiceSMOImpl extends BaseServiceSMO implements IOaWorkflowUserInnerServiceSMO {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;


    /**
     * 启动流程
     *
     * @return
     */
    public JSONObject startProcess(@RequestBody JSONObject reqJson) {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        //variables.put("reqJson", reqJson);
        variables.put("startUserId", reqJson.getString("createUserId"));
        variables.put("nextUserId", reqJson.getString("createUserId"));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getWorkflowDto(reqJson.getString("flowId")), reqJson.getString("id"), variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);
        reqJson.put("processInstanceId", processInstanceId);
        //第一个节点自动提交
        //autoFinishFirstTask(reqJson);
        return reqJson;
    }

    private String getWorkflowDto(String flowId) {
        return WorkflowDto.DEFAULT_PROCESS + flowId;
    }

    /**
     * 自动提交第一步
     */
    private void autoFinishFirstTask(JSONObject reqJson) {
        Task task = null;
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(reqJson.getString("createUserId")).active();
        List<Task> todoList = query.list();//获取申请人的待办任务列表
        for (Task tmp : todoList) {
            if (tmp.getProcessInstanceId().equals(reqJson.getString("processInstanceId"))) {
                task = tmp;//获取当前流程实例，当前申请人的待办任务
                break;
            }
        }
        Assert.notNull(task, "未找到当前用户任务userId = " + reqJson.getString("createUserId"));
        reqJson.put("taskId", task.getId());
        reqJson.put("auditCode", "10000");
        reqJson.put("auditMessage", "提交");
        completeTask(reqJson);
    }

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    public long getUserTaskCount(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getProcessDefinitionKey()));
        query.taskAssignee(user.getUserId());
        return query.count();
    }

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    public List<JSONObject> getUserTasks(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getProcessDefinitionKey()));
        ;
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = null;
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage((user.getPage() - 1) * user.getRow(), user.getRow());
        } else {
            list = query.list();
        }
        JSONObject taskBusinessKeyMap = null;
        List<JSONObject> tasks = new ArrayList<>();
        for (Task task : list) {
            taskBusinessKeyMap = new JSONObject();
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            taskBusinessKeyMap.put(business_key, task.getId());
            taskBusinessKeyMap.put("id", business_key);
            tasks.add(taskBusinessKeyMap);
        }
        return tasks;
    }


    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    public long getUserHistoryTaskCount(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();
//        Query query = historyService.createHistoricTaskInstanceQuery()
//                .processDefinitionKey("complaint")
//                .taskAssignee(user.getUserId());

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(getWorkflowDto(user.getProcessDefinitionKey()))
                .taskAssignee(user.getUserId())
                .finished();
        if (!StringUtil.isEmpty(user.getAuditLink()) && "START".equals(user.getAuditLink())) {
            historicTaskInstanceQuery.taskName("complaint");
        } else if (!StringUtil.isEmpty(user.getAuditLink()) && "AUDIT".equals(user.getAuditLink())) {
            historicTaskInstanceQuery.taskName("complaitDealUser");
        }

        Query query = historicTaskInstanceQuery;

        return query.count();
    }

    /**
     * 获取用户审批的任务
     *
     * @param user 用户信息
     */
    public List<JSONObject> getUserHistoryTasks(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(getWorkflowDto(user.getProcessDefinitionKey()))
                .taskAssignee(user.getUserId())
                .finished();
        if (!StringUtil.isEmpty(user.getAuditLink()) && "START".equals(user.getAuditLink())) {
            historicTaskInstanceQuery.taskName("complaint");
        } else if (!StringUtil.isEmpty(user.getAuditLink()) && "AUDIT".equals(user.getAuditLink())) {
            historicTaskInstanceQuery.taskName("complaitDealUser");
        }

        Query query = historicTaskInstanceQuery.orderByHistoricTaskInstanceStartTime().desc();

        List<HistoricTaskInstance> list = null;
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage((user.getPage() - 1) * user.getRow(), user.getRow());
        } else {
            list = query.list();
        }
        JSONObject taskBusinessKeyMap = null;
        List<JSONObject> tasks = new ArrayList<>();
        List<String> complaintIds = new ArrayList<>();
        for (HistoricTaskInstance task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            taskBusinessKeyMap.put(business_key, task.getId());
            tasks.add(taskBusinessKeyMap);
        }
        return tasks;
    }


    public boolean completeTask(@RequestBody JSONObject reqJson) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(reqJson.getString("taskId")).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(reqJson.getString("createUserId"));
        taskService.addComment(reqJson.getString("taskId"), processInstanceId, reqJson.getString("auditMessage"));
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", reqJson.getString("auditCode"));
        variables.put("currentUserId", reqJson.getString("createUserId"));
        variables.put("flag", "1200".equals(reqJson.getString("auditCode")) ? "false" : "true");
        variables.put("startUserId", reqJson.getString("startUserId"));
        taskService.complete(reqJson.getString("taskId"), variables);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (pi == null) {
            return true;
        }
        return false;
    }

    public List<AuditMessageDto> getAuditMessage(@RequestBody JSONObject reqJson) {

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(reqJson.getString("taskId")).singleResult();
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

    /**
     * 获取任务当前处理人
     *
     * @param reqJson
     * @return
     */
    public JSONObject getTaskCurrentUser(@RequestBody JSONObject reqJson) {

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(reqJson.getString("id")).list();

        if (tasks == null || tasks.size() == 0) {
            reqJson.put("currentUserId", "");
            reqJson.put("currentUserName", "");
            reqJson.put("currentUserTel", "");
            return reqJson;
        }
        String userIds = "";
        String userNames = "";
        String userTels = "";
        String taskIds = "";
        for (Task task : tasks) {
            String userId = task.getAssignee();
            taskIds += (task.getId() + "/");
            List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(new String[]{userId});
            if (users == null || users.size() == 0) {
                continue;
            }
            userIds += (userId + "/");
            userNames += (users.get(0).getName() + "/");
            userTels += (users.get(0).getTel() + "/");
        }
        userIds = userIds.endsWith("/") ? userIds.substring(0, userIds.length() - 1) : userIds;
        userNames = userNames.endsWith("/") ? userNames.substring(0, userNames.length() - 1) : userNames;
        userTels = userTels.endsWith("/") ? userTels.substring(0, userTels.length() - 1) : userTels;
        taskIds = taskIds.endsWith("/") ? taskIds.substring(0, taskIds.length() - 1) : taskIds;

        reqJson.put("currentUserId", userIds);
        reqJson.put("currentUserName", userNames);
        reqJson.put("currentUserTel", userTels);
        reqJson.put("taskId", taskIds);
        return reqJson;

    }


    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

}
