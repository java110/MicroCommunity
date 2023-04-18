package com.java110.fee.bmo.applyRoomDiscountType.impl;

import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountTypeDto;
import com.java110.fee.bmo.applyRoomDiscountType.IGetApplyRoomDiscountTypeBMO;
import com.java110.intf.fee.IApplyRoomDiscountTypeInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getApplyRoomDiscountTypeBMOImpl")
public class GetApplyRoomDiscountTypeBMOImpl implements IGetApplyRoomDiscountTypeBMO {

    @Autowired
    private IApplyRoomDiscountTypeInnerServiceSMO applyRoomDiscountTypeInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountTypeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto) {


        int count = applyRoomDiscountTypeInnerServiceSMOImpl.queryApplyRoomDiscountTypesCount(applyRoomDiscountTypeDto);

        List<ApplyRoomDiscountTypeDto> applyRoomDiscountTypeDtos = null;
        if (count > 0) {
            applyRoomDiscountTypeDtos = applyRoomDiscountTypeInnerServiceSMOImpl.queryApplyRoomDiscountTypes(applyRoomDiscountTypeDto);
        } else {
            applyRoomDiscountTypeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) applyRoomDiscountTypeDto.getRow()), count, applyRoomDiscountTypeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
