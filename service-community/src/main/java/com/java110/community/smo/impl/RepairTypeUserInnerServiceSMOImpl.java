package com.java110.community.smo.impl;


import com.java110.community.dao.IRepairTypeUserServiceDao;
import com.java110.intf.community.IRepairTypeUserInnerServiceSMO;
import com.java110.dto.repair.RepairTypeUserDto;
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
 * @Description 报修设置内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RepairTypeUserInnerServiceSMOImpl extends BaseServiceSMO implements IRepairTypeUserInnerServiceSMO {

    @Autowired
    private IRepairTypeUserServiceDao repairTypeUserServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RepairTypeUserDto> queryRepairTypeUsers(@RequestBody  RepairTypeUserDto repairTypeUserDto) {

        //校验是否传了 分页信息

        int page = repairTypeUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairTypeUserDto.setPage((page - 1) * repairTypeUserDto.getRow());
        }

        List<RepairTypeUserDto> repairTypeUsers = BeanConvertUtil.covertBeanList(repairTypeUserServiceDaoImpl.getRepairTypeUserInfo(BeanConvertUtil.beanCovertMap(repairTypeUserDto)), RepairTypeUserDto.class);

        if (repairTypeUsers == null || repairTypeUsers.size() == 0) {
            return repairTypeUsers;
        }

        String[] userIds = getUserIds(repairTypeUsers);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (RepairTypeUserDto repairTypeUser : repairTypeUsers) {
            refreshRepairTypeUser(repairTypeUser, users);
        }
        return repairTypeUsers;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param repairTypeUser 小区报修设置信息
     * @param users 用户列表
     */
    private void refreshRepairTypeUser(RepairTypeUserDto repairTypeUser, List<UserDto> users) {
        for (UserDto user : users) {
            if (repairTypeUser.getTypeUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, repairTypeUser);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param repairTypeUsers 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<RepairTypeUserDto> repairTypeUsers) {
        List<String> userIds = new ArrayList<String>();
        for (RepairTypeUserDto repairTypeUser : repairTypeUsers) {
            userIds.add(repairTypeUser.getTypeUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryRepairTypeUsersCount(@RequestBody RepairTypeUserDto repairTypeUserDto) {
        return repairTypeUserServiceDaoImpl.queryRepairTypeUsersCount(BeanConvertUtil.beanCovertMap(repairTypeUserDto));    }

    public IRepairTypeUserServiceDao getRepairTypeUserServiceDaoImpl() {
        return repairTypeUserServiceDaoImpl;
    }

    public void setRepairTypeUserServiceDaoImpl(IRepairTypeUserServiceDao repairTypeUserServiceDaoImpl) {
        this.repairTypeUserServiceDaoImpl = repairTypeUserServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
