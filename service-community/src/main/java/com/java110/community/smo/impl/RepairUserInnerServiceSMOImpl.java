package com.java110.community.smo.impl;


import com.java110.community.dao.IRepairUserServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 报修派单内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RepairUserInnerServiceSMOImpl extends BaseServiceSMO implements IRepairUserInnerServiceSMO {

    @Autowired
    private IRepairUserServiceDao repairUserServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RepairUserDto> queryRepairUsers(@RequestBody  RepairUserDto repairUserDto) {

        //校验是否传了 分页信息

        int page = repairUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairUserDto.setPage((page - 1) * repairUserDto.getRow());
        }

        List<RepairUserDto> repairUsers = BeanConvertUtil.covertBeanList(repairUserServiceDaoImpl.getRepairUserInfo(BeanConvertUtil.beanCovertMap(repairUserDto)), RepairUserDto.class);

        if (repairUsers == null || repairUsers.size() == 0) {
            return repairUsers;
        }

        String[] userIds = getUserIds(repairUsers);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (RepairUserDto repairUser : repairUsers) {
            refreshRepairUser(repairUser, users);
        }
        return repairUsers;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param repairUser 小区报修派单信息
     * @param users 用户列表
     */
    private void refreshRepairUser(RepairUserDto repairUser, List<UserDto> users) {
        for (UserDto user : users) {
            if (repairUser.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, repairUser);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param repairUsers 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<RepairUserDto> repairUsers) {
        List<String> userIds = new ArrayList<String>();
        for (RepairUserDto repairUser : repairUsers) {
            userIds.add(repairUser.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryRepairUsersCount(@RequestBody RepairUserDto repairUserDto) {
        return repairUserServiceDaoImpl.queryRepairUsersCount(BeanConvertUtil.beanCovertMap(repairUserDto));    }

    public IRepairUserServiceDao getRepairUserServiceDaoImpl() {
        return repairUserServiceDaoImpl;
    }

    public void setRepairUserServiceDaoImpl(IRepairUserServiceDao repairUserServiceDaoImpl) {
        this.repairUserServiceDaoImpl = repairUserServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
