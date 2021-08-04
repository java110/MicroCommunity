package com.java110.store.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeAds.StoreAdsDto;
import com.java110.intf.store.IStoreAdsInnerServiceSMO;
import com.java110.po.storeAds.StoreAdsPo;
import com.java110.store.dao.IStoreAdsServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 商户广告内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StoreAdsInnerServiceSMOImpl extends BaseServiceSMO implements IStoreAdsInnerServiceSMO {

    @Autowired
    private IStoreAdsServiceDao storeAdsServiceDaoImpl;


    @Override
    public int saveStoreAds(@RequestBody StoreAdsPo storeAdsPo) {
        int saveFlag = 1;
        storeAdsServiceDaoImpl.saveStoreAdsInfo(BeanConvertUtil.beanCovertMap(storeAdsPo));
        return saveFlag;
    }

    @Override
    public int updateStoreAds(@RequestBody StoreAdsPo storeAdsPo) {
        int saveFlag = 1;
        storeAdsServiceDaoImpl.updateStoreAdsInfo(BeanConvertUtil.beanCovertMap(storeAdsPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreAds(@RequestBody StoreAdsPo storeAdsPo) {
        int saveFlag = 1;
        storeAdsPo.setStatusCd("1");
        storeAdsServiceDaoImpl.updateStoreAdsInfo(BeanConvertUtil.beanCovertMap(storeAdsPo));
        return saveFlag;
    }

    @Override
    public List<StoreAdsDto> queryStoreAdss(@RequestBody StoreAdsDto storeAdsDto) {

        //校验是否传了 分页信息

        int page = storeAdsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeAdsDto.setPage((page - 1) * storeAdsDto.getRow());
        }

        List<StoreAdsDto> storeAdss = BeanConvertUtil.covertBeanList(storeAdsServiceDaoImpl.getStoreAdsInfo(BeanConvertUtil.beanCovertMap(storeAdsDto)), StoreAdsDto.class);

        return storeAdss;
    }


    @Override
    public int queryStoreAdssCount(@RequestBody StoreAdsDto storeAdsDto) {
        return storeAdsServiceDaoImpl.queryStoreAdssCount(BeanConvertUtil.beanCovertMap(storeAdsDto));
    }

    public IStoreAdsServiceDao getStoreAdsServiceDaoImpl() {
        return storeAdsServiceDaoImpl;
    }

    public void setStoreAdsServiceDaoImpl(IStoreAdsServiceDao storeAdsServiceDaoImpl) {
        this.storeAdsServiceDaoImpl = storeAdsServiceDaoImpl;
    }
}
