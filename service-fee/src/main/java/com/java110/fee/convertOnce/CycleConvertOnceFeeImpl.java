package com.java110.fee.convertOnce;

import com.java110.dto.fee.FeeDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.fee.PayFeePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换一次性费用
 */

@Service
public class CycleConvertOnceFeeImpl implements ICycleConvertOnceFee {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public int convertPayFees(List<PayFeePo> payFeePos) {

        List<PayFeePo> tmpPayFeePos = new ArrayList<>();
        for (PayFeePo tmpPayFeePo : payFeePos) {
            //todo 一次性费用 直接跳过
            if (FeeDto.FEE_FLAG_ONCE.equals(tmpPayFeePo.getFeeFlag())) {
                continue;
            }

            tmpPayFeePos.add(tmpPayFeePo);
        }
        if (tmpPayFeePos.isEmpty()) {
            return 0;
        }

        //todo 业务处理


        return 0;
    }

    @Override
    public int convertPayFee(PayFeePo payFeePo) {
        List<PayFeePo> payFeePos = new ArrayList<>();
        payFeePos.add(payFeePo);
        return convertPayFees(payFeePos);
    }

    @Override
    public int covertCommunityPayFee(String communityId) {
        return 0;
    }

    @Override
    public int covertRuleIdsPayFee(List<String> ruleIds) {
        return 0;
    }
}
