package com.java110.fee.bmo.feeDiscountRuleSpec.impl;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.fee.bmo.feeDiscountRuleSpec.IComputeFeeDiscountBMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("computeFeeDiscountBMOImpl")
public class ComputeFeeDiscountBMOImpl implements IComputeFeeDiscountBMO {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;


    /**
     * 计算折扣
     *
     * @param feeId
     * @param communityId
     * @param cycles
     * @param page
     * @param row
     * @return
     */
    @Override
    public ResponseEntity<String> compute(String feeId, String communityId, double cycles, int page, int row) {

        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(communityId);
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "费用不存在");

        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(feeDtos.get(0).getConfigId());
        payFeeConfigDiscountDto.setCommunityId(communityId);
        List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos =
                payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscounts(payFeeConfigDiscountDto);

        if (payFeeConfigDiscountDtos == null || payFeeConfigDiscountDtos.size() < 1) {
            return ResultVo.success();
        }


        for(PayFeeConfigDiscountDto tmpPayFeeConfigDiscountDto : payFeeConfigDiscountDtos){

        }

        return null;
    }
}
