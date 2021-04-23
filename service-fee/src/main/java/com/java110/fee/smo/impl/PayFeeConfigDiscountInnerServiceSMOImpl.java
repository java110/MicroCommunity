package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.fee.dao.IPayFeeConfigDiscountServiceDao;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用项折扣内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PayFeeConfigDiscountInnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeConfigDiscountInnerServiceSMO {

    @Autowired
    private IPayFeeConfigDiscountServiceDao payFeeConfigDiscountServiceDaoImpl;


    @Override
    public int savePayFeeConfigDiscount(@RequestBody PayFeeConfigDiscountPo payFeeConfigDiscountPo) {
        int saveFlag = 1;
        payFeeConfigDiscountServiceDaoImpl.savePayFeeConfigDiscountInfo(BeanConvertUtil.beanCovertMap(payFeeConfigDiscountPo));
        return saveFlag;
    }

    @Override
    public int updatePayFeeConfigDiscount(@RequestBody PayFeeConfigDiscountPo payFeeConfigDiscountPo) {
        int saveFlag = 1;
        payFeeConfigDiscountServiceDaoImpl.updatePayFeeConfigDiscountInfo(BeanConvertUtil.beanCovertMap(payFeeConfigDiscountPo));
        return saveFlag;
    }

    @Override
    public int deletePayFeeConfigDiscount(@RequestBody PayFeeConfigDiscountPo payFeeConfigDiscountPo) {
        int saveFlag = 1;
        payFeeConfigDiscountPo.setStatusCd("1");
        payFeeConfigDiscountServiceDaoImpl.updatePayFeeConfigDiscountInfo(BeanConvertUtil.beanCovertMap(payFeeConfigDiscountPo));
        return saveFlag;
    }

    @Override
    public List<PayFeeConfigDiscountDto> queryPayFeeConfigDiscounts(@RequestBody PayFeeConfigDiscountDto payFeeConfigDiscountDto) {

        //校验是否传了 分页信息

        int page = payFeeConfigDiscountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            payFeeConfigDiscountDto.setPage((page - 1) * payFeeConfigDiscountDto.getRow());
        }

        List<PayFeeConfigDiscountDto> payFeeConfigDiscounts = BeanConvertUtil.covertBeanList(payFeeConfigDiscountServiceDaoImpl.getPayFeeConfigDiscountInfo(BeanConvertUtil.beanCovertMap(payFeeConfigDiscountDto)), PayFeeConfigDiscountDto.class);

        return payFeeConfigDiscounts;
    }


    @Override
    public int queryPayFeeConfigDiscountsCount(@RequestBody PayFeeConfigDiscountDto payFeeConfigDiscountDto) {
        return payFeeConfigDiscountServiceDaoImpl.queryPayFeeConfigDiscountsCount(BeanConvertUtil.beanCovertMap(payFeeConfigDiscountDto));
    }

    public IPayFeeConfigDiscountServiceDao getPayFeeConfigDiscountServiceDaoImpl() {
        return payFeeConfigDiscountServiceDaoImpl;
    }

    public void setPayFeeConfigDiscountServiceDaoImpl(IPayFeeConfigDiscountServiceDao payFeeConfigDiscountServiceDaoImpl) {
        this.payFeeConfigDiscountServiceDaoImpl = payFeeConfigDiscountServiceDaoImpl;
    }
}
