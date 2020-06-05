package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.store.IResourceStoreInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.store.dao.IResourceStoreServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 资源内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ResourceStoreInnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreInnerServiceSMO {

    @Autowired
    private IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ResourceStoreDto> queryResourceStores(@RequestBody ResourceStoreDto resourceResourceStoreDto) {

        //校验是否传了 分页信息

        int page = resourceResourceStoreDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreDto.setPage((page - 1) * resourceResourceStoreDto.getRow());
        }

        List<ResourceStoreDto> resourceResourceStores = BeanConvertUtil.covertBeanList(resourceResourceStoreServiceDaoImpl.getResourceStoreInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreDto)), ResourceStoreDto.class);

        return resourceResourceStores;
    }


    @Override
    public int queryResourceStoresCount(@RequestBody ResourceStoreDto resourceResourceStoreDto) {
        return resourceResourceStoreServiceDaoImpl.queryResourceStoresCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreDto));
    }

    public IResourceStoreServiceDao getResourceStoreServiceDaoImpl() {
        return resourceResourceStoreServiceDaoImpl;
    }

    public void setResourceStoreServiceDaoImpl(IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl) {
        this.resourceResourceStoreServiceDaoImpl = resourceResourceStoreServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
