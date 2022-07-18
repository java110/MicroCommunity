package com.java110.common.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.store.IComplaintInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.entity.audit.AuditUser;
import com.java110.utils.cache.MappingCache;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private final static Logger logger = LoggerFactory.getLogger(ComplaintUserInnerServiceSMOImpl.class);

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;


    /**
     * 启动流程
     *
     * @return
     */
    public ComplaintDto startProcess(@RequestBody ComplaintDto complaintDto) {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        //variables.put("complaintDto", complaintDto);
        variables.put("startUserId", complaintDto.getCurrentUserId());
        String key = getWorkflowDto(complaintDto.getCommunityId());

        if(StringUtil.isEmpty(key)){
            return null;
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, complaintDto.getComplaintId(), variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);

        complaintDto.setProcessInstanceId(processInstanceId);
        //第一个节点自动提交
        //autoFinishFirstTask(complaintDto);
        return complaintDto;
    }

    private String getWorkflowDto(String communityId) {
        //开启流程
        //WorkflowDto.DEFAULT_PROCESS + workflowDto.getFlowId()
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setFlowType(WorkflowDto.FLOW_TYPE_COMPLAINT);
        workflowDto.setCommunityId(communityId);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);

        if(workflowDtos == null || workflowDtos.size() < 1){
            return "";
        }

        WorkflowDto tmpWorkflowDto = workflowDtos.get(0);
        if (StringUtil.isEmpty(tmpWorkflowDto.getProcessDefinitionKey())) {
            return "";
        }
        return WorkflowDto.DEFAULT_PROCESS + tmpWorkflowDto.getFlowId();
    }

    /**
     * 自动提交第一步
     */
    private void autoFinishFirstTask(ComplaintDto complaintDto) {
        Task task = null;
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(complaintDto.getCurrentUserId()).active();
        List<Task> todoList = query.list();//获取申请人的待办任务列表
        for (Task tmp : todoList) {
            if (tmp.getProcessInstanceId().equals(complaintDto.getProcessInstanceId())) {
                task = tmp;//获取当前流程实例，当前申请人的待办任务
                break;
            }
        }
        Assert.notNull(task, "未找到当前用户任务userId = " + complaintDto.getCurrentUserId());
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
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getCommunityId()));
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
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getCommunityId()));
        ;
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = null;
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage((user.getPage() - 1) * user.getRow(), user.getRow());
        } else {
            list = query.list();
        }

        List<String> complaintIds = new ArrayList<>();
        Map<String, String> taskBusinessKeyMap = new HashMap<>();
        for (Task task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            complaintIds.add(business_key);
            taskBusinessKeyMap.put(business_key, task.getId());
        }

        if (complaintIds == null || complaintIds.size() == 0) {
            return new ArrayList<>();
        }

        //查询 投诉信息
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStoreId(user.getStoreId());
        complaintDto.setCommunityId(user.getCommunityId());
        complaintDto.setComplaintIds(complaintIds.toArray(new String[complaintIds.size()]));
        List<ComplaintDto> tmpComplaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);

        for (ComplaintDto tmpComplaintDto : tmpComplaintDtos) {
            tmpComplaintDto.setTaskId(taskBusinessKeyMap.get(tmpComplaintDto.getComplaintId()));
        }
        return tmpComplaintDtos;
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
                .processDefinitionKey(getWorkflowDto(user.getCommunityId()))
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
    public List<ComplaintDto> getUserHistoryTasks(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(getWorkflowDto(user.getCommunityId()))
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

        List<String> complaintIds = new ArrayList<>();
        for (HistoricTaskInstance task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            complaintIds.add(business_key);
        }

        //查询 投诉信息
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStoreId(user.getStoreId());
        complaintDto.setCommunityId(user.getCommunityId());
        complaintDto.setComplaintIds(complaintIds.toArray(new String[complaintIds.size()]));
        List<ComplaintDto> tmpComplaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);
        return tmpComplaintDtos;
    }


    public boolean completeTask(@RequestBody ComplaintDto complaintDto) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(complaintDto.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(complaintDto.getCurrentUserId());
        taskService.addComment(complaintDto.getTaskId(), processInstanceId, complaintDto.getAuditMessage());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", complaintDto.getAuditCode());
        variables.put("currentUserId", complaintDto.getCurrentUserId());
        variables.put("flag", "1200".equals(complaintDto.getAuditCode()) ? "false" : "true");
        variables.put("startUserId", complaintDto.getStartUserId());
        //taskService.setAssignee(complaintDto.getTaskId(),complaintDto.getCurrentUserId());
        //taskService.addCandidateUser(complaintDto.getTaskId(), complaintDto.getCurrentUserId());
        //taskService.claim(complaintDto.getTaskId(), complaintDto.getCurrentUserId());
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

    /**
     * 删除指定任务
     */
    public boolean deleteTask(@RequestBody ComplaintDto complaintDto){
        String taskId = complaintDto.getTaskId();

        Task task = taskService.createTaskQuery().taskId(complaintDto.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        logger.info("删除id为：{}，投诉建议任务");
        if (null != pi) {
            //该流程实例未结束的
            runtimeService.deleteProcessInstance(processInstanceId, "删除任务");
        }
        historyService.deleteHistoricProcessInstance(processInstanceId);
        return true;
    }


    /**
     * 获取任务当前处理人
     *
     * @param complaintDto
     * @return
     */
    public ComplaintDto getTaskCurrentUser(@RequestBody ComplaintDto complaintDto) {

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(complaintDto.getComplaintId()).list();

        if (tasks == null || tasks.size() == 0) {
            complaintDto.setCurrentUserId("");
            complaintDto.setCurrentUserName("");
            complaintDto.setCurrentUserTel("");
            return complaintDto;
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
        complaintDto.setCurrentUserId(userIds);
        complaintDto.setCurrentUserName(userNames);
        complaintDto.setCurrentUserTel(userTels);
        complaintDto.setTaskId(taskIds);
        return complaintDto;

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

    public IComplaintInnerServiceSMO getComplaintInnerServiceSMOImpl() {
        return complaintInnerServiceSMOImpl;
    }

    public void setComplaintInnerServiceSMOImpl(IComplaintInnerServiceSMO complaintInnerServiceSMOImpl) {
        this.complaintInnerServiceSMOImpl = complaintInnerServiceSMOImpl;
    }
}
