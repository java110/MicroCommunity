package com.java110.fee.bmo.applyRoomDiscount.impl;

import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.fee.bmo.applyRoomDiscount.IGetApplyRoomDiscountBMO;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getApplyRoomDiscountBMOImpl")
public class GetApplyRoomDiscountBMOImpl implements IGetApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ApplyRoomDiscountDto applyRoomDiscountDto) {


        int count = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscountsCount(applyRoomDiscountDto);

        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = null;
        if (count > 0) {
            applyRoomDiscountDtos = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
        } else {
            applyRoomDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) applyRoomDiscountDto.getRow()), count, applyRoomDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
