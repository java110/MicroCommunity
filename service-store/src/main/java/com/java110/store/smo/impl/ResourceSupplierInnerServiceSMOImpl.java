package com.java110.store.smo.impl;


import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.store.dao.IResourceSupplierServiceDao;
import com.java110.intf.store.IResourceSupplierInnerServiceSMO;
import com.java110.dto.resourceSupplier.ResourceSupplierDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 物品供应商内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ResourceSupplierInnerServiceSMOImpl extends BaseServiceSMO implements IResourceSupplierInnerServiceSMO {

    @Autowired
    private IResourceSupplierServiceDao resourceSupplierServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ResourceSupplierDto> queryResourceSuppliers(@RequestBody ResourceSupplierDto resourceSupplierDto) {

        //校验是否传了 分页信息

        int page = resourceSupplierDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceSupplierDto.setPage((page - 1) * resourceSupplierDto.getRow());
        }

        List<ResourceSupplierDto> resourceSuppliers = BeanConvertUtil.covertBeanList(resourceSupplierServiceDaoImpl.getResourceSupplierInfo(BeanConvertUtil.beanCovertMap(resourceSupplierDto)), ResourceSupplierDto.class);

        if (resourceSuppliers == null || resourceSuppliers.size() == 0) {
            return resourceSuppliers;
        }

        String[] userIds = getUserIds(resourceSuppliers);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ResourceSupplierDto resourceSupplier : resourceSuppliers) {
            refreshResourceSupplier(resourceSupplier, users);
        }
        return resourceSuppliers;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param resourceSupplier 小区物品供应商信息
     * @param users            用户列表
     */
    private void refreshResourceSupplier(ResourceSupplierDto resourceSupplier, List<UserDto> users) {
        for (UserDto user : users) {
            if (resourceSupplier.getRsId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, resourceSupplier);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param resourceSuppliers 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ResourceSupplierDto> resourceSuppliers) {
        List<String> userIds = new ArrayList<String>();
        for (ResourceSupplierDto resourceSupplier : resourceSuppliers) {
            userIds.add(resourceSupplier.getRsId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryResourceSuppliersCount(@RequestBody ResourceSupplierDto resourceSupplierDto) {
        return resourceSupplierServiceDaoImpl.queryResourceSuppliersCount(BeanConvertUtil.beanCovertMap(resourceSupplierDto));
    }

    public IResourceSupplierServiceDao getResourceSupplierServiceDaoImpl() {
        return resourceSupplierServiceDaoImpl;
    }

    public void setResourceSupplierServiceDaoImpl(IResourceSupplierServiceDao resourceSupplierServiceDaoImpl) {
        this.resourceSupplierServiceDaoImpl = resourceSupplierServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
