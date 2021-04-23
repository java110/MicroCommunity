package com.java110.fee.bmo.payFeeAudit;

import com.java110.dto.payFeeAudit.PayFeeAuditDto;
import org.springframework.http.ResponseEntity;

public interface IGetPayFeeAuditBMO {


    /**
     * 查询缴费审核
     * add by wuxw
     *
     * @param payFeeAuditDto
     * @return
     */
    ResponseEntity<String> get(PayFeeAuditDto payFeeAuditDto);


}
