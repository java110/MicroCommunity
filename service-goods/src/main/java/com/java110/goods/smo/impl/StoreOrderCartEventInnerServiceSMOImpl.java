package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeOrderCartEvent.StoreOrderCartEventDto;
import com.java110.goods.dao.IStoreOrderCartEventServiceDao;
import com.java110.intf.goods.IStoreOrderCartEventInnerServiceSMO;
import com.java110.po.storeOrderCartEvent.StoreOrderCartEventPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 购物车事件内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreOrderCartEventInnerServiceSMOImpl extends BaseServiceSMO implements IStoreOrderCartEventInnerServiceSMO {

    @Autowired
    private IStoreOrderCartEventServiceDao storeOrderCartEventServiceDaoImpl;


    @Override
    public int saveStoreOrderCartEvent(@RequestBody StoreOrderCartEventPo storeOrderCartEventPo) {
        int saveFlag = 1;
        storeOrderCartEventServiceDaoImpl.saveStoreOrderCartEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartEventPo));
        return saveFlag;
    }

    @Override
    public int updateStoreOrderCartEvent(@RequestBody StoreOrderCartEventPo storeOrderCartEventPo) {
        int saveFlag = 1;
        storeOrderCartEventServiceDaoImpl.updateStoreOrderCartEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartEventPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreOrderCartEvent(@RequestBody StoreOrderCartEventPo storeOrderCartEventPo) {
        int saveFlag = 1;
        storeOrderCartEventServiceDaoImpl.updateStoreOrderCartEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartEventPo));
        return saveFlag;
    }

    @Override
    public List<StoreOrderCartEventDto> queryStoreOrderCartEvents(@RequestBody StoreOrderCartEventDto storeOrderCartEventDto) {

        //校验是否传了 分页信息

        int page = storeOrderCartEventDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeOrderCartEventDto.setPage((page - 1) * storeOrderCartEventDto.getRow());
        }

        List<StoreOrderCartEventDto> storeOrderCartEvents = BeanConvertUtil.covertBeanList(storeOrderCartEventServiceDaoImpl.getStoreOrderCartEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartEventDto)), StoreOrderCartEventDto.class);

        return storeOrderCartEvents;
    }


    @Override
    public int queryStoreOrderCartEventsCount(@RequestBody StoreOrderCartEventDto storeOrderCartEventDto) {
        return storeOrderCartEventServiceDaoImpl.queryStoreOrderCartEventsCount(BeanConvertUtil.beanCovertMap(storeOrderCartEventDto));
    }

    public IStoreOrderCartEventServiceDao getStoreOrderCartEventServiceDaoImpl() {
        return storeOrderCartEventServiceDaoImpl;
    }

    public void setStoreOrderCartEventServiceDaoImpl(IStoreOrderCartEventServiceDao storeOrderCartEventServiceDaoImpl) {
        this.storeOrderCartEventServiceDaoImpl = storeOrderCartEventServiceDaoImpl;
    }
}
