package com.java110.fee.bmo.payFeeDetailMonth.impl;

import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.fee.bmo.payFeeDetailMonth.IGetPayFeeDetailMonthBMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPayFeeDetailMonthBMOImpl")
public class GetPayFeeDetailMonthBMOImpl implements IGetPayFeeDetailMonthBMO {

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    /**
     * @param payFeeDetailMonthDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PayFeeDetailMonthDto payFeeDetailMonthDto) {


        int count = payFeeDetailMonthInnerServiceSMOImpl.queryPayFeeDetailMonthsCount(payFeeDetailMonthDto);

        List<PayFeeDetailMonthDto> payFeeDetailMonthDtos = null;
        if (count > 0) {
            payFeeDetailMonthDtos = payFeeDetailMonthInnerServiceSMOImpl.queryPayFeeDetailMonths(payFeeDetailMonthDto);
        } else {
            payFeeDetailMonthDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeDetailMonthDto.getRow()), count, payFeeDetailMonthDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
