package com.java110.common.smo.impl;


import com.java110.common.dao.IAuditUserServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IAuditUserInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.auditUser.AuditUserDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 审核人员内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AuditUserInnerServiceSMOImpl extends BaseServiceSMO implements IAuditUserInnerServiceSMO {

    @Autowired
    private IAuditUserServiceDao auditUserServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<AuditUserDto> queryAuditUsers(@RequestBody AuditUserDto auditUserDto) {

        //校验是否传了 分页信息

        int page = auditUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            auditUserDto.setPage((page - 1) * auditUserDto.getRow());
        }

        List<AuditUserDto> auditUsers = BeanConvertUtil.covertBeanList(auditUserServiceDaoImpl.getAuditUserInfo(BeanConvertUtil.beanCovertMap(auditUserDto)), AuditUserDto.class);

        if (auditUsers == null || auditUsers.size() == 0) {
            return auditUsers;
        }

        String[] userIds = getUserIds(auditUsers);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (AuditUserDto auditUser : auditUsers) {
            refreshAuditUser(auditUser, users);
        }
        return auditUsers;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param auditUser 小区审核人员信息
     * @param users     用户列表
     */
    private void refreshAuditUser(AuditUserDto auditUser, List<UserDto> users) {
        for (UserDto user : users) {
            if (auditUser.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, auditUser);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param auditUsers 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AuditUserDto> auditUsers) {
        List<String> userIds = new ArrayList<String>();
        for (AuditUserDto auditUser : auditUsers) {
            userIds.add(auditUser.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAuditUsersCount(@RequestBody AuditUserDto auditUserDto) {
        return auditUserServiceDaoImpl.queryAuditUsersCount(BeanConvertUtil.beanCovertMap(auditUserDto));
    }

    public IAuditUserServiceDao getAuditUserServiceDaoImpl() {
        return auditUserServiceDaoImpl;
    }

    public void setAuditUserServiceDaoImpl(IAuditUserServiceDao auditUserServiceDaoImpl) {
        this.auditUserServiceDaoImpl = auditUserServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
