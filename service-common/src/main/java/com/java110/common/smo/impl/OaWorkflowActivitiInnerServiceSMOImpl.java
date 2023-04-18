package com.java110.common.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.oaWorkflow.OaWorkflowDataDto;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.oaWorkflowData.OaWorkflowDataPo;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
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
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

//@Service("resourceEntryStoreSMOImpl")
@RestController
public class OaWorkflowActivitiInnerServiceSMOImpl extends BaseServiceSMO implements IOaWorkflowActivitiInnerServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(OaWorkflowActivitiInnerServiceSMOImpl.class);
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;


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
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("createUserId"));
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(users, "用户不存在");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getWorkflowDto(reqJson.getString("flowId")), reqJson.getString("id"), variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        // System.out.println("流程开启成功.......实例流程id:" + processInstanceId);
        reqJson.put("processInstanceId", processInstanceId);
        //第一个节点自动提交
        //autoFinishFirstTask(reqJson);
        //刷入扩展表
        OaWorkflowDataPo oaWorkflowDataPo = null;
        oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataPo.setFlowId(reqJson.getString("flowId"));
        oaWorkflowDataPo.setContext(reqJson.getString("auditMessage"));
        oaWorkflowDataPo.setDataId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
        oaWorkflowDataPo.setEvent(OaWorkflowDataDto.EVENT_COMMIT);
        oaWorkflowDataPo.setPreDataId("-1");
        oaWorkflowDataPo.setStaffId(reqJson.getString("createUserId"));
        oaWorkflowDataPo.setStaffName(users.get(0).getName());
        oaWorkflowDataPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataInnerServiceSMOImpl.saveOaWorkflowData(oaWorkflowDataPo);
        return reqJson;
    }

    private String getWorkflowDto(String flowId) {
        return WorkflowDto.DEFAULT_PROCESS + flowId;
    }

    /**
     * 自动提交第一步
     */
    public boolean autoFinishFirstTask(@RequestBody JSONObject reqJson) {
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
        return completeTask(reqJson);
    }

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    public long getUserTaskCount(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getFlowId()));
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
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(getWorkflowDto(user.getFlowId()));

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
            taskBusinessKeyMap.put("taskId", task.getId());
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
                .processDefinitionKey(getWorkflowDto(user.getFlowId()))
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
                .processDefinitionKey(getWorkflowDto(user.getFlowId()))
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
            taskBusinessKeyMap = new JSONObject();
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            taskBusinessKeyMap.put(business_key, task.getId());
            taskBusinessKeyMap.put("taskId", task.getId());
            taskBusinessKeyMap.put("id", business_key);
            tasks.add(taskBusinessKeyMap);
        }

        return tasks;
    }

    /**
     * 查询用户任务数
     *
     * @param user{ userId:''
     *              processDefinitionkeys:[]
     *              }
     * @return
     */
    public long getDefinitionKeysUserTaskCount(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKeyIn(user.getProcessDefinitionKeys());
        query.taskAssignee(user.getUserId());
        return query.count();
    }

    /**
     * 获取用户任务
     *
     * @param user 用户信息{
     *             processDefinitionKeys:[],
     *
     * }
     */
    public List<JSONObject> getDefinitionKeysUserTasks(@RequestBody AuditUser user) {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKeyIn(user.getProcessDefinitionKeys());

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
            taskBusinessKeyMap.put("taskId", task.getId());
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
    public long getDefinitionKeysUserHistoryTaskCount(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();
//        Query query = historyService.createHistoricTaskInstanceQuery()
//                .processDefinitionKey("complaint")
//                .taskAssignee(user.getUserId());

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKeyIn(user.getProcessDefinitionKeys())
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
    public List<JSONObject> getDefinitionKeysUserHistoryTasks(@RequestBody AuditUser user) {
        HistoryService historyService = processEngine.getHistoryService();

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKeyIn(user.getProcessDefinitionKeys())
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
            taskBusinessKeyMap = new JSONObject();
            String processInstanceId = task.getProcessInstanceId();
            //3.使用流程实例，查询
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //4.使用流程实例对象获取BusinessKey
            String business_key = pi.getBusinessKey();
            taskBusinessKeyMap.put(business_key, task.getId());
            taskBusinessKeyMap.put("taskId", task.getId());
            taskBusinessKeyMap.put("id", business_key);
            tasks.add(taskBusinessKeyMap);
        }

        return tasks;
    }

    @Java110Transactional
    public boolean completeTask(@RequestBody JSONObject reqJson) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(reqJson.getString("taskId")).singleResult();
        if (task == null) {
            throw new IllegalArgumentException("任务已处理");
        }

        //判断是否为结束流程
//        if ("1500".equals(reqJson.getString("auditCode"))) {
//            doTaskFinish(reqJson);
//        } else {
//            //扩展 工作流功能
//            doTaskAuditAgree(reqJson);
//        }

        String processInstanceId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(reqJson.getString("nextUserId"));
        taskService.addComment(reqJson.getString("taskId"), processInstanceId, reqJson.getString("auditMessage"));
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("nextUserId", reqJson.getString("nextUserId"));
        variables.put("auditCode", reqJson.getString("auditCode"));
        taskService.complete(reqJson.getString("taskId"), variables);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (pi == null) {
            doTaskFinish(reqJson);
            return true;
        }
        //扩展 工作流功能
        doTaskAuditAgree(reqJson);
        return false;
    }

    private void doTaskFinish(JSONObject reqJson) {
        OaWorkflowDataDto oaWorkflowDataDto = new OaWorkflowDataDto();
        oaWorkflowDataDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataDto.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataDto.setPage(1);
        oaWorkflowDataDto.setRow(1);
        oaWorkflowDataDto.setHis("N");
        List<OaWorkflowDataDto> oaWorkflowDataDtos = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatas(oaWorkflowDataDto);

        if (oaWorkflowDataDtos == null || oaWorkflowDataDtos.size() < 1) {
            return;
        }
        //修改 当前 为完成
        OaWorkflowDataPo oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setDataId(oaWorkflowDataDtos.get(0).getDataId());
        oaWorkflowDataPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setContext(reqJson.getString("auditMessage"));
        oaWorkflowDataInnerServiceSMOImpl.updateOaWorkflowData(oaWorkflowDataPo);
    }

    /**
     * 处理 审核状态
     *
     * @param reqJson
     */
    private void doTaskAuditAgree(JSONObject reqJson) {
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("nextUserId"));
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(users, "用户不存在");

        String preDataId = "-1";
        //查询当前节点
        OaWorkflowDataDto oaWorkflowDataDto = new OaWorkflowDataDto();
        oaWorkflowDataDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataDto.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataDto.setPage(1);
        oaWorkflowDataDto.setRow(1);
        List<OaWorkflowDataDto> oaWorkflowDataDtos = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatas(oaWorkflowDataDto);

        OaWorkflowDataPo oaWorkflowDataPo = null;
        if (oaWorkflowDataDtos == null || oaWorkflowDataDtos.size() < 1) {
            oaWorkflowDataPo = new OaWorkflowDataPo();
            oaWorkflowDataPo.setBusinessKey(reqJson.getString("id"));
            oaWorkflowDataPo.setFlowId(reqJson.getString("flowId"));
            oaWorkflowDataPo.setContext("");
            oaWorkflowDataPo.setDataId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
            oaWorkflowDataPo.setEvent(OaWorkflowDataDto.EVENT_COMMIT);
            oaWorkflowDataPo.setPreDataId(preDataId);
            oaWorkflowDataPo.setStaffId(reqJson.getString("nextUserId"));
            oaWorkflowDataPo.setStaffName(users.get(0).getName());
            oaWorkflowDataPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            oaWorkflowDataPo.setStoreId(reqJson.getString("storeId"));
            oaWorkflowDataInnerServiceSMOImpl.saveOaWorkflowData(oaWorkflowDataPo);
            return;
        }
        //修改 当前 为完成
        oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setDataId(oaWorkflowDataDtos.get(0).getDataId());
        oaWorkflowDataPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setContext(reqJson.getString("auditMessage"));
        oaWorkflowDataInnerServiceSMOImpl.updateOaWorkflowData(oaWorkflowDataPo);

        //如果为-1 不插入任务
        if ("-1".equals(reqJson.getString("nextUserId"))) {
            return;
        }

        oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataPo.setFlowId(reqJson.getString("flowId"));
        oaWorkflowDataPo.setContext("");
        oaWorkflowDataPo.setDataId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
        oaWorkflowDataPo.setEvent(OaWorkflowDataDto.EVENT_COMMIT);
        oaWorkflowDataPo.setPreDataId(oaWorkflowDataDtos.get(0).getDataId());
        oaWorkflowDataPo.setStaffId(reqJson.getString("nextUserId"));
        oaWorkflowDataPo.setStaffName(users.get(0).getName());
        oaWorkflowDataPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataInnerServiceSMOImpl.saveOaWorkflowData(oaWorkflowDataPo);
    }

    /**
     * 转办
     *
     * @param reqJson
     * @return
     */
    @Java110Transactional
    public boolean changeTaskToOtherUser(@RequestBody JSONObject reqJson) {

        //查询当前节点
        OaWorkflowDataDto oaWorkflowDataDto = new OaWorkflowDataDto();
        oaWorkflowDataDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataDto.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataDto.setPage(1);
        oaWorkflowDataDto.setRow(1);
        List<OaWorkflowDataDto> oaWorkflowDataDtos = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatas(oaWorkflowDataDto);

        Assert.listOnlyOne(oaWorkflowDataDtos, "数据错误未包含上级数据");

        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("nextUserId"));
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(users, "用户不存在");

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(reqJson.getString("taskId")).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        taskService.addComment(reqJson.getString("taskId"), processInstanceId, reqJson.getString("auditMessage"));
        taskService.setAssignee(reqJson.getString("taskId"), reqJson.getString("nextUserId"));
        //taskService.setOwner(reqJson.getString("taskId"), reqJson.getString("nextUserId"));

        OaWorkflowDataPo oaWorkflowDataPo = null;
        //修改 当前 为完成
        oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setDataId(oaWorkflowDataDtos.get(0).getDataId());
        oaWorkflowDataPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setContext(reqJson.getString("auditMessage"));
        oaWorkflowDataInnerServiceSMOImpl.updateOaWorkflowData(oaWorkflowDataPo);

        oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataPo.setFlowId(reqJson.getString("flowId"));
        oaWorkflowDataPo.setContext("");
        oaWorkflowDataPo.setDataId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
        oaWorkflowDataPo.setEvent(OaWorkflowDataDto.EVENT_TRANSFER);
        oaWorkflowDataPo.setPreDataId(oaWorkflowDataDtos.get(0).getDataId());
        oaWorkflowDataPo.setStaffId(reqJson.getString("nextUserId"));
        oaWorkflowDataPo.setStaffName(users.get(0).getName());
        oaWorkflowDataPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataInnerServiceSMOImpl.saveOaWorkflowData(oaWorkflowDataPo);
        return true;
    }

    @Java110Transactional
    public boolean goBackTask(@RequestBody JSONObject reqJson) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(reqJson.getString("taskId")).singleResult();
        if (task == null) {
            throw new IllegalArgumentException("任务已处理");
        }
//        //退回给提交者
//        if ("1400".equals(reqJson.getString("auditCode"))) {
//            String processInstanceId = task.getProcessInstanceId();
//            Authentication.setAuthenticatedUserId(reqJson.getString("startUserId"));
//            taskService.addComment(reqJson.getString("taskId"), processInstanceId, reqJson.getString("auditMessage"));
//            Map<String, Object> variables = new HashMap<String, Object>();
//            variables.put("nextUserId", reqJson.getString("startUserId"));
//            variables.put("auditCode", reqJson.getString("auditCode"));
//            taskService.complete(reqJson.getString("taskId"), variables);
//
//            return true;
//        }

        String event = doTaskAuditUnAgree(reqJson);
        if (OaWorkflowDataDto.EVENT_COMMIT.equals(event)) { //提交状态
            String processInstanceId = task.getProcessInstanceId();
            Authentication.setAuthenticatedUserId(reqJson.getString("nextUserId"));
            taskService.addComment(reqJson.getString("taskId"), processInstanceId, reqJson.getString("auditMessage"));
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("nextUserId", reqJson.getString("nextUserId"));
            variables.put("auditCode", reqJson.getString("auditCode"));
            taskService.complete(reqJson.getString("taskId"), variables);
        } else { //转单
            taskService.setAssignee(reqJson.getString("taskId"), reqJson.getString("nextUserId"));
        }
        return true;
    }

    /**
     * 审核不同意 退回
     *
     * @param reqJson
     */
    private String doTaskAuditUnAgree(JSONObject reqJson) {
        //查询当前节点
        OaWorkflowDataDto oaWorkflowDataDto = new OaWorkflowDataDto();
        oaWorkflowDataDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataDto.setBusinessKey(reqJson.getString("id"));
        oaWorkflowDataDto.setPage(1);
        oaWorkflowDataDto.setRow(1);
        List<OaWorkflowDataDto> oaWorkflowDataDtos = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatas(oaWorkflowDataDto);

        Assert.listOnlyOne(oaWorkflowDataDtos, "没有上级处理人");

        if ("-1".equals(oaWorkflowDataDtos.get(0).getPreDataId())) {
            throw new IllegalArgumentException("没有上级处理人");
        }

        oaWorkflowDataDto = new OaWorkflowDataDto();
        oaWorkflowDataDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDataDto.setDataId(oaWorkflowDataDtos.get(0).getPreDataId());
        oaWorkflowDataDto.setPage(1);
        oaWorkflowDataDto.setRow(1);
        List<OaWorkflowDataDto> preOaWorkflowDataDtos = oaWorkflowDataInnerServiceSMOImpl.queryOaWorkflowDatas(oaWorkflowDataDto);

        //将现在节点处理为完成
        OaWorkflowDataPo oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setDataId(oaWorkflowDataDtos.get(0).getDataId());
        oaWorkflowDataPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setContext(reqJson.getString("auditMessage"));
        oaWorkflowDataInnerServiceSMOImpl.updateOaWorkflowData(oaWorkflowDataPo);

        reqJson.put("nextUserId", preOaWorkflowDataDtos.get(0).getStaffId());
        if ("1400".equals(reqJson.getString("auditCode"))) {
            reqJson.put("nextUserId", reqJson.getString("startUserId"));
        }
        oaWorkflowDataPo = new OaWorkflowDataPo();
        oaWorkflowDataPo.setBusinessKey(preOaWorkflowDataDtos.get(0).getBusinessKey());
        oaWorkflowDataPo.setFlowId(preOaWorkflowDataDtos.get(0).getFlowId());
        oaWorkflowDataPo.setContext("");
        oaWorkflowDataPo.setDataId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
        oaWorkflowDataPo.setEvent(preOaWorkflowDataDtos.get(0).getEvent());
        oaWorkflowDataPo.setPreDataId(oaWorkflowDataDtos.get(0).getPreDataId());
        oaWorkflowDataPo.setStaffId(reqJson.getString("nextUserId"));
        oaWorkflowDataPo.setStaffName(preOaWorkflowDataDtos.get(0).getStaffName());
        oaWorkflowDataPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        oaWorkflowDataPo.setStoreId(preOaWorkflowDataDtos.get(0).getStoreId());
        oaWorkflowDataInnerServiceSMOImpl.saveOaWorkflowData(oaWorkflowDataPo);

        return oaWorkflowDataDtos.get(0).getEvent();
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

    @Override
    public List<JSONObject> nextAllNodeTaskList(@RequestBody JSONObject reqJson) {
        List<JSONObject> tasks = new ArrayList<>();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(reqJson.getString("taskId")).singleResult();
        if (task == null) {
            throw new IllegalArgumentException("任务已处理");
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        //获取当前节点输出连线
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        JSONObject taskObj = null;
        taskObj = new JSONObject();
        taskObj.put("assignee", "-1"); //  默认 不需要指定下一个处理人 表示结束
        boolean isReturn = false;
        //遍历输出连线
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            //获取输出节点元素
            FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
            isReturn = false;
            //排除非用户任务接点
            if (targetFlowElement instanceof UserTask) {
                //判断输出节点的el表达式
                Map vars = new HashMap();
                vars.put("auditCode", "1200");
                if (isCondition(outgoingFlow.getConditionExpression(), vars)) {
                    //true 获取输出节点名称
                    taskObj.put("back", outgoingFlow.getTargetFlowElement().getName());
                    isReturn = true;
                }
                vars.put("auditCode", "1400");
                if (isCondition(outgoingFlow.getConditionExpression(), vars)) {
                    //true 获取输出节点名称
                    taskObj.put("backIndex", outgoingFlow.getTargetFlowElement().getName());
                    isReturn = true;
                }
                //结束
                vars.put("auditCode", "1500");
                if (isCondition(outgoingFlow.getConditionExpression(), vars)) {
                    //true 获取输出节点名称
                    taskObj.put("exit", outgoingFlow.getTargetFlowElement().getName());
                    isReturn = true;
                }
                if (!isReturn) {
                    String assignee = ((UserTask) targetFlowElement).getAssignee();
                    if (!StringUtil.isEmpty(assignee) && assignee.indexOf("${") < 0) {
                        taskObj.put("assignee", assignee); // 下一节点处理人
                    }
                    if ("${startUserId}".equals(assignee)) {
                        taskObj.put("assignee", reqJson.getString("startUserId")); // 开始人
                    }
                    if ("${nextUserId}".equals(assignee)) {
                        taskObj.put("assignee", "-2"); // 需要前台指定
                    }
                    taskObj.put("next", outgoingFlow.getTargetFlowElement().getName());
                }
            }
            //如果下一个为 结束节点
            if (targetFlowElement instanceof EndEvent) {
                //true 获取输出节点名称
                taskObj.put("exit", "1");
            }
        }
        tasks.add(taskObj);
        return tasks;
    }

    @Override
    public List<JSONObject> queryFirstAuditStaff(@RequestBody OaWorkflowXmlDto oaWorkflowXmlDto) {
        String bpmnXml = oaWorkflowXmlDto.getBpmnXml();
        bpmnXml = bpmnXml.replaceAll("camunda:assignee", "activiti:assignee");
        List<JSONObject> tasks = new ArrayList<>();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(bpmnXml.getBytes()), StandardCharsets.UTF_8);
        XMLStreamReader xtr = null;
        try {
            xtr = xif.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        StartEvent startEvent = null;
        for (FlowElement flowElement : flowElements) {
            //假设是开始节点
            if (flowElement instanceof StartEvent) {
                startEvent = (StartEvent) flowElement;
            }
        }
        if (startEvent == null) {
            throw new SMOException("流程文件未包含开始节点");
        }
        List<SequenceFlow> outgoingFlows = startEvent.getOutgoingFlows();

        UserTask submitUser = getUserTask(outgoingFlows);

        if (submitUser == null) {
            throw new SMOException("未包含提交者");
        }

        UserTask auditUser = getUserTask(submitUser.getOutgoingFlows());
        if (auditUser == null) {
            throw new SMOException("未包含审核人员");
        }
        String assignee = auditUser.getAssignee();
        JSONObject taskObj = null;
        taskObj = new JSONObject();
        taskObj.put("assignee", "-1"); //  默认 不需要指定下一个处理人 表示结束
        if (!StringUtil.isEmpty(assignee) && assignee.indexOf("${") < 0) {
            taskObj.put("assignee", assignee); // 下一节点处理人
        }
        if ("${startUserId}".equals(assignee)) {
            taskObj.put("assignee", "-2"); // 开始人
        }
        if ("${nextUserId}".equals(assignee)) {
            taskObj.put("assignee", "-2"); // 需要前台指定
        }
        tasks.add(taskObj);
        return tasks;
    }

    private static UserTask getUserTask(List<SequenceFlow> outgoingFlows) {
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            //获取输出节点元素
            FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
            //排除非用户任务接点
            if (targetFlowElement instanceof UserTask) {
                //判断输出节点的el表达式
                Map vars = new HashMap();
                vars.put("auditCode", "1200");
                if (isCondition(outgoingFlow.getConditionExpression(), vars)) {
                    continue;
                }
                vars.put("auditCode", "1400");
                if (isCondition(outgoingFlow.getConditionExpression(), vars)) {
                    continue;
                }
                //结束
                vars.put("auditCode", "1500");
                if (isCondition(outgoingFlow.getConditionExpression(), vars)) {
                    continue;
                }
                return ((UserTask) targetFlowElement);
            }
        }
        return null;
    }


    /**
     * el表达式判断
     *
     * @param expression
     * @param vars
     * @return
     */
    private static boolean isCondition(String expression, Map<String, Object> vars) {
        if (expression == null || expression == "") {
            return false;
        }

        //分割表达式
        String[] exprArr = expression.split("[{}$&]");
        for (String expr : exprArr) {
            //是否包含键message
            if (expr.contains("auditCode")) {
                if (!vars.containsKey("auditCode")) {
                    continue;
                }
                if (expr.contains("==")) {
                    String[] primes = expr.split("==");
                    String valExpr = primes[1].trim();
                    if (valExpr.startsWith("'")) {
                        valExpr = valExpr.substring(1);
                    }
                    if (valExpr.endsWith("'")) {
                        valExpr = valExpr.substring(0, valExpr.length() - 1);
                    }
                    if (primes.length == 2 && valExpr.equals(vars.get("auditCode"))) {
                        return true;
                    }
                }
            }
        }
        return false;
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
