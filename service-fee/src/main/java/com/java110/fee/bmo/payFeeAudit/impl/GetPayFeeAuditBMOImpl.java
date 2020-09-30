package com.java110.fee.bmo.payFeeAudit.impl;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.order.BusinessDto;
import com.java110.dto.order.OrderDto;
import com.java110.dto.payFeeAudit.PayFeeAuditDto;
import com.java110.fee.bmo.payFeeAudit.IGetPayFeeAuditBMO;
import com.java110.intf.IPayFeeAuditInnerServiceSMO;
import com.java110.intf.order.IOrderInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
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

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;

    /**
     * @param payFeeAuditDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PayFeeAuditDto payFeeAuditDto) {


        int count = payFeeAuditInnerServiceSMOImpl.queryPayFeeAuditsCount(payFeeAuditDto);

        List<PayFeeAuditDto> payFeeAuditDtos = null;
        if (count > 0) {
            payFeeAuditDtos = payFeeAuditInnerServiceSMOImpl.queryPayFeeAudits(payFeeAuditDto);

            frashRoomAndStaff(payFeeAuditDtos);
        } else {
            payFeeAuditDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeAuditDto.getRow()), count, payFeeAuditDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void frashRoomAndStaff(List<PayFeeAuditDto> payFeeAuditDtos) {

        List<FeeDto> feeDtos = BeanConvertUtil.covertBeanList(payFeeAuditDtos, FeeDto.class);
        computeFeeSMOImpl.freshFeeObjName(feeDtos);
        List<String> bIds = new ArrayList<>();
        for (PayFeeAuditDto payFeeAuditDto : payFeeAuditDtos) {
            for (FeeDto feeDto : feeDtos) {
                if (payFeeAuditDto.getFeeId().equals(feeDto.getFeeId())) {
                    payFeeAuditDto.setPayerObjName(feeDto.getPayerObjName());
                }
            }

            if (StringUtil.isEmpty(payFeeAuditDto.getbId()) || payFeeAuditDto.getbId().startsWith("-")) {
                continue;
            }

            bIds.add(payFeeAuditDto.getbId());
        }
        BusinessDto businessDto = new BusinessDto();
        businessDto.setbIds(bIds.toArray(new String[bIds.size()]));
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOrderByBId(businessDto);

        for (PayFeeAuditDto payFeeAuditDto : payFeeAuditDtos) {
            for (OrderDto orderDto : orderDtos) {
                if (payFeeAuditDto.getbId().equals(orderDto.getbId())) {
                    payFeeAuditDto.setUserId(orderDto.getUserId());
                    payFeeAuditDto.setUserName(orderDto.getUserName());
                }
            }
        }


    }

}
