package com.java110.job.smo.impl;


import com.java110.job.dao.ITaskAttrServiceDao;
import com.java110.intf.job.ITaskAttrInnerServiceSMO;
import com.java110.dto.task.TaskAttrDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 定时任务属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class TaskAttrInnerServiceSMOImpl extends BaseServiceSMO implements ITaskAttrInnerServiceSMO {

    @Autowired
    private ITaskAttrServiceDao taskAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<TaskAttrDto> queryTaskAttrs(@RequestBody  TaskAttrDto taskAttrDto) {

        //校验是否传了 分页信息

        int page = taskAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            taskAttrDto.setPage((page - 1) * taskAttrDto.getRow());
        }

        List<TaskAttrDto> taskAttrs = BeanConvertUtil.covertBeanList(taskAttrServiceDaoImpl.getTaskAttrInfo(BeanConvertUtil.beanCovertMap(taskAttrDto)), TaskAttrDto.class);

        if (taskAttrs == null || taskAttrs.size() == 0) {
            return taskAttrs;
        }

        String[] userIds = getUserIds(taskAttrs);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (TaskAttrDto taskAttr : taskAttrs) {
            refreshTaskAttr(taskAttr, users);
        }
        return taskAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param taskAttr 小区定时任务属性信息
     * @param users 用户列表
     */
    private void refreshTaskAttr(TaskAttrDto taskAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (taskAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, taskAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param taskAttrs 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<TaskAttrDto> taskAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (TaskAttrDto taskAttr : taskAttrs) {
            userIds.add(taskAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryTaskAttrsCount(@RequestBody TaskAttrDto taskAttrDto) {
        return taskAttrServiceDaoImpl.queryTaskAttrsCount(BeanConvertUtil.beanCovertMap(taskAttrDto));    }

    public ITaskAttrServiceDao getTaskAttrServiceDaoImpl() {
        return taskAttrServiceDaoImpl;
    }

    public void setTaskAttrServiceDaoImpl(ITaskAttrServiceDao taskAttrServiceDaoImpl) {
        this.taskAttrServiceDaoImpl = taskAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
