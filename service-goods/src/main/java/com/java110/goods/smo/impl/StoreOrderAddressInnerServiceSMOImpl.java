package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeOrderAddress.StoreOrderAddressDto;
import com.java110.goods.dao.IStoreOrderAddressServiceDao;
import com.java110.intf.IStoreOrderAddressInnerServiceSMO;
import com.java110.po.storeOrderAddress.StoreOrderAddressPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 发货地址内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreOrderAddressInnerServiceSMOImpl extends BaseServiceSMO implements IStoreOrderAddressInnerServiceSMO {

    @Autowired
    private IStoreOrderAddressServiceDao storeOrderAddressServiceDaoImpl;


    @Override
    public int saveStoreOrderAddress(@RequestBody StoreOrderAddressPo storeOrderAddressPo) {
        int saveFlag = 1;
        storeOrderAddressServiceDaoImpl.saveStoreOrderAddressInfo(BeanConvertUtil.beanCovertMap(storeOrderAddressPo));
        return saveFlag;
    }

    @Override
    public int updateStoreOrderAddress(@RequestBody StoreOrderAddressPo storeOrderAddressPo) {
        int saveFlag = 1;
        storeOrderAddressServiceDaoImpl.updateStoreOrderAddressInfo(BeanConvertUtil.beanCovertMap(storeOrderAddressPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreOrderAddress(@RequestBody StoreOrderAddressPo storeOrderAddressPo) {
        int saveFlag = 1;
        storeOrderAddressPo.setStatusCd("1");
        storeOrderAddressServiceDaoImpl.updateStoreOrderAddressInfo(BeanConvertUtil.beanCovertMap(storeOrderAddressPo));
        return saveFlag;
    }

    @Override
    public List<StoreOrderAddressDto> queryStoreOrderAddresss(@RequestBody StoreOrderAddressDto storeOrderAddressDto) {

        //校验是否传了 分页信息

        int page = storeOrderAddressDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeOrderAddressDto.setPage((page - 1) * storeOrderAddressDto.getRow());
        }

        List<StoreOrderAddressDto> storeOrderAddresss = BeanConvertUtil.covertBeanList(storeOrderAddressServiceDaoImpl.getStoreOrderAddressInfo(BeanConvertUtil.beanCovertMap(storeOrderAddressDto)), StoreOrderAddressDto.class);

        return storeOrderAddresss;
    }


    @Override
    public int queryStoreOrderAddresssCount(@RequestBody StoreOrderAddressDto storeOrderAddressDto) {
        return storeOrderAddressServiceDaoImpl.queryStoreOrderAddresssCount(BeanConvertUtil.beanCovertMap(storeOrderAddressDto));
    }

    public IStoreOrderAddressServiceDao getStoreOrderAddressServiceDaoImpl() {
        return storeOrderAddressServiceDaoImpl;
    }

    public void setStoreOrderAddressServiceDaoImpl(IStoreOrderAddressServiceDao storeOrderAddressServiceDaoImpl) {
        this.storeOrderAddressServiceDaoImpl = storeOrderAddressServiceDaoImpl;
    }
}
