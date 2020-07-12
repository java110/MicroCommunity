package com.java110.common.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IActivitiWorkflowImageInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.Base64Convert;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName ActivitiWorkflowImageInnerServiceSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/10 1:42
 * @Version 1.0
 * add by wuxw 2020/5/10
 **/
@RestController
public class ActivitiWorkflowImageInnerServiceSMOImpl extends BaseServiceSMO implements IActivitiWorkflowImageInnerServiceSMO {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;



    public String getWorkflowImage(@RequestBody String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = "";
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory == null)
                return null;
            else
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        //使用宋体
        String fontName = "宋体";
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null)
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());

        InputStream is = processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
                        fontName, fontName, fontName, null, 1.0);
        String image = "";
        try {
            image = Base64Convert.ioToBase64(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}
