package com.java110.fee.bmo.payFeeAudit;

import com.java110.po.payFeeAudit.PayFeeAuditPo;
import org.springframework.http.ResponseEntity;

public interface ISavePayFeeAuditBMO {


    /**
     * 添加缴费审核
     * add by wuxw
     *
     * @param payFeeAuditPo
     * @return
     */
    ResponseEntity<String> save(PayFeeAuditPo payFeeAuditPo);


}
