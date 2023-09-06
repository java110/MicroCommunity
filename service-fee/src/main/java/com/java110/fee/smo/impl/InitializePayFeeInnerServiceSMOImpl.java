package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.fee.dao.IInitializePayFeeServiceDao;
import com.java110.intf.fee.IInitializePayFeeInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 缴费审核内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InitializePayFeeInnerServiceSMOImpl extends BaseServiceSMO implements IInitializePayFeeInnerServiceSMO {

    @Autowired
    private IInitializePayFeeServiceDao initializePayFeeServiceDaoImpl;


    @Override
    public int deletePayFee(@RequestBody Map communityId) {
        int deleteFee = initializePayFeeServiceDaoImpl.deletePayFee(communityId);

        //todo 删除缴费明细
        initializePayFeeServiceDaoImpl.deletePayFeeDetail(communityId);

        //todo 删除 欠费表
        initializePayFeeServiceDaoImpl.deleteReportOweFee(communityId);

        //todo 删除 月报表
        initializePayFeeServiceDaoImpl.deletePayFeeDetailMonth(communityId);

        return deleteFee;
    }

    public IInitializePayFeeServiceDao getInitializePayFeeServiceDaoImpl() {
        return initializePayFeeServiceDaoImpl;
    }

    public void setInitializePayFeeServiceDaoImpl(IInitializePayFeeServiceDao initializePayFeeServiceDaoImpl) {
        this.initializePayFeeServiceDaoImpl = initializePayFeeServiceDaoImpl;
    }
}
