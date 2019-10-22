package com.java110.common.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.common.IResourceEntryStoreInnerServiceSMO;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service("resourceEntryStoreSMOImpl")
@RestController
public class ResourceEntryStoreInnerServiceSMOImpl extends BaseServiceSMO implements IResourceEntryStoreInnerServiceSMO {

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
    public String startProcess() {
        //将信息加入map,以便传入流程中
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "廉斌");
        variables.put("day", 10);
        //开启流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("resourceEntry", variables);
        //将得到的实例流程id值赋给之前设置的变量
        String processInstanceId = processInstance.getId();
        System.out.println("流程开启成功.......实例流程id:" + processInstanceId);

        return processInstanceId;
    }

    public void getTaskAndComplete(String processInstanceId) {
        //获取taskservice实例
        TaskService taskService = processEngine.getTaskService();

        //开始进行流程
        while (this.processEngine.getRuntimeService()
                .createProcessInstanceQuery()//获取查询对象
                .processInstanceId(processInstanceId)//根据id查询流程实例
                .singleResult()//获取查询结果,如果为空,说明这个流程已经执行完毕,否则,获取任务并执行
                != null) {
            Task task = taskService.createTaskQuery()//创建查询对象
                    .processInstanceId(processInstanceId)//通过流程实例id来查询当前任务
                    .singleResult();//获取单个查询结果
            String taskName = task.getName();
            if (taskName.equals("StartEvent")) {//职员节点
                completeEmployeeTask(task);
            } else if (taskName.equals("departmentManager")) {//领导节点
                completeLeaderTask(task);
            } else {//经理节点
                completeJingliTask(task);
            }
        }

        System.out.println("审核结束..........");
    }


    //职员提交申请
    public void completeEmployeeTask(Task task) {
        //获取任务id
        String taskId = task.getId();

        //完成任务
        this.processEngine.getTaskService().complete(taskId);
        System.out.println("职员已经提交申请.......");

    }

    //领导审批
    public void completeLeaderTask(Task task) {
        //获取任务id
        String taskId = task.getId();

        //领导意见
        Map<String, Object> variables = new HashMap<String, Object>();
        //variables.put("day",4);
        variables.put("leaderResult", 1);
        //完成任务
        this.processEngine.getTaskService().complete(taskId, variables);
        System.out.println("领导审核完毕........");

    }

    //经理审批
    public void completeJingliTask(Task task) {
        //获取任务id
        String taskId = task.getId();
        String name = task.getName();
        //经理意见
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("result", 0);
        //完成任务
        this.processEngine.getTaskService().complete(taskId, variables);
        System.out.println("经理审核完毕........,审核经理:" + name);

    }

    /**
     *
     * <p>描述: 根据用户id查询待办任务列表</p>
     * @author 范相如
     * @date 2018年2月25日
     */
    public List<Task> findTasksByUserId(String userId) {
        List<Task> resultTask = taskService.createTaskQuery().processDefinitionKey("demo5").taskCandidateOrAssigned(userId).list();
        return resultTask;
    }
}
