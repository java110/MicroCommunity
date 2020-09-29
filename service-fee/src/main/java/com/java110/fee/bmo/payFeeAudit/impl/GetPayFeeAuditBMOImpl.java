package com.java110.fee.bmo.payFeeAudit.impl;

import com.java110.dto.payFeeAudit.PayFeeAuditDto;
import com.java110.fee.bmo.payFeeAudit.IGetPayFeeAuditBMO;
import com.java110.intf.IPayFeeAuditInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPayFeeAuditBMOImpl")
public class GetPayFeeAuditBMOImpl implements IGetPayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    /**
     * @param payFeeAuditDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PayFeeAuditDto payFeeAuditDto) {


        int count = payFeeAuditInnerServiceSMOImpl.queryPayFeeAuditsCount(payFeeAuditDto);

        List<PayFeeAuditDto> payFeeAuditDtos = null;
        if (count > 0) {
            payFeeAuditDtos = payFeeAuditInnerServiceSMOImpl.queryPayFeeAudits(payFeeAuditDto);
        } else {
            payFeeAuditDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeAuditDto.getRow()), count, payFeeAuditDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
