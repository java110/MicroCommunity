package com.java110.job.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.job.ITaskInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.task.TaskTemplateDto;
import com.java110.dto.task.TaskTemplateSpecDto;
import com.java110.dto.task.TaskAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.job.dao.ITaskAttrServiceDao;
import com.java110.job.dao.ITaskServiceDao;
import com.java110.job.quartz.TaskSystemJob;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 定时任务内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class TaskInnerServiceSMOImpl extends BaseServiceSMO implements ITaskInnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(TaskInnerServiceSMOImpl.class);

    private static final String defaultCronExpression = "0 * * * * ?";// 每分钟执行一次

    private static final String prefixJobName = "task_"; // job
    private static final String triggerNames = "taskToData_"; // job

    @Autowired
    private Scheduler scheduler;


    @Autowired
    private ITaskServiceDao taskServiceDaoImpl;

    @Autowired
    private ITaskAttrServiceDao taskAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<TaskDto> queryTasks(@RequestBody TaskDto taskDto) {

        //校验是否传了 分页信息

        int page = taskDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            taskDto.setPage((page - 1) * taskDto.getRow());
        }

        List<TaskDto> tasks = BeanConvertUtil.covertBeanList(taskServiceDaoImpl.getTaskInfo(BeanConvertUtil.beanCovertMap(taskDto)), TaskDto.class);

        if (tasks == null || tasks.size() == 0) {
            return tasks;
        }

        String[] userIds = getUserIds(tasks);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (TaskDto task : tasks) {
            refreshTask(task, users);
        }
        return tasks;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param task  小区定时任务信息
     * @param users 用户列表
     */
    private void refreshTask(TaskDto task, List<UserDto> users) {
        for (UserDto user : users) {
            if (task.getTaskId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, task);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param tasks 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<TaskDto> tasks) {
        List<String> userIds = new ArrayList<String>();
        for (TaskDto task : tasks) {
            userIds.add(task.getTaskId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryTasksCount(@RequestBody TaskDto taskDto) {
        return taskServiceDaoImpl.queryTasksCount(BeanConvertUtil.beanCovertMap(taskDto));
    }


    @Override
    public int queryTaskTemplateCount(@RequestBody TaskTemplateDto taskTemplateDto) {
        return taskServiceDaoImpl.queryTaskTemplateCount(BeanConvertUtil.beanCovertMap(taskTemplateDto));
    }


    @Override
    public List<TaskTemplateDto> queryTaskTemplate(@RequestBody TaskTemplateDto taskTemplateDto) {

        //校验是否传了 分页信息

        int page = taskTemplateDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            taskTemplateDto.setPage((page - 1) * taskTemplateDto.getRow());
        }

        List<TaskTemplateDto> taskTemplates = BeanConvertUtil.covertBeanList(taskServiceDaoImpl.getTaskTemplateInfo(BeanConvertUtil.beanCovertMap(taskTemplateDto)), TaskTemplateDto.class);

        return taskTemplates;
    }


    @Override
    public int queryTaskTemplateSpecCount(@RequestBody TaskTemplateSpecDto taskTemplateSpecDto) {
        return taskServiceDaoImpl.queryTaskTemplateSpecCount(BeanConvertUtil.beanCovertMap(taskTemplateSpecDto));
    }


    @Override
    public List<TaskTemplateSpecDto> queryTaskTemplateSpec(@RequestBody TaskTemplateSpecDto taskTemplateSpecDto) {

        //校验是否传了 分页信息

        int page = taskTemplateSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            taskTemplateSpecDto.setPage((page - 1) * taskTemplateSpecDto.getRow());
        }

        List<TaskTemplateSpecDto> taskTemplates = BeanConvertUtil.covertBeanList(taskServiceDaoImpl.getTaskTemplateSpecInfo(BeanConvertUtil.beanCovertMap(taskTemplateSpecDto)), TaskTemplateSpecDto.class);

        return taskTemplates;
    }

    /**
     * 启动任务
     *
     * @param taskDto
     * @return
     */
    public int startTask(@RequestBody TaskDto taskDto) {
        List<TaskAttrDto> attrDtos = BeanConvertUtil.covertBeanList(taskAttrServiceDaoImpl.getTaskAttrInfo(BeanConvertUtil.beanCovertMap(taskDto)),
                TaskAttrDto.class);
        Map info = new HashMap();
        info.put("templateId", taskDto.getTemplateId());
        List<TaskTemplateDto> taskTemplateDtos = BeanConvertUtil.covertBeanList(taskServiceDaoImpl.getTaskTemplateInfo(info), TaskTemplateDto.class);

        Assert.listOnlyOne(taskTemplateDtos, "模板不存在或存在多个");

        taskDto.setTaskTemplateDto(taskTemplateDtos.get(0));
        taskDto.setTaskAttr(attrDtos);

        try {
            String cronExpression = taskDto.getTaskCron();// 如果没有配置则，每一分运行一次

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            String jobName = prefixJobName + taskDto.getTaskId();

            String triggerName = triggerNames + taskDto.getTaskId();

            //设置任务名称
            JobKey jobKey = new JobKey(jobName, TaskSystemJob.JOB_GROUP_NAME);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            if (jobDetail != null) {
                return 0;
            }

            String taskCfgName = taskDto.getTaskName();
            JobDetail warnJob = JobBuilder.newJob(TaskSystemJob.class).withIdentity(jobName, TaskSystemJob.JOB_GROUP_NAME).withDescription("任务启动").build();

            warnJob.getJobDataMap().put(TaskSystemJob.JOB_DATA_CONFIG_NAME, taskCfgName);

            warnJob.getJobDataMap().put(TaskSystemJob.JOB_DATA_TASK_ID, taskDto.getTaskId());
            warnJob.getJobDataMap().put(TaskSystemJob.JOB_DATA_TASK, taskDto);
            warnJob.getJobDataMap().put(TaskSystemJob.JOB_DATA_TASK_ATTR, taskDto);

            // 触发时间点
            CronTrigger warnTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerName + "_group").withSchedule(cronScheduleBuilder).build();

            // 错过执行后，立即执行
            //warnTrigger(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
            //交由Scheduler安排触发
            scheduler.scheduleJob(warnJob, warnTrigger);
            Map paramIn = new HashMap();
            paramIn.put("taskId", taskDto.getTaskId());
            paramIn.put("state", "002");
            paramIn.put("statusCd", "0");
            taskServiceDaoImpl.updateTaskInfoInstance(paramIn);

        } catch (Exception e) {
            logger.error("启动侦听失败", e);
            return 0;
        }
        return 1;
    }

    /**
     * 停止任务
     *
     * @param taskDto
     * @return
     */
    public int stopTask(@RequestBody TaskDto taskDto) {

        try {
            String jobName = prefixJobName + taskDto.getTaskId();

            String triggerName = prefixJobName + taskDto.getTaskId();

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TaskSystemJob.JOB_GROUP_NAME);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);

            JobKey jobKey = new JobKey(jobName, TaskSystemJob.JOB_GROUP_NAME);
            // 删除任务
            scheduler.deleteJob(jobKey);

            Map paramIn = new HashMap();
            paramIn.put("taskId", taskDto.getTaskId());
            paramIn.put("state", "001");
            paramIn.put("statusCd", "0");
            taskServiceDaoImpl.updateTaskInfoInstance(paramIn);

        } catch (Exception e) {
            logger.error("启动侦听失败", e);
            return 0;
        }
        return 1;
    }


    public ITaskServiceDao getTaskServiceDaoImpl() {
        return taskServiceDaoImpl;
    }

    public void setTaskServiceDaoImpl(ITaskServiceDao taskServiceDaoImpl) {
        this.taskServiceDaoImpl = taskServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
