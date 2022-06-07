package com.java110.common.smo.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.workflow.WorkflowAuditInfoDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.dto.workflow.WorkflowStepDto;
import com.java110.dto.workflow.WorkflowStepStaffDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.task.Comment;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 工作流内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class WorkflowInnerServiceSMOImpl extends BaseServiceSMO implements IWorkflowInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(BaseServiceSMO.class);

    @Autowired
    private IWorkflowServiceDao workflowServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<WorkflowDto> queryWorkflows(@RequestBody WorkflowDto workflowDto) {

        //校验是否传了 分页信息

        int page = workflowDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workflowDto.setPage((page - 1) * workflowDto.getRow());
        }

        List<WorkflowDto> workflows = BeanConvertUtil.covertBeanList(workflowServiceDaoImpl.getWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowDto)), WorkflowDto.class);

        if (workflows == null || workflows.size() == 0) {
            return workflows;
        }

        String[] userIds = getUserIds(workflows);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (WorkflowDto workflow : workflows) {
            refreshWorkflow(workflow, users);
        }
        return workflows;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param workflow 小区工作流信息
     * @param users    用户列表
     */
    private void refreshWorkflow(WorkflowDto workflow, List<UserDto> users) {
        for (UserDto user : users) {
            if (workflow.getFlowId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, workflow);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param workflows 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<WorkflowDto> workflows) {
        List<String> userIds = new ArrayList<String>();
        for (WorkflowDto workflow : workflows) {
            userIds.add(workflow.getFlowId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    public String getRunWorkflowImage(@RequestBody String businessKey) {

        String image = "";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            //  获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey).singleResult();

            if (historicProcessInstance == null) {
                //throw new BusinessException("获取流程实例ID[" + pProcessInstanceId + "]对应的历史流程实例失败！");
            } else {
                // 获取流程定义
                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

                // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(historicProcessInstance.getId()).orderByHistoricActivityInstanceId().asc().list();

                // 已执行的节点ID集合
                List<String> executedActivityIdList = new ArrayList<String>();
                int index = 1;
                //logger.info("获取已经执行的节点ID");
                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                    executedActivityIdList.add(activityInstance.getActivityId());

                    //logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
                    index++;
                }

                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

                // 已执行的线集合
                List<String> flowIds = new ArrayList<String>();
                // 获取流程走过的线 (getHighLightedFlows是下面的方法)
                flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);


                // 获取流程图图像字符流
                ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
                //配置字体
                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体", "宋体", "宋体", null, 2.0);


                image = Base64Convert.ioToBase64(imageStream);
            }
        } catch (Exception e) {
            logger.error("读取图片失败", e);

        }
        return image;
    }

    public List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 对历史流程节点进行遍历
            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());


            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
            FlowNode sameActivityImpl1 = null;

            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
            HistoricActivityInstance activityImp2_;

            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
                activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点

                if (activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
                {

                } else {
                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
                    break;
                }

            }
            sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线

            for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }

        }
        return highFlows;

    }


    public String getWorkflowImage(@RequestBody WorkflowDto workflowDto) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        String image = "";
        List<String> list = processEngine.getRepositoryService()//
                .getDeploymentResourceNames(workflowDto.getProcessDefinitionKey());

        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String name : list) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }

        InputStream in = processEngine.getRepositoryService()
                .getResourceAsStream(workflowDto.getProcessDefinitionKey(), resourceName);


        try {
            image = Base64Convert.ioToBase64(in);
        } catch (IOException e) {
            logger.error("读取图片失败", e);
        }
        return image;
    }


    /**
     * @Date：2017/11/24
     * @Description：创建流程并部署
     */
    public WorkflowDto addFlowDeployment(@RequestBody WorkflowDto workflowDto) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//
//        RepositoryService repositoryService  = processEngine.getRepositoryService();
//        repositoryService.deleteDeployment("1");
        try {
            // 1. 建立模型
            BpmnModel model = new BpmnModel();
            Process process = new Process();
            model.addProcess(process);
            process.setId(WorkflowDto.DEFAULT_PROCESS + workflowDto.getFlowId());
            process.setName(workflowDto.getFlowName());
            process.setDocumentation(workflowDto.getDescrible());
            //添加流程
            //开始节点
            process.addFlowElement(createStartEvent());
            List<WorkflowStepDto> workflowStepDtos = workflowDto.getWorkflowSteps();
            for (int i = 0; i < workflowStepDtos.size(); i++) {
                WorkflowStepDto step = workflowStepDtos.get(i);
                //判断是否会签
                if (WorkflowStepDto.TYPE_COUNTERSIGN.equals(step.getType())) {
                    //会签
                    //加入并行网关-分支
                    process.addFlowElement(createParallelGateway("parallelGateway-fork" + i, "parallelGateway-fork" + i));
                    //获取角色下所有用户
                    List<WorkflowStepStaffDto> userList = step.getWorkflowStepStaffs();
                    for (int u = 0; u < userList.size(); u++) {
                        //并行网关分支的审核节点
                        process.addFlowElement(createUserTask("userTask" + i + u, userList.get(u).getStaffName(), userList.get(u).getStaffId()));
                    }
                    //并行网关-汇聚
                    process.addFlowElement(createParallelGateway("parallelGateway-join" + i, "parallelGateway-join" + i));

                    process.addFlowElement(createUserTask("repulse" + i, "提交者", "${startUserId}"));

                } else {
                    //普通流转
                    //审核节点
                    process.addFlowElement(createUserTask("task" + i, step.getWorkflowStepStaffs().get(0).getStaffName(), step.getWorkflowStepStaffs().get(0).getStaffId()));
                    //回退节点
                    process.addFlowElement(createUserTask("repulse" + i, "提交者", "${startUserId}"));
                }
            }
            //结束节点
            process.addFlowElement(createEndEvent());


            //连线
            for (int y = 0; y < workflowStepDtos.size(); y++) {
                WorkflowStepDto step = workflowStepDtos.get(y);
                //是否会签
                if (WorkflowStepDto.TYPE_COUNTERSIGN.equals(step.getType())) {
                    //会签
                    //判断是否第一个节点
                    if (y == 0) {
                        //开始节点和并行网关-分支连线
                        process.addFlowElement(createSequenceFlow("startEvent", "parallelGateway-fork" + y, "startEvent-parallelGateway-fork" + y, ""));
                    } else {
                        //审核节点或者并行网关-汇聚到并行网关-分支
                        //判断上一个节点是否是会签
                        if (WorkflowStepDto.TYPE_COUNTERSIGN.equals(workflowStepDtos.get(y - 1).getType())) {
                            process.addFlowElement(createSequenceFlow("parallelGateway-join" + (y - 1), "parallelGateway-fork" + y, "parallelGateway-join-parallelGateway-fork-分支" + y, "${flag=='true'}"));
                        } else {
                            process.addFlowElement(createSequenceFlow("task" + (y - 1), "parallelGateway-fork" + y, "task-parallelGateway-fork" + y, "${flag=='true'}"));
                        }
                    }
                    //并行网关-分支和会签用户连线，会签用户和并行网关-汇聚连线
                    List<WorkflowStepStaffDto> userList = step.getWorkflowStepStaffs();
                    for (int u = 0; u < userList.size(); u++) {
                        process.addFlowElement(createSequenceFlow("parallelGateway-fork" + y, "userTask" + y + u, "parallelGateway-fork-userTask" + y + u, ""));
                        process.addFlowElement(createSequenceFlow("userTask" + y + u, "parallelGateway-join" + y, "userTask-parallelGateway-join", ""));
                        if (u == (userList.size() - 1)) {
                            if (y == (workflowStepDtos.size() - 1)) {
                                if("Y".equals(workflowDto.getStartNodeFinish())){ //需要提交者确认
                                    process.addFlowElement(createSequenceFlow("parallelGateway-join" + y, "repulse" + y, "parallelGateway-join-repulse", ""));
                                }else {
                                    process.addFlowElement(createSequenceFlow("parallelGateway-join" + y, "endEvent", "parallelGateway-join-endEvent" + y, "${flag=='true'}"));
                                    process.addFlowElement(createSequenceFlow("parallelGateway-join" + y, "repulse" + y, "tparallelGateway-join-repulse" + y, "${flag=='false'}"));
                                }
                            } else {
                                process.addFlowElement(createSequenceFlow("parallelGateway-join" + y, "repulse" + y, "parallelGateway-join-repulse", "${flag=='false'}"));
                            }
                            process.addFlowElement(createSequenceFlow("repulse" + y, "task" + getNormal(workflowStepDtos, y), "repulse-task" + y, "${flag=='true'}"));
                        }
                    }


                    process.addFlowElement(createSequenceFlow("repulse" + y, "endEvent", "parallelGateway-join-endEvent", "${flag=='false'}"));

                } else {
                    //普通流转
                    //第一个节点
                    if (y == 0) {
                        //开始节点和审核节点1
                        process.addFlowElement(createSequenceFlow("startEvent", "task" + y, "startEvent-task" + y, ""));
                    } else {
                        //判断上一个节点是否会签
                        if (WorkflowStepDto.TYPE_COUNTERSIGN.equals(workflowStepDtos.get(y - 1).getType())) {
                            //会签
                            //并行网关-汇聚到审核节点
                            process.addFlowElement(createSequenceFlow("parallelGateway-join" + (y - 1), "task" + y, "parallelGateway-join-task" + y, "${flag=='true'}"));
                        } else {
                            //普通
                            process.addFlowElement(createSequenceFlow("task" + (y - 1), "task" + y, "task" + (y - 1) + "task" + y, "${flag=='true'}"));
                        }
                    }
                    //是否最后一个节点
                  /*  if (y == (workflowStepDtos.size() - 1)) {
                        //审核节点到结束节点
                        process.addFlowElement(createSequenceFlow("repulse" + y, "endEvent", "task" + y + "endEvent", "${flag=='false'}"));
                        process.addFlowElement(createSequenceFlow("task" + y, "repulse" + y, "task-repulse" + y, "${flag=='false'}"));
                    } else {
                        //审核节点到回退节点
                        process.addFlowElement(createSequenceFlow("task" + y, "repulse" + y, "task-repulse" + y, "${flag=='false'}"));
                    }*/
                    process.addFlowElement(createSequenceFlow("repulse" + y, "endEvent", "repulse" + y + "endEvent", "${flag=='false'}"));
                    if (y == (workflowStepDtos.size() - 1)) {
                        if("Y".equals(workflowDto.getStartNodeFinish())){ //需要提交者确认
                            process.addFlowElement(createSequenceFlow("task" + y, "repulse" + y, "task-repulse" + y, ""));
                        }else {
                            process.addFlowElement(createSequenceFlow("task" + y, "endEvent", "task-endEvent" + y, "${flag=='true'}"));
                            process.addFlowElement(createSequenceFlow("task" + y, "repulse" + y, "task-repulse" + y, "${flag=='false'}"));
                        }
                    } else {
                        process.addFlowElement(createSequenceFlow("task" + y, "repulse" + y, "task-repulse" + y, "${flag=='false'}"));
                    }

                    process.addFlowElement(createSequenceFlow("repulse" + y, "task" + y, "repulse-task" + y, "${flag=='true'}"));
                }
            }

            // 2. 生成的图形信息
            new BpmnAutoLayout(model).execute();

            // 3. 部署流程
            Deployment deployment = processEngine.getRepositoryService().createDeployment()
                    .addBpmnModel(process.getId() + ".bpmn", model).name(process.getId() + "_deployment").deploy();
            workflowDto.setProcessDefinitionKey(deployment.getId());

        } catch (Exception e) {
            logger.error("部署工作流失败", e);
        }

        logger.debug("工作流部署完成");
        return workflowDto;
    }

    private int getNormal(List<WorkflowStepDto> workflowStepDtos, int y) {
        for (int stepIndex = y; stepIndex > 0; stepIndex--) {
            if (WorkflowStepDto.TYPE_NORMAL.equals(workflowStepDtos.get(stepIndex).getType())) {
                return stepIndex;
            }
        }

        return 0;
    }


    //任务节点-组
    protected UserTask createGroupTask(String id, String name, String candidateGroup) {
        List<String> candidateGroups = new ArrayList<String>();
        candidateGroups.add(candidateGroup);
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setCandidateGroups(candidateGroups);
        return userTask;
    }

    //任务节点-用户
    protected UserTask createUserTask(String id, String name, String userPkno) {
        List<String> candidateUsers = new ArrayList<String>();
        candidateUsers.add(userPkno);
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(userPkno);
        //userTask.setCandidateUsers(candidateUsers);
        return userTask;
    }

    //任务节点-锁定者
    protected UserTask createAssigneeTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    /*连线*/
    protected SequenceFlow createSequenceFlow(String from, String to, String name, String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setName(name);
        if (!StringUtil.isEmpty(conditionExpression)) {
            flow.setConditionExpression(conditionExpression);
        }
        return flow;
    }

    //排他网关
    protected ExclusiveGateway createExclusiveGateway(String id, String name) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(name);
        return exclusiveGateway;
    }

    //并行网关
    protected ParallelGateway createParallelGateway(String id, String name) {
        ParallelGateway gateway = new ParallelGateway();
        gateway.setId(id);
        gateway.setName(name);
        return gateway;
    }

    //开始节点
    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        return startEvent;
    }

    /*结束节点*/
    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        return endEvent;
    }

    @Override
    public int queryWorkflowsCount(@RequestBody WorkflowDto workflowDto) {
        return workflowServiceDaoImpl.queryWorkflowsCount(BeanConvertUtil.beanCovertMap(workflowDto));
    }

    /**
     * 查询审核历史
     *
     * @param workflowAuditInfoDto
     * @return
     */
    public List<WorkflowAuditInfoDto> queryWorkflowAuditHistory(@RequestBody WorkflowAuditInfoDto workflowAuditInfoDto) {
        //List<TaskBo> taskBoList = new ArrayList<TaskBo>();
        HistoricProcessInstance hisProcessInstance = (HistoricProcessInstance) historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(workflowAuditInfoDto.getBusinessKey()).singleResult();
        List<HistoricActivityInstance> hisActInstList = new ArrayList<>();
        if (hisProcessInstance != null) {
            // 该流程实例的所有节点审批记录
            hisActInstList = getHisUserTaskActivityInstanceList(hisProcessInstance
                    .getId());
        }
        List<WorkflowAuditInfoDto> workflowAuditInfoDtos = new ArrayList<>();
        WorkflowAuditInfoDto tmpWorkflowAuditInfoDto = null;
        for (Iterator iterator = hisActInstList.iterator(); iterator.hasNext(); ) {
            // 需要转换成HistoricActivityInstance
            HistoricActivityInstance activityInstance = (HistoricActivityInstance) iterator
                    .next();

            tmpWorkflowAuditInfoDto = new WorkflowAuditInfoDto();
            tmpWorkflowAuditInfoDto.setAuditName(activityInstance.getActivityName());
            if (activityInstance.getEndTime() != null) {
                tmpWorkflowAuditInfoDto.setAuditTime(DateUtil.getFormatTimeString(activityInstance.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
                tmpWorkflowAuditInfoDto.setStateName("完成");
                tmpWorkflowAuditInfoDto.setState(WorkflowAuditInfoDto.STATE_FINISH);
            } else {
                tmpWorkflowAuditInfoDto.setStateName("正在处理");
                tmpWorkflowAuditInfoDto.setState(WorkflowAuditInfoDto.STATE_NO);
            }
            Long time = activityInstance.getDurationInMillis() == null ? new Date().getTime() - activityInstance.getStartTime().getTime()
                    : activityInstance.getDurationInMillis();
            tmpWorkflowAuditInfoDto.setDuration(getCostTime(time));

            //如果还没结束则不放里面
            List<Comment> comments = taskService.getTaskComments(activityInstance.getTaskId());
            String msg = "";
            if (comments != null && comments.size() > 0) {
                for (Comment comment : comments) {
                    msg += (comment.getFullMessage() + "/");
                }
            }
            msg = msg.endsWith("/") ? msg.substring(0, msg.length() - 1) : msg;
            tmpWorkflowAuditInfoDto.setUserId(activityInstance.getAssignee());
            tmpWorkflowAuditInfoDto.setMessage(msg);
            workflowAuditInfoDtos.add(tmpWorkflowAuditInfoDto);
        }
        return workflowAuditInfoDtos;
    }

    @Override
    public WorkflowModelDto createModel(@RequestBody  WorkflowModelDto workflowModelDto) {
        logger.info("创建模型入参name：{},key:{}", workflowModelDto.getName(), workflowModelDto.getKey());
        Model model = repositoryService.newModel();
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, workflowModelDto.getName());
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setName(workflowModelDto.getName());
        model.setKey(workflowModelDto.getKey());
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        createObjectNode(model.getId());
        logger.info("创建模型结束，返回模型ID：{}", model.getId());
        workflowModelDto.setModelId(model.getId());
        return workflowModelDto;
    }


    /**
     * 创建模型时完善ModelEditorSource
     *
     * @param modelId
     */
    @SuppressWarnings("deprecation")
    private void createObjectNode(String modelId) {
        logger.info("创建模型完善ModelEditorSource入参模型ID：{}", modelId);
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes("utf-8"));
        } catch (Exception e) {
            logger.info("创建模型时完善ModelEditorSource服务异常：{}", e);
        }
        logger.info("创建模型完善ModelEditorSource结束");
    }



    /**
     * @param processInstanceId
     * @return
     * @CreateUser:xxxx
     * @ReturnType:List
     * @CreateDate:2014-6-25下午5:03:13
     * @UseFor :在 ACT_HI_ACTINST 表中找到对应流程实例的userTask节点 不包括startEvent
     * <p>
     * .finished()
     */
    private List<HistoricActivityInstance> getHisUserTaskActivityInstanceList(
            String processInstanceId) {
        List<HistoricActivityInstance> hisActivityInstanceList = ((HistoricActivityInstanceQuery) historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).activityType("userTask")
                .orderByHistoricActivityInstanceStartTime().asc())
                .list();
        return hisActivityInstanceList;
    }

    public IWorkflowServiceDao getWorkflowServiceDaoImpl() {
        return workflowServiceDaoImpl;
    }

    public void setWorkflowServiceDaoImpl(IWorkflowServiceDao workflowServiceDaoImpl) {
        this.workflowServiceDaoImpl = workflowServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    public String getCostTime(Long time) {
        if (time == null) {
            return "00:00";
        }
        long hours = time / (1000 * 60 * 60);
        long minutes = (time - hours * (1000 * 60 * 60)) / (1000 * 60);
        String diffTime = "";
        if (minutes < 10) {
            diffTime = hours + ":0" + minutes;
        } else {
            diffTime = hours + ":" + minutes;
        }
        return diffTime;
    }


}
