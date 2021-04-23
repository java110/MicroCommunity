package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.storeOrderCartReturn.StoreOrderCartReturnDto;
import com.java110.goods.dao.IStoreOrderCartReturnServiceDao;
import com.java110.intf.goods.IStoreOrderCartReturnInnerServiceSMO;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
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
public class StoreOrderCartReturnInnerServiceSMOImpl extends BaseServiceSMO implements IStoreOrderCartReturnInnerServiceSMO {

    @Autowired
    private IStoreOrderCartReturnServiceDao storeOrderCartReturnServiceDaoImpl;


    @Override
    public int saveStoreOrderCartReturn(@RequestBody StoreOrderCartReturnPo storeOrderCartReturnPo) {
        int saveFlag = 1;
        storeOrderCartReturnServiceDaoImpl.saveStoreOrderCartReturnInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnPo));
        return saveFlag;
    }

    @Override
    public int updateStoreOrderCartReturn(@RequestBody StoreOrderCartReturnPo storeOrderCartReturnPo) {
        int saveFlag = 1;
        storeOrderCartReturnServiceDaoImpl.updateStoreOrderCartReturnInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnPo));
        return saveFlag;
    }

    @Override
    public int deleteStoreOrderCartReturn(@RequestBody StoreOrderCartReturnPo storeOrderCartReturnPo) {
        int saveFlag = 1;
        storeOrderCartReturnPo.setStatusCd("1");
        storeOrderCartReturnServiceDaoImpl.updateStoreOrderCartReturnInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnPo));
        return saveFlag;
    }

    @Override
    public List<StoreOrderCartReturnDto> queryStoreOrderCartReturns(@RequestBody StoreOrderCartReturnDto storeOrderCartReturnDto) {

        //校验是否传了 分页信息

        int page = storeOrderCartReturnDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            storeOrderCartReturnDto.setPage((page - 1) * storeOrderCartReturnDto.getRow());
        }

        List<StoreOrderCartReturnDto> storeOrderCartReturns = BeanConvertUtil.covertBeanList(storeOrderCartReturnServiceDaoImpl.getStoreOrderCartReturnInfo(BeanConvertUtil.beanCovertMap(storeOrderCartReturnDto)), StoreOrderCartReturnDto.class);

        return storeOrderCartReturns;
    }


    @Override
    public int queryStoreOrderCartReturnsCount(@RequestBody StoreOrderCartReturnDto storeOrderCartReturnDto) {
        return storeOrderCartReturnServiceDaoImpl.queryStoreOrderCartReturnsCount(BeanConvertUtil.beanCovertMap(storeOrderCartReturnDto));
    }

    public IStoreOrderCartReturnServiceDao getStoreOrderCartReturnServiceDaoImpl() {
        return storeOrderCartReturnServiceDaoImpl;
    }

    public void setStoreOrderCartReturnServiceDaoImpl(IStoreOrderCartReturnServiceDao storeOrderCartReturnServiceDaoImpl) {
        this.storeOrderCartReturnServiceDaoImpl = storeOrderCartReturnServiceDaoImpl;
    }
}
