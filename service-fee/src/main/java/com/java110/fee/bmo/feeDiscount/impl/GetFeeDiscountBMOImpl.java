package com.java110.fee.bmo.feeDiscount.impl;

import com.java110.dto.feeDiscount.FeeDiscountDto;
import com.java110.fee.bmo.feeDiscount.IGetFeeDiscountBMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeDiscountBMOImpl")
public class GetFeeDiscountBMOImpl implements IGetFeeDiscountBMO {

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    /**
     * @param feeDiscountDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeDiscountDto feeDiscountDto) {


        int count = feeDiscountInnerServiceSMOImpl.queryFeeDiscountsCount(feeDiscountDto);

        List<FeeDiscountDto> feeDiscountDtos = null;
        if (count > 0) {
            feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);
        } else {
            feeDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeDiscountDto.getRow()), count, feeDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
