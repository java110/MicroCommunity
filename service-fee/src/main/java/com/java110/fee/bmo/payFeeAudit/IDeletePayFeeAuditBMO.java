package com.java110.fee.bmo.payFeeAudit;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import org.springframework.http.ResponseEntity;

public interface IDeletePayFeeAuditBMO {


    /**
     * 修改缴费审核
     * add by wuxw
     * @param payFeeAuditPo
     * @return
     */
    ResponseEntity<String> delete(PayFeeAuditPo payFeeAuditPo);


}
