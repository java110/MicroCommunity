package com.java110.fee.bmo.payFeeConfigDiscount.impl;

import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.fee.bmo.payFeeConfigDiscount.IGetPayFeeConfigDiscountBMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPayFeeConfigDiscountBMOImpl")
public class GetPayFeeConfigDiscountBMOImpl implements IGetPayFeeConfigDiscountBMO {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param payFeeConfigDiscountDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PayFeeConfigDiscountDto payFeeConfigDiscountDto) {


        int count = payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscountsCount(payFeeConfigDiscountDto);

        List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos = null;
        if (count > 0) {
            payFeeConfigDiscountDtos = payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscounts(payFeeConfigDiscountDto);
            freshDiscountSpec(payFeeConfigDiscountDtos);
        } else {
            payFeeConfigDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeConfigDiscountDto.getRow()), count, payFeeConfigDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshDiscountSpec(List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos) {

        if (payFeeConfigDiscountDtos == null || payFeeConfigDiscountDtos.size() < 1) {
            return;
        }

        List<String> discountIds = new ArrayList<>();
        for (PayFeeConfigDiscountDto payFeeConfigDiscountDto : payFeeConfigDiscountDtos) {
            discountIds.add(payFeeConfigDiscountDto.getDiscountId());
        }

        FeeDiscountSpecDto tmpFeeDiscountSpecDto = new FeeDiscountSpecDto();

        tmpFeeDiscountSpecDto.setDiscountIds(discountIds.toArray(new String[discountIds.size()]));
        tmpFeeDiscountSpecDto.setCommunityId(payFeeConfigDiscountDtos.get(0).getCommunityId());

        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(tmpFeeDiscountSpecDto);

        if (feeDiscountSpecDtos == null || feeDiscountSpecDtos.size() < 1) {
            return;
        }
        List<FeeDiscountSpecDto> tmpSpecs = null;
        for (PayFeeConfigDiscountDto payFeeConfigDiscountDto : payFeeConfigDiscountDtos) {
            tmpSpecs = new ArrayList<>();
            for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
                if (payFeeConfigDiscountDto.getDiscountId().equals(feeDiscountSpecDto.getDiscountId())) {
                    tmpSpecs.add(feeDiscountSpecDto);
                }
            }
            payFeeConfigDiscountDto.setFeeDiscountSpecs(tmpSpecs);
        }
    }
}
