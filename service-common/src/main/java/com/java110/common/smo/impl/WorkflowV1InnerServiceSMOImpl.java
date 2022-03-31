/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.common.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowV1ServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.common.IWorkflowV1InnerServiceSMO;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.FlowUtil;
import com.java110.utils.util.StringUtil;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class WorkflowV1InnerServiceSMOImpl extends BaseServiceSMO implements IWorkflowV1InnerServiceSMO {

    @Autowired
    private IWorkflowV1ServiceDao workflowV1ServiceDaoImpl;

    @Autowired
    private ProcessEngine processEngine;


    @Autowired
    private RepositoryService repositoryService;


    @Override
    public int saveWorkflow(@RequestBody WorkflowPo workflowPo) {
        int saveFlag = workflowV1ServiceDaoImpl.saveWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowPo));
        return saveFlag;
    }

    @Override
    public int updateWorkflow(@RequestBody WorkflowPo workflowPo) {
        int saveFlag = workflowV1ServiceDaoImpl.updateWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowPo));
        return saveFlag;
    }

    @Override
    public int deleteWorkflow(@RequestBody WorkflowPo workflowPo) {
        workflowPo.setStatusCd("1");
        int saveFlag = workflowV1ServiceDaoImpl.updateWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowPo));
        return saveFlag;
    }

    @Override
    public List<WorkflowDto> queryWorkflows(@RequestBody WorkflowDto workflowDto) {

        //校验是否传了 分页信息

        int page = workflowDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workflowDto.setPage((page - 1) * workflowDto.getRow());
        }

        List<WorkflowDto> workflows = BeanConvertUtil.covertBeanList(workflowV1ServiceDaoImpl.getWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowDto)), WorkflowDto.class);

        return workflows;
    }


    @Override
    public int queryWorkflowsCount(@RequestBody WorkflowDto workflowDto) {
        return workflowV1ServiceDaoImpl.queryWorkflowsCount(BeanConvertUtil.beanCovertMap(workflowDto));
    }


    /**
     * @param reqJson {
     *                taskId:"",
     *                startUserId:""
     *                }
     * @return
     */
    @Override
    public List<JSONObject> getWorkflowNextNode(@RequestBody JSONObject reqJson) {
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
                vars.put("flag", "false"); // 1100
                if (FlowUtil.isCondition(outgoingFlow.getConditionExpression(), vars)) {
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
                }
            }
            //如果下一个为 结束节点
            if (targetFlowElement instanceof EndEvent) {
                //true 获取输出节点名称
                taskObj.put("assignee", "-1");
            }
        }
        tasks.add(taskObj);
        return tasks;
    }



}
