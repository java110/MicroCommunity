package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.java110.fee.feeMonth.IPayFeeMonth;
import com.java110.intf.fee.IPayFeeMonthInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 月缴费表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class PayFeeMonthInnerServiceSMOImpl extends BaseServiceSMO implements IPayFeeMonthInnerServiceSMO {

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Override
    public int payFeeDetailRefreshFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto) {
        payFeeMonthImpl.payFeeDetailRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto.getFeeId(),
                payFeeDetailRefreshFeeMonthDto.getDetailId(),
                payFeeDetailRefreshFeeMonthDto.getCommunityId());
        return 1;
    }

    @Override
    public int doGeneratorOrRefreshFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto) {
        payFeeMonthImpl.doGeneratorOrRefreshFeeMonth(payFeeDetailRefreshFeeMonthDto.getFeeId(),
                payFeeDetailRefreshFeeMonthDto.getCommunityId());
        return 1;
    }

    @Override
    public int doGeneratorOrRefreshAllFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto) {
        payFeeMonthImpl.doGeneratorOrRefreshAllFeeMonth(payFeeDetailRefreshFeeMonthDto.getCommunityId());
        return 1;
    }

    @Override
    public void doGeneratorFeeMonths(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto) {
        payFeeMonthImpl.doGeneratorFeeMonths(payFeeDetailRefreshFeeMonthDto.getFeeIds(),payFeeDetailRefreshFeeMonthDto.getCommunityId());
    }

    @Override
    public void doGeneratorOweFees(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto) {
        payFeeMonthImpl.doGeneratorOweFees(payFeeDetailRefreshFeeMonthDto.getFeeIds(),payFeeDetailRefreshFeeMonthDto.getCommunityId());
    }
}
