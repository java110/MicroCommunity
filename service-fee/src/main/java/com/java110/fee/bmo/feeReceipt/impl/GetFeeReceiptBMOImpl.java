package com.java110.fee.bmo.feeReceipt.impl;

import com.java110.dto.feeReceipt.FeeReceiptDto;
import com.java110.fee.bmo.feeReceipt.IGetFeeReceiptBMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeReceiptBMOImpl")
public class GetFeeReceiptBMOImpl implements IGetFeeReceiptBMO {

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    /**
     * @param feeReceiptDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeReceiptDto feeReceiptDto) {


        int count = feeReceiptInnerServiceSMOImpl.queryFeeReceiptsCount(feeReceiptDto);

        List<FeeReceiptDto> feeReceiptDtos = null;
        if (count > 0) {
            feeReceiptDtos = feeReceiptInnerServiceSMOImpl.queryFeeReceipts(feeReceiptDto);
        } else {
            feeReceiptDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDto.getRow()), count, feeReceiptDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
