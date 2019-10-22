package com.java110.common.activity;

import com.java110.utils.factory.ApplicationContextFactory;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;

/**
 * 初始化 activity 流程信息
 */
public class DeploymentActivity {

    public static void deploymentProcess() {

        ProcessEngine processEngine = ApplicationContextFactory.getBean("processEngine", ProcessEngine.class);

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("采购申请流程")
                .addClasspathResource("processes/resourceEntryStore.bpmn").deploy();


    }
}
