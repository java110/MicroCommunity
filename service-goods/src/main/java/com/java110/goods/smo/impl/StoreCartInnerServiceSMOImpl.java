package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.goods.dao.IStoreCartServiceDao;
import com.java110.intf.goods.IStoreCartInnerServiceSMO;
import com.java110.po.storeCart.StoreCartPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 购物车内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreCartInnerServiceSMOImpl extends BaseServiceSMO implements IStoreCartInnerServiceSMO {

    @Autowired
    private IStoreCartServiceDao storeCartServiceDaoImpl;


    @Override
    public int saveStoreCart(@RequestBody StoreCartPo storeCartPo) {
        int saveFlag = 1;
        storeCartServiceDaoImpl.saveStoreCartInfo(BeanConvertUtil.beanCovertMap(storeCartPo));
        return saveFlag;
    }

    @Override
    public int updateStoreCart(@RequestBody StoreCartPo storeCartPo) {
        int saveFlag = 1;
        storeCartServiceDaoImpl.updateStoreCartInfo(BeanConvertUtil.beanCovertMap(storeCartPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreCart(@RequestBody StoreCartPo storeCartPo) {
        int saveFlag = 1;
        storeCartPo.setStatusCd("1");
        storeCartServiceDaoImpl.updateStoreCartInfo(BeanConvertUtil.beanCovertMap(storeCartPo));
        return saveFlag;
    }

    @Override
    public List<StoreCartDto> queryStoreCarts(@RequestBody StoreCartDto storeCartDto) {

        //校验是否传了 分页信息

        int page = storeCartDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeCartDto.setPage((page - 1) * storeCartDto.getRow());
        }

        List<StoreCartDto> storeCarts = BeanConvertUtil.covertBeanList(storeCartServiceDaoImpl.getStoreCartInfo(BeanConvertUtil.beanCovertMap(storeCartDto)), StoreCartDto.class);

        return storeCarts;
    }


    @Override
    public int queryStoreCartsCount(@RequestBody StoreCartDto storeCartDto) {
        return storeCartServiceDaoImpl.queryStoreCartsCount(BeanConvertUtil.beanCovertMap(storeCartDto));
    }

    public IStoreCartServiceDao getStoreCartServiceDaoImpl() {
        return storeCartServiceDaoImpl;
    }

    public void setStoreCartServiceDaoImpl(IStoreCartServiceDao storeCartServiceDaoImpl) {
        this.storeCartServiceDaoImpl = storeCartServiceDaoImpl;
    }
}
