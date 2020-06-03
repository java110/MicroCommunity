package com.java110.job.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.task.ITaskInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.task.TaskTemplateDto;
import com.java110.dto.task.TaskTemplateSpecDto;
import com.java110.dto.user.UserDto;
import com.java110.job.dao.ITaskServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ITaskServiceDao taskServiceDaoImpl;

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
