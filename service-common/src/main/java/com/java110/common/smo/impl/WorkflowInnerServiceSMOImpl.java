package com.java110.common.smo.impl;


import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.core.smo.common.IWorkflowInnerServiceSMO;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @Autowired
    private IWorkflowServiceDao workflowServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<WorkflowDto> queryWorkflows(@RequestBody  WorkflowDto workflowDto) {

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
     * @param users 用户列表
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

    @Override
    public int queryWorkflowsCount(@RequestBody WorkflowDto workflowDto) {
        return workflowServiceDaoImpl.queryWorkflowsCount(BeanConvertUtil.beanCovertMap(workflowDto));    }

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
}
