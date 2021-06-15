package com.java110.common.activity;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * @ClassName ActivitiConfig
 * @Description TODO
 * @Author wuxw
 * @Date 2019/10/22 21:55
 * @Version 1.0
 * add by wuxw 2019/10/22
 **/
@Configuration
public class ActivitiConfig  {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

   /* @Override
    public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        springProcessEngineConfiguration.setIdGenerator(new ActivityIdGenerator());
    }*/


    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
        SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
        spec.setDataSource(dataSource);
        spec.setTransactionManager(platformTransactionManager);
        spec.setDatabaseSchemaUpdate("true");
        spec.setActivityFontName("宋体");
        spec.setAnnotationFontName("宋体");
        spec.setLabelFontName("宋体");
        Resource[] resources = null;
        // 启动自动部署流程
        try {
            resources = new PathMatchingResourcePatternResolver().getResources("classpath*:processes/*.bpmn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        spec.setDeploymentResources(resources);
        return spec;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return processEngineFactoryBean;
    }


    @Bean
    public RepositoryService repositoryService() throws Exception {
        return processEngine().getObject().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() throws Exception {
        return processEngine().getObject().getRuntimeService();
    }

    @Bean
    public TaskService taskService() throws Exception {
        return processEngine().getObject().getTaskService();
    }

    @Bean
    public HistoryService historyService() throws Exception {
        return processEngine().getObject().getHistoryService();
    }

}