package com.java110.store.smo.impl;


import com.java110.dto.resourceStoreType.ResourceStoreTypeDto;
import com.java110.intf.store.IResourceStoreTypeInnerServiceSMO;
import com.java110.store.dao.IResourceStoreTypeServiceDao;
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
 * @Description 物品类型内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ResourceStoreTypeInnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreTypeInnerServiceSMO {

    @Autowired
    private IResourceStoreTypeServiceDao resourceResourceStoreTypeTypeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ResourceStoreTypeDto> queryResourceStoreTypes(@RequestBody ResourceStoreTypeDto resourceResourceStoreTypeTypeDto) {

        //校验是否传了 分页信息

        int page = resourceResourceStoreTypeTypeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreTypeTypeDto.setPage((page - 1) * resourceResourceStoreTypeTypeDto.getRow());
        }

        List<ResourceStoreTypeDto> resourceResourceStoreTypeTypes = BeanConvertUtil.covertBeanList(resourceResourceStoreTypeTypeServiceDaoImpl.getResourceStoreTypeInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreTypeTypeDto)), ResourceStoreTypeDto.class);

        if (resourceResourceStoreTypeTypes == null || resourceResourceStoreTypeTypes.size() == 0) {
            return resourceResourceStoreTypeTypes;
        }

        String[] userIds = getUserIds(resourceResourceStoreTypeTypes);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ResourceStoreTypeDto resourceResourceStoreTypeType : resourceResourceStoreTypeTypes) {
            refreshResourceStoreType(resourceResourceStoreTypeType, users);
        }
        return resourceResourceStoreTypeTypes;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param resourceResourceStoreTypeType 小区物品类型信息
     * @param users                         用户列表
     */
    private void refreshResourceStoreType(ResourceStoreTypeDto resourceResourceStoreTypeType, List<UserDto> users) {
        for (UserDto user : users) {
            if (resourceResourceStoreTypeType.getRstId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, resourceResourceStoreTypeType);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param resourceResourceStoreTypeTypes 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ResourceStoreTypeDto> resourceResourceStoreTypeTypes) {
        List<String> userIds = new ArrayList<String>();
        for (ResourceStoreTypeDto resourceResourceStoreTypeType : resourceResourceStoreTypeTypes) {
            userIds.add(resourceResourceStoreTypeType.getRstId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryResourceStoreTypesCount(@RequestBody ResourceStoreTypeDto resourceResourceStoreTypeTypeDto) {
        return resourceResourceStoreTypeTypeServiceDaoImpl.queryResourceStoreTypesCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreTypeTypeDto));
    }

    public IResourceStoreTypeServiceDao getResourceStoreTypeServiceDaoImpl() {
        return resourceResourceStoreTypeTypeServiceDaoImpl;
    }

    public void setResourceStoreTypeServiceDaoImpl(IResourceStoreTypeServiceDao resourceResourceStoreTypeTypeServiceDaoImpl) {
        this.resourceResourceStoreTypeTypeServiceDaoImpl = resourceResourceStoreTypeTypeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
