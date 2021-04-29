package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.goods.dao.IStoreOrderServiceDao;
import com.java110.intf.goods.IStoreOrderInnerServiceSMO;
import com.java110.po.storeOrder.StoreOrderPo;
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
public class StoreOrderInnerServiceSMOImpl extends BaseServiceSMO implements IStoreOrderInnerServiceSMO {

    @Autowired
    private IStoreOrderServiceDao storeOrderServiceDaoImpl;


    @Override
    public int saveStoreOrder(@RequestBody StoreOrderPo storeOrderPo) {
        int saveFlag = 1;
        storeOrderServiceDaoImpl.saveStoreOrderInfo(BeanConvertUtil.beanCovertMap(storeOrderPo));
        return saveFlag;
    }

    @Override
    public int updateStoreOrder(@RequestBody StoreOrderPo storeOrderPo) {
        int saveFlag = 1;
        storeOrderServiceDaoImpl.updateStoreOrderInfo(BeanConvertUtil.beanCovertMap(storeOrderPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreOrder(@RequestBody StoreOrderPo storeOrderPo) {
        int saveFlag = 1;
        storeOrderPo.setStatusCd("1");
        storeOrderServiceDaoImpl.updateStoreOrderInfo(BeanConvertUtil.beanCovertMap(storeOrderPo));
        return saveFlag;
    }

    @Override
    public List<StoreOrderDto> queryStoreOrders(@RequestBody StoreOrderDto storeOrderDto) {

        //校验是否传了 分页信息

        int page = storeOrderDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeOrderDto.setPage((page - 1) * storeOrderDto.getRow());
        }

        List<StoreOrderDto> storeOrders = BeanConvertUtil.covertBeanList(storeOrderServiceDaoImpl.getStoreOrderInfo(BeanConvertUtil.beanCovertMap(storeOrderDto)), StoreOrderDto.class);

        return storeOrders;
    }


    @Override
    public int queryStoreOrdersCount(@RequestBody StoreOrderDto storeOrderDto) {
        return storeOrderServiceDaoImpl.queryStoreOrdersCount(BeanConvertUtil.beanCovertMap(storeOrderDto));
    }

    public IStoreOrderServiceDao getStoreOrderServiceDaoImpl() {
        return storeOrderServiceDaoImpl;
    }

    public void setStoreOrderServiceDaoImpl(IStoreOrderServiceDao storeOrderServiceDaoImpl) {
        this.storeOrderServiceDaoImpl = storeOrderServiceDaoImpl;
    }
}
