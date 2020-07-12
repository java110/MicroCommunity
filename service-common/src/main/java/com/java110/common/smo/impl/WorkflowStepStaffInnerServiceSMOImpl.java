package com.java110.common.smo.impl;


import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.intf.common.IWorkflowStepStaffInnerServiceSMO;
import com.java110.dto.workflow.WorkflowStepStaffDto;
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
public class WorkflowStepStaffInnerServiceSMOImpl extends BaseServiceSMO implements IWorkflowStepStaffInnerServiceSMO {

    @Autowired
    private IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<WorkflowStepStaffDto> queryWorkflowStepStaffs(@RequestBody  WorkflowStepStaffDto workflowStepStaffDto) {

        //校验是否传了 分页信息

        int page = workflowStepStaffDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workflowStepStaffDto.setPage((page - 1) * workflowStepStaffDto.getRow());
        }

        List<WorkflowStepStaffDto> workflowStepStaffs = BeanConvertUtil.covertBeanList(workflowStepStaffServiceDaoImpl.getWorkflowStepStaffInfo(BeanConvertUtil.beanCovertMap(workflowStepStaffDto)), WorkflowStepStaffDto.class);

        if (workflowStepStaffs == null || workflowStepStaffs.size() == 0) {
            return workflowStepStaffs;
        }

        String[] userIds = getUserIds(workflowStepStaffs);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (WorkflowStepStaffDto workflowStepStaff : workflowStepStaffs) {
            refreshWorkflowStepStaff(workflowStepStaff, users);
        }
        return workflowStepStaffs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param workflowStepStaff 小区工作流节点信息
     * @param users 用户列表
     */
    private void refreshWorkflowStepStaff(WorkflowStepStaffDto workflowStepStaff, List<UserDto> users) {
        for (UserDto user : users) {
            if (workflowStepStaff.getWssId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, workflowStepStaff);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param workflowStepStaffs 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<WorkflowStepStaffDto> workflowStepStaffs) {
        List<String> userIds = new ArrayList<String>();
        for (WorkflowStepStaffDto workflowStepStaff : workflowStepStaffs) {
            userIds.add(workflowStepStaff.getWssId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryWorkflowStepStaffsCount(@RequestBody WorkflowStepStaffDto workflowStepStaffDto) {
        return workflowStepStaffServiceDaoImpl.queryWorkflowStepStaffsCount(BeanConvertUtil.beanCovertMap(workflowStepStaffDto));    }

    public IWorkflowStepStaffServiceDao getWorkflowStepStaffServiceDaoImpl() {
        return workflowStepStaffServiceDaoImpl;
    }

    public void setWorkflowStepStaffServiceDaoImpl(IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl) {
        this.workflowStepStaffServiceDaoImpl = workflowStepStaffServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
