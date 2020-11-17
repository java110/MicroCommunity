package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeOrderCartReturnEvent.StoreOrderCartReturnEventDto;
import com.java110.goods.dao.IStoreOrderCartReturnEventServiceDao;
import com.java110.intf.goods.IStoreOrderCartReturnEventInnerServiceSMO;
import com.java110.po.storeOrderCartReturnEvent.StoreOrderCartReturnEventPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 退货事件内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreOrderCartReturnEventInnerServiceSMOImpl extends BaseServiceSMO implements IStoreOrderCartReturnEventInnerServiceSMO {

    @Autowired
    private IStoreOrderCartReturnEventServiceDao storeOrderCartReturnEventServiceDaoImpl;


    @Override
    public int saveStoreOrderCartReturnEvent(@RequestBody StoreOrderCartReturnEventPo storeOrderCartReturnEventPo) {
        int saveFlag = 1;
        storeOrderCartReturnEventServiceDaoImpl.saveStoreOrderCartReturnEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnEventPo));
        return saveFlag;
    }

    @Override
    public int updateStoreOrderCartReturnEvent(@RequestBody StoreOrderCartReturnEventPo storeOrderCartReturnEventPo) {
        int saveFlag = 1;
        storeOrderCartReturnEventServiceDaoImpl.updateStoreOrderCartReturnEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnEventPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreOrderCartReturnEvent(@RequestBody StoreOrderCartReturnEventPo storeOrderCartReturnEventPo) {
        int saveFlag = 1;
        storeOrderCartReturnEventServiceDaoImpl.updateStoreOrderCartReturnEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnEventPo));
        return saveFlag;
    }

    @Override
    public List<StoreOrderCartReturnEventDto> queryStoreOrderCartReturnEvents(@RequestBody StoreOrderCartReturnEventDto storeOrderCartReturnEventDto) {

        //校验是否传了 分页信息

        int page = storeOrderCartReturnEventDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeOrderCartReturnEventDto.setPage((page - 1) * storeOrderCartReturnEventDto.getRow());
        }

        List<StoreOrderCartReturnEventDto> storeOrderCartReturnEvents = BeanConvertUtil.covertBeanList(storeOrderCartReturnEventServiceDaoImpl.getStoreOrderCartReturnEventInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnEventDto)), StoreOrderCartReturnEventDto.class);

        return storeOrderCartReturnEvents;
    }


    @Override
    public int queryStoreOrderCartReturnEventsCount(@RequestBody StoreOrderCartReturnEventDto storeOrderCartReturnEventDto) {
        return storeOrderCartReturnEventServiceDaoImpl.queryStoreOrderCartReturnEventsCount(BeanConvertUtil.beanCovertMap(storeOrderCartReturnEventDto));
    }

    public IStoreOrderCartReturnEventServiceDao getStoreOrderCartReturnEventServiceDaoImpl() {
        return storeOrderCartReturnEventServiceDaoImpl;
    }

    public void setStoreOrderCartReturnEventServiceDaoImpl(IStoreOrderCartReturnEventServiceDao storeOrderCartReturnEventServiceDaoImpl) {
        this.storeOrderCartReturnEventServiceDaoImpl = storeOrderCartReturnEventServiceDaoImpl;
    }
}
