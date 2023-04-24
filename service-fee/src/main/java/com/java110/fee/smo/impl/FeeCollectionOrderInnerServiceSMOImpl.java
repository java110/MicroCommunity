package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.fee.FeeCollectionOrderDto;
import com.java110.fee.dao.IFeeCollectionOrderServiceDao;
import com.java110.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.java110.po.feeCollectionOrder.FeeCollectionOrderPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 催缴单内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeCollectionOrderInnerServiceSMOImpl extends BaseServiceSMO implements IFeeCollectionOrderInnerServiceSMO {

    @Autowired
    private IFeeCollectionOrderServiceDao feeCollectionOrderServiceDaoImpl;


    @Override
    public int saveFeeCollectionOrder(@RequestBody FeeCollectionOrderPo feeCollectionOrderPo) {
        int saveFlag = 1;
        feeCollectionOrderServiceDaoImpl.saveFeeCollectionOrderInfo(BeanConvertUtil.beanCovertMap(feeCollectionOrderPo));
        return saveFlag;
    }

    @Override
    public int updateFeeCollectionOrder(@RequestBody FeeCollectionOrderPo feeCollectionOrderPo) {
        int saveFlag = 1;
        feeCollectionOrderServiceDaoImpl.updateFeeCollectionOrderInfo(BeanConvertUtil.beanCovertMap(feeCollectionOrderPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeCollectionOrder(@RequestBody FeeCollectionOrderPo feeCollectionOrderPo) {
        int saveFlag = 1;
        feeCollectionOrderPo.setStatusCd("1");
        feeCollectionOrderServiceDaoImpl.updateFeeCollectionOrderInfo(BeanConvertUtil.beanCovertMap(feeCollectionOrderPo));
        return saveFlag;
    }

    @Override
    public List<FeeCollectionOrderDto> queryFeeCollectionOrders(@RequestBody FeeCollectionOrderDto feeCollectionOrderDto) {

        //校验是否传了 分页信息

        int page = feeCollectionOrderDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeCollectionOrderDto.setPage((page - 1) * feeCollectionOrderDto.getRow());
        }

        List<FeeCollectionOrderDto> feeCollectionOrders = BeanConvertUtil.covertBeanList(feeCollectionOrderServiceDaoImpl.getFeeCollectionOrderInfo(BeanConvertUtil.beanCovertMap(feeCollectionOrderDto)), FeeCollectionOrderDto.class);

        return feeCollectionOrders;
    }


    @Override
    public int queryFeeCollectionOrdersCount(@RequestBody FeeCollectionOrderDto feeCollectionOrderDto) {
        return feeCollectionOrderServiceDaoImpl.queryFeeCollectionOrdersCount(BeanConvertUtil.beanCovertMap(feeCollectionOrderDto));
    }

    public IFeeCollectionOrderServiceDao getFeeCollectionOrderServiceDaoImpl() {
        return feeCollectionOrderServiceDaoImpl;
    }

    public void setFeeCollectionOrderServiceDaoImpl(IFeeCollectionOrderServiceDao feeCollectionOrderServiceDaoImpl) {
        this.feeCollectionOrderServiceDaoImpl = feeCollectionOrderServiceDaoImpl;
    }
}
