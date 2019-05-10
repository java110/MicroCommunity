package com.java110.user.smo.impl;


import com.java110.common.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.OwnerDto;
import com.java110.dto.PageDto;
import com.java110.dto.UserDto;
import com.java110.user.dao.IOwnerServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerInnerServiceSMO {

    @Autowired
    private IOwnerServiceDao ownerServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OwnerDto> queryOwners(@RequestBody OwnerDto ownerDto) {

        //校验是否传了 分页信息

        int page = ownerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDto.setPage((page - 1) * ownerDto.getRow());
            ownerDto.setRow(page * ownerDto.getRow());
        }

        List<OwnerDto> owners = BeanConvertUtil.covertBeanList(ownerServiceDaoImpl.getOwnerInfo(BeanConvertUtil.beanCovertMap(ownerDto)), OwnerDto.class);

        if (owners == null || owners.size() == 0) {
            return owners;
        }

        String[] userIds = getUserIds(owners);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (OwnerDto owner : owners) {
            refreshOwner(owner, users);
        }
        return owners;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param owner 小区业主信息
     * @param users 用户列表
     */
    private void refreshOwner(OwnerDto owner, List<UserDto> users) {
        for (UserDto user : users) {
            if (owner.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, owner);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param owners 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerDto> owners) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerDto owner : owners) {
            userIds.add(owner.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnersCount(@RequestBody OwnerDto ownerDto) {
        return ownerServiceDaoImpl.queryOwnersCount(BeanConvertUtil.beanCovertMap(ownerDto));
    }

    public IOwnerServiceDao getOwnerServiceDaoImpl() {
        return ownerServiceDaoImpl;
    }

    public void setOwnerServiceDaoImpl(IOwnerServiceDao ownerServiceDaoImpl) {
        this.ownerServiceDaoImpl = ownerServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
