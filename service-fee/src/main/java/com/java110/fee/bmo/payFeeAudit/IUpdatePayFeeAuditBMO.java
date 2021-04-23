package com.java110.fee.bmo.payFeeAudit;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePayFeeAuditBMO {


    /**
     * 修改缴费审核
     * add by wuxw
     * @param payFeeAuditPo
     * @return
     */
    ResponseEntity<String> update(PayFeeAuditPo payFeeAuditPo);


}
