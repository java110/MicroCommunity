package com.java110.common.smo.impl;


import com.java110.common.dao.IWorkflowStepServiceDao;
import com.java110.intf.common.IWorkflowStepInnerServiceSMO;
import com.java110.dto.workflow.WorkflowStepDto;
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
 * @Description 工作流节点内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class WorkflowStepInnerServiceSMOImpl extends BaseServiceSMO implements IWorkflowStepInnerServiceSMO {

    @Autowired
    private IWorkflowStepServiceDao workflowStepServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<WorkflowStepDto> queryWorkflowSteps(@RequestBody  WorkflowStepDto workflowStepDto) {

        //校验是否传了 分页信息

        int page = workflowStepDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workflowStepDto.setPage((page - 1) * workflowStepDto.getRow());
        }

        List<WorkflowStepDto> workflowSteps = BeanConvertUtil.covertBeanList(workflowStepServiceDaoImpl.getWorkflowStepInfo(BeanConvertUtil.beanCovertMap(workflowStepDto)), WorkflowStepDto.class);

        if (workflowSteps == null || workflowSteps.size() == 0) {
            return workflowSteps;
        }

        String[] userIds = getUserIds(workflowSteps);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (WorkflowStepDto workflowStep : workflowSteps) {
            refreshWorkflowStep(workflowStep, users);
        }
        return workflowSteps;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param workflowStep 小区工作流节点信息
     * @param users 用户列表
     */
    private void refreshWorkflowStep(WorkflowStepDto workflowStep, List<UserDto> users) {
        for (UserDto user : users) {
            if (workflowStep.getFlowId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, workflowStep);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param workflowSteps 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<WorkflowStepDto> workflowSteps) {
        List<String> userIds = new ArrayList<String>();
        for (WorkflowStepDto workflowStep : workflowSteps) {
            userIds.add(workflowStep.getFlowId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryWorkflowStepsCount(@RequestBody WorkflowStepDto workflowStepDto) {
        return workflowStepServiceDaoImpl.queryWorkflowStepsCount(BeanConvertUtil.beanCovertMap(workflowStepDto));    }

    public IWorkflowStepServiceDao getWorkflowStepServiceDaoImpl() {
        return workflowStepServiceDaoImpl;
    }

    public void setWorkflowStepServiceDaoImpl(IWorkflowStepServiceDao workflowStepServiceDaoImpl) {
        this.workflowStepServiceDaoImpl = workflowStepServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
