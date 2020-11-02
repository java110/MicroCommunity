package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeOrderCart.StoreOrderCartDto;
import com.java110.goods.dao.IStoreOrderCartServiceDao;
import com.java110.intf.IStoreOrderCartInnerServiceSMO;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 订单购物车内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreOrderCartInnerServiceSMOImpl extends BaseServiceSMO implements IStoreOrderCartInnerServiceSMO {

    @Autowired
    private IStoreOrderCartServiceDao storeOrderCartServiceDaoImpl;


    @Override
    public int saveStoreOrderCart(@RequestBody StoreOrderCartPo storeOrderCartPo) {
        int saveFlag = 1;
        storeOrderCartServiceDaoImpl.saveStoreOrderCartInfo(BeanConvertUtil.beanCovertMap(storeOrderCartPo));
        return saveFlag;
    }

    @Override
    public int updateStoreOrderCart(@RequestBody StoreOrderCartPo storeOrderCartPo) {
        int saveFlag = 1;
        storeOrderCartServiceDaoImpl.updateStoreOrderCartInfo(BeanConvertUtil.beanCovertMap(storeOrderCartPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreOrderCart(@RequestBody StoreOrderCartPo storeOrderCartPo) {
        int saveFlag = 1;
        storeOrderCartPo.setStatusCd("1");
        storeOrderCartServiceDaoImpl.updateStoreOrderCartInfo(BeanConvertUtil.beanCovertMap(storeOrderCartPo));
        return saveFlag;
    }

    @Override
    public List<StoreOrderCartDto> queryStoreOrderCarts(@RequestBody StoreOrderCartDto storeOrderCartDto) {

        //校验是否传了 分页信息

        int page = storeOrderCartDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeOrderCartDto.setPage((page - 1) * storeOrderCartDto.getRow());
        }

        List<StoreOrderCartDto> storeOrderCarts = BeanConvertUtil.covertBeanList(storeOrderCartServiceDaoImpl.getStoreOrderCartInfo(BeanConvertUtil.beanCovertMap(storeOrderCartDto)), StoreOrderCartDto.class);

        return storeOrderCarts;
    }


    @Override
    public int queryStoreOrderCartsCount(@RequestBody StoreOrderCartDto storeOrderCartDto) {
        return storeOrderCartServiceDaoImpl.queryStoreOrderCartsCount(BeanConvertUtil.beanCovertMap(storeOrderCartDto));
    }

    public IStoreOrderCartServiceDao getStoreOrderCartServiceDaoImpl() {
        return storeOrderCartServiceDaoImpl;
    }

    public void setStoreOrderCartServiceDaoImpl(IStoreOrderCartServiceDao storeOrderCartServiceDaoImpl) {
        this.storeOrderCartServiceDaoImpl = storeOrderCartServiceDaoImpl;
    }
}
