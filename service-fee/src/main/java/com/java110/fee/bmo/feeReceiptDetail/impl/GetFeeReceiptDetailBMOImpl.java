package com.java110.fee.bmo.feeReceiptDetail.impl;

import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.fee.bmo.feeReceiptDetail.IGetFeeReceiptDetailBMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeReceiptDetailBMOImpl")
public class GetFeeReceiptDetailBMOImpl implements IGetFeeReceiptDetailBMO {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    /**
     * @param feeReceiptDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeReceiptDetailDto feeReceiptDetailDto) {


        int count = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetailsCount(feeReceiptDetailDto);

        List<FeeReceiptDetailDto> feeReceiptDetailDtos = null;
        if (count > 0) {
            feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);
        } else {
            feeReceiptDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDetailDto.getRow()), count, feeReceiptDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
