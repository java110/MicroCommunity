package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.feeDiscount.FeeDiscountDto;
import com.java110.fee.dao.IFeeDiscountServiceDao;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用折扣内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeDiscountInnerServiceSMOImpl extends BaseServiceSMO implements IFeeDiscountInnerServiceSMO {

    @Autowired
    private IFeeDiscountServiceDao feeDiscountServiceDaoImpl;


    @Override
    public int saveFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo) {
        int saveFlag = 1;
        feeDiscountServiceDaoImpl.saveFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountPo));
        return saveFlag;
    }

    @Override
    public int updateFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo) {
        int saveFlag = 1;
        feeDiscountServiceDaoImpl.updateFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountPo));
        return saveFlag;
    }

    @Override
    public int deleteFeeDiscount(@RequestBody FeeDiscountPo feeDiscountPo) {
        int saveFlag = 1;
        feeDiscountPo.setStatusCd("1");
        feeDiscountServiceDaoImpl.updateFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountPo));
        return saveFlag;
    }

    @Override
    public List<FeeDiscountDto> queryFeeDiscounts(@RequestBody FeeDiscountDto feeDiscountDto) {

        //校验是否传了 分页信息

        int page = feeDiscountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeDiscountDto.setPage((page - 1) * feeDiscountDto.getRow());
        }

        List<FeeDiscountDto> feeDiscounts = BeanConvertUtil.covertBeanList(feeDiscountServiceDaoImpl.getFeeDiscountInfo(BeanConvertUtil.beanCovertMap(feeDiscountDto)), FeeDiscountDto.class);

        return feeDiscounts;
    }


    @Override
    public int queryFeeDiscountsCount(@RequestBody FeeDiscountDto feeDiscountDto) {
        return feeDiscountServiceDaoImpl.queryFeeDiscountsCount(BeanConvertUtil.beanCovertMap(feeDiscountDto));
    }

    public IFeeDiscountServiceDao getFeeDiscountServiceDaoImpl() {
        return feeDiscountServiceDaoImpl;
    }

    public void setFeeDiscountServiceDaoImpl(IFeeDiscountServiceDao feeDiscountServiceDaoImpl) {
        this.feeDiscountServiceDaoImpl = feeDiscountServiceDaoImpl;
    }
}
