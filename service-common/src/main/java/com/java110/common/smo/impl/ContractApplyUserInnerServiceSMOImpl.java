package com.java110.common.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IContractApplyUserInnerServiceSMO;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
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
public class ContractApplyUserInnerServiceSMOImpl extends BaseServiceSMO implements IContractApplyUserInnerServiceSMO {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;


    /**
     * 启动流程
     *
     * @return
     */
    public ContractDto startProcess(@RequestBody ContractDto contractDto) {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("contractDto", contractDto);
        variables.put("userId", contractDto.getCurrentUserId());
        variables.put("startUserId", contractDto.getCurrentUserId());
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getWorkflowDto(contractDto.getStoreId()),contractDto.getContractId(), variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);

        contractDto.setProcessInstanceId(processInstanceId);

        return contractDto;

    }

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    public long getUserTaskCount(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getStoreId()));
        query.taskAssignee(user.getUserId());
        return query.count();
    }

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    public List<ContractDto> getUserTasks(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getStoreId()));
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = null;
        if (user.getPage() >= 1) {
            user.setPage(user.getPage() - 1);
        }
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage(user.getPage(), user.getRow());
        } else {
            list = query.list();
        }

        List<String> contractIds = new ArrayList<>();
        Map<String, String> taskBusinessKeyMap = new HashMap<>();
        for (Task task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            contractIds.add(business_key);
            taskBusinessKeyMap.put(business_key, task.getId());
        }

        if (contractIds == null || contractIds.size() == 0) {
            return new ArrayList<>();
        }

        //查询 投诉信息
        ContractDto contractDto = new ContractDto();
        contractDto.setStoreId(user.getStoreId());
        contractDto.setContractIds(contractIds.toArray(new String[contractIds.size()]));
        List<ContractDto> tmpContractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        for (ContractDto tmpContractDto : tmpContractDtos) {
            tmpContractDto.setTaskId(taskBusinessKeyMap.get(tmpContractDto.getContractId()));
        }
        return tmpContractDtos;
    }

    public boolean agreeCompleteTask(@RequestBody ContractDto contractDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", contractDto.getAuditCode());
        taskService.complete(contractDto.getTaskId(), variables);
        return true;
    }

    public boolean refuteCompleteTask(@RequestBody ContractDto contractDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", contractDto.getAuditCode());
        taskService.complete(contractDto.getTaskId(), variables);
        return true;
    }

    /**
     * 审核 当前任务
     *
     * @param contractDto 资源订单
     * @return
     */
    public boolean complete(@RequestBody ContractDto contractDto) {
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(contractDto.getTaskId());


        return true;
    }


    private String getWorkflowDto(String storeId) {
        //开启流程
        //WorkflowDto.DEFAULT_PROCESS + workflowDto.getFlowId()
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setFlowType(WorkflowDto.FLOW_TYPE_CONTRACT_APPLY);
        workflowDto.setStoreId(storeId);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);
        Assert.listOnlyOne(workflowDtos, "未找到 投诉建议流程或找到多条，请在物业账号系统管理下流程管理中配置流程");

        WorkflowDto tmpWorkflowDto = workflowDtos.get(0);
        if (StringUtil.isEmpty(tmpWorkflowDto.getProcessDefinitionKey())) {
            throw new IllegalArgumentException("合同起草续签流程还未部署");
        }
        return WorkflowDto.DEFAULT_PROCESS + tmpWorkflowDto.getFlowId();
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
                .processDefinitionKey(getWorkflowDto(user.getStoreId()))
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
    public List<ContractDto> getUserHistoryTasks(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(getWorkflowDto(user.getStoreId()))
                .taskAssignee(user.getUserId())
                .finished();
//        if (!StringUtil.isEmpty(user.getAuditLink()) && "START".equals(user.getAuditLink())) {
//            historicTaskInstanceQuery.taskName("complaint");
//        } else if (!StringUtil.isEmpty(user.getAuditLink()) && "AUDIT".equals(user.getAuditLink())) {
//            historicTaskInstanceQuery.taskName("complaitDealUser");
//        }

        Query query = historicTaskInstanceQuery.orderByHistoricTaskInstanceStartTime().desc();

        List<HistoricTaskInstance> list = null;
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage((user.getPage() - 1) * user.getRow(), user.getRow());
        } else {
            list = query.list();
        }

        List<String> contractIds = new ArrayList<>();
        Map<String, String> taskBusinessKeyMap = new HashMap<>();

        for (HistoricTaskInstance task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            contractIds.add(business_key);
            taskBusinessKeyMap.put(business_key, task.getId());
        }

        //查询 投诉信息
        ContractDto contractDto = new ContractDto();
        contractDto.setStoreId(user.getStoreId());
        contractDto.setContractIds(contractIds.toArray(new String[contractIds.size()]));
        List<ContractDto> tmpContractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        for (ContractDto tmpContractDto : tmpContractDtos) {
            tmpContractDto.setTaskId(taskBusinessKeyMap.get(tmpContractDto.getContractId()));
        }
        return tmpContractDtos;
    }


    public boolean completeTask(@RequestBody ContractDto contractDto) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(contractDto.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(contractDto.getCurrentUserId());
        taskService.addComment(contractDto.getTaskId(), processInstanceId, contractDto.getAuditMessage());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", contractDto.getAuditCode());
        variables.put("currentUserId", contractDto.getCurrentUserId());
        variables.put("flag", "1200".equals(contractDto.getAuditCode()) ? "false" : "true");
        variables.put("startUserId", contractDto.getStartUserId());
        taskService.complete(contractDto.getTaskId(), variables);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (pi == null) {
            return true;
        }
        return false;
    }

}
