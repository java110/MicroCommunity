package com.java110.common.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import com.java110.dto.businessDatabus.CustomBusinessDatabusDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IStorehouseInnerServiceSMO;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
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

import static com.java110.dto.storehouse.StorehouseDto.SH_TYPE_GROUP;

//@Service("resourceEntryStoreSMOImpl")
@RestController
public class AllocationStorehouseUserInnerServiceSMOImpl extends BaseServiceSMO implements IAllocationStorehouseUserInnerServiceSMO {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private IStorehouseInnerServiceSMO iStorehouseInnerServiceSMO;


    /**
     * 启动流程
     *
     * @return
     */
    public AllocationStorehouseApplyDto startProcess(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("allocationStorehouseApplyDto", allocationStorehouseApplyDto);
        variables.put("userId", allocationStorehouseApplyDto.getCurrentUserId());
        variables.put("startUserId", allocationStorehouseApplyDto.getCurrentUserId());
        variables.put("nextUserId", allocationStorehouseApplyDto.getNextUserId());
        //查询调拨源仓库是集团仓库还是小区仓库，小区仓库走被调拨流程审批
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShId(allocationStorehouseApplyDto.getShId());
        List<StorehouseDto> storehouseDtoList = iStorehouseInnerServiceSMO.queryStorehouses(storehouseDto);
        StorehouseDto storehouseDto1 = new StorehouseDto();
        String communityId = null;
        if (storehouseDtoList != null && storehouseDtoList.size() > 0) {
            storehouseDto1 = storehouseDtoList.get(0);
        }
        if (SH_TYPE_GROUP.equals(storehouseDto1.getShType())) {//集团仓库
            communityId = "9999";
        } else {//小区仓库
            communityId = storehouseDto1.getShObjId();
        }
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getWorkflowDto(allocationStorehouseApplyDto.getStoreId(), communityId), allocationStorehouseApplyDto.getApplyId(), variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);
        String processDefinitionId = processInstance.getProcessDefinitionId();
        String businessKey = processInstance.getBusinessKey();
        //获取下级处理人id
        PurchaseApplyDto purchaseDto = new PurchaseApplyDto();
        purchaseDto.setActRuTaskId(processInstanceId);
        purchaseDto.setProcDefId(processDefinitionId);
        purchaseDto.setBusinessKey(businessKey);
        List<PurchaseApplyDto> actRuTaskUserIds = purchaseApplyInnerServiceSMOImpl.getActRuTaskUserId(purchaseDto);
        allocationStorehouseApplyDto.setProcessInstanceId(processInstanceId);
        if (actRuTaskUserIds != null && actRuTaskUserIds.size() > 0) {
            for (PurchaseApplyDto purchaseApply : actRuTaskUserIds) {
                String actRuTaskUserId = purchaseApply.getTaskUserId();
                MachineRecordPo machineRecordPo = new MachineRecordPo();
                machineRecordPo.setApplyOrderId(businessKey);
                machineRecordPo.setPurchaseUserId(actRuTaskUserId);
                //传送databus
                dataBusInnerServiceSMOImpl.customExchange(CustomBusinessDatabusDto.getInstance(
                        BusinessTypeConstant.BUSINESS_TYPE_DATABUS_ALLOCATION_STOREHOUSE_APPLY, BeanConvertUtil.beanCovertJson(machineRecordPo)));
            }
        }
        return allocationStorehouseApplyDto;
    }

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    public long getUserTaskCount(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        List<String> workflowlist = getWorkflowDto1(user.getStoreId());
        TaskQuery query = taskService.createTaskQuery().processDefinitionKeyIn(workflowlist);
//        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getStoreId(), user.getCommunityId()));
        query.taskAssignee(user.getUserId());
        return query.count();
    }

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    public List<AllocationStorehouseApplyDto> getUserTasks(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKeyIn(getWorkflowDto1(user.getStoreId()));
        query.taskAssignee(user.getUserId());
        query.orderByTaskCreateTime().desc();
        List<Task> list = null;
        if (user.getPage() >= 1) {
            user.setPage((user.getPage() - 1) * user.getRow());
        }
        if (user.getPage() != PageDto.DEFAULT_PAGE) {
            list = query.listPage(user.getPage(), user.getRow());
        } else {
            list = query.list();
        }

        List<String> appIyIds = new ArrayList<>();
        Map<String, String> taskBusinessKeyMap = new HashMap<>();
        for (Task task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            appIyIds.add(business_key);
            taskBusinessKeyMap.put(business_key, task.getId());
        }

        if (appIyIds == null || appIyIds.size() == 0) {
            return new ArrayList<>();
        }

        //查询 投诉信息
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        allocationStorehouseApplyDto.setStoreId(user.getStoreId());
        allocationStorehouseApplyDto.setApplyIds(appIyIds.toArray(new String[appIyIds.size()]));
        List<AllocationStorehouseApplyDto> tmpAllocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        for (AllocationStorehouseApplyDto tmpAllocationStorehouseApplyDto : tmpAllocationStorehouseApplyDtos) {
            tmpAllocationStorehouseApplyDto.setTaskId(taskBusinessKeyMap.get(tmpAllocationStorehouseApplyDto.getApplyId()));
        }
        return tmpAllocationStorehouseApplyDtos;
    }

    public boolean agreeCompleteTask(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", allocationStorehouseApplyDto.getAuditCode());
        taskService.complete(allocationStorehouseApplyDto.getTaskId(), variables);
        return true;
    }

    public boolean refuteCompleteTask(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", allocationStorehouseApplyDto.getAuditCode());
        taskService.complete(allocationStorehouseApplyDto.getTaskId(), variables);
        return true;
    }

    /**
     * 审核 当前任务
     *
     * @param allocationStorehouseApplyDto 资源订单
     * @return
     */
    public boolean complete(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(allocationStorehouseApplyDto.getTaskId());


        return true;
    }

    private String getWorkflowDto(String storeId, String communityId) {
        //开启流程
        //WorkflowDto.DEFAULT_PROCESS + workflowDto.getFlowId()
        WorkflowDto workflowDto = new WorkflowDto();
        if (!StringUtil.isEmpty(communityId) && "9999".equals(communityId)) {
            workflowDto.setFlowType(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE);
        } else {
            workflowDto.setFlowType(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO);
        }
        workflowDto.setStoreId(storeId);
        workflowDto.setCommunityId(communityId);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);
        Assert.listOnlyOne(workflowDtos, "未找到 调拨流程或找到多条，请在物业账号系统管理下流程管理中配置流程");
        WorkflowDto tmpWorkflowDto = workflowDtos.get(0);
        if (StringUtil.isEmpty(tmpWorkflowDto.getProcessDefinitionKey())) {
            throw new IllegalArgumentException("调拨流程流程还未部署");
        }
        return WorkflowDto.DEFAULT_PROCESS + tmpWorkflowDto.getFlowId();
    }

    private List<String> getWorkflowDto1(String storeId) {

        //开启流程
        //WorkflowDto.DEFAULT_PROCESS + workflowDto.getFlowId()
        WorkflowDto workflowDto = new WorkflowDto();
        String[] flowTypes = new String[]{WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE, WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO};
        workflowDto.setFlowTypes(flowTypes);
        workflowDto.setStoreId(storeId);
        List<WorkflowDto> workflowDtos = workflowInnerServiceSMOImpl.queryWorkflows(workflowDto);
        if (workflowDtos != null && workflowDtos.size() == 0) {
            throw new IllegalArgumentException("未找到 调拨流程，请在物业账号系统管理下流程管理中配置流程!");
        }
        List<String> flowIdList = new ArrayList<String>();
        for (WorkflowDto workflowDto1 : workflowDtos) {
            if (StringUtil.isEmpty(workflowDto1.getProcessDefinitionKey()) && WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE.equals(workflowDto1.getFlowType())) {
                throw new IllegalArgumentException("小区编码=" + workflowDto1.getCommunityId() + "的物品调拨流程还未部署");
            }
            if (StringUtil.isEmpty(workflowDto1.getProcessDefinitionKey()) && WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO.equals(workflowDto1.getFlowType())) {
                throw new IllegalArgumentException("小区编码=" + workflowDto1.getCommunityId() + "的物品被调拨还未部署");
            }
            flowIdList.add(WorkflowDto.DEFAULT_PROCESS + workflowDto1.getFlowId());
        }
        return flowIdList;
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
                .processDefinitionKeyIn(getWorkflowDto1(user.getStoreId()))
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
    public List<AllocationStorehouseApplyDto> getUserHistoryTasks(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKeyIn(getWorkflowDto1(user.getStoreId()))
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

        List<String> appIyIds = new ArrayList<>();
        Map<String, String> taskBusinessKeyMap = new HashMap<>();

        for (HistoricTaskInstance task : list) {
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            appIyIds.add(business_key);
            taskBusinessKeyMap.put(business_key, task.getId());
        }

        //查询 投诉信息
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        allocationStorehouseApplyDto.setStoreId(user.getStoreId());
        allocationStorehouseApplyDto.setApplyIds(appIyIds.toArray(new String[appIyIds.size()]));
        List<AllocationStorehouseApplyDto> tmpAllocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);

        for (AllocationStorehouseApplyDto tmpAllocationStorehouseApplyDto : tmpAllocationStorehouseApplyDtos) {
            tmpAllocationStorehouseApplyDto.setTaskId(taskBusinessKeyMap.get(tmpAllocationStorehouseApplyDto.getApplyId()));
        }
        return tmpAllocationStorehouseApplyDtos;
    }


    public boolean completeTask(@RequestBody AllocationStorehouseApplyDto allocationStorehouseApplyDto) {
        //获取状态标识
        String noticeState = allocationStorehouseApplyDto.getNoticeState();
        //获取审批状态
        String auditCode = allocationStorehouseApplyDto.getAuditCode();
        //获取拒绝理由
        String auditMessage = "";
        if (!StringUtil.isEmpty(auditCode) && auditCode.equals("1200")) {
            auditMessage = allocationStorehouseApplyDto.getAuditMessage();
        }
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(allocationStorehouseApplyDto.getTaskId()).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(allocationStorehouseApplyDto.getCurrentUserId());
        taskService.addComment(allocationStorehouseApplyDto.getTaskId(), processInstanceId, allocationStorehouseApplyDto.getAuditMessage());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("auditCode", allocationStorehouseApplyDto.getAuditCode());
        variables.put("currentUserId", allocationStorehouseApplyDto.getCurrentUserId());
        variables.put("flag", "1200".equals(allocationStorehouseApplyDto.getAuditCode()) ? "false" : "true");
        variables.put("startUserId", allocationStorehouseApplyDto.getStartUserId());
        taskService.complete(allocationStorehouseApplyDto.getTaskId(), variables);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (pi == null) {
            return true;
        }
        String rootProcessInstanceId = pi.getId();
        String processDefinitionId = pi.getProcessDefinitionId();
        String businessKey = pi.getBusinessKey();
        //获取下级处理人id
        PurchaseApplyDto purchaseDto = new PurchaseApplyDto();
        purchaseDto.setActRuTaskId(rootProcessInstanceId);
        purchaseDto.setProcDefId(processDefinitionId);
        purchaseDto.setBusinessKey(businessKey);
        List<PurchaseApplyDto> actRuTaskUserIds = purchaseApplyInnerServiceSMOImpl.getActRuTaskUserId(purchaseDto);
        if (actRuTaskUserIds != null && actRuTaskUserIds.size() > 0) {
            for (PurchaseApplyDto purchaseApply : actRuTaskUserIds) {
                String actRuTaskUserId = purchaseApply.getTaskUserId();
                MachineRecordPo machineRecordPo = new MachineRecordPo();
                machineRecordPo.setApplyOrderId(businessKey);
                machineRecordPo.setPurchaseUserId(actRuTaskUserId);
                machineRecordPo.setNoticeState(noticeState);
                machineRecordPo.setAuditMessage(auditMessage);
                //传送databus
                dataBusInnerServiceSMOImpl.customExchange(CustomBusinessDatabusDto.getInstance(
                        BusinessTypeConstant.BUSINESS_TYPE_DATABUS_ALLOCATION_STOREHOUSE_APPLY, BeanConvertUtil.beanCovertJson(machineRecordPo)));
            }
        }
        return false;
    }

}
