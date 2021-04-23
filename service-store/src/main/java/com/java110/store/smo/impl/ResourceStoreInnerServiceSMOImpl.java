package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.store.dao.IResourceStoreServiceDao;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public int updateResourceStore(@RequestBody ResourceStorePo resourceStorePo) {
        //查询
        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + resourceStorePo.getResId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            Map info = new HashMap<>();
            info.put("resId", resourceStorePo.getResId());
            info.put("storeId", resourceStorePo.getStoreId());
            List<Map> stores = resourceResourceStoreServiceDaoImpl.getResourceStoreInfo(info);

            Assert.listOnlyOne(stores, "不存在该物品");
            int stock = Integer.parseInt(stores.get(0).get("stock").toString());
            int newStock = Integer.parseInt(resourceStorePo.getStock());
            int totalStock = stock + newStock;

            if (totalStock < 0) {
                throw new IllegalArgumentException("库存不足，参数有误");
            }
            resourceStorePo.setStock(totalStock + "");
            resourceStorePo.setStatusCd("0");
            return resourceResourceStoreServiceDaoImpl.updateResourceStoreInfoInstance(BeanConvertUtil.beanCovertMap(resourceStorePo));
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
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
