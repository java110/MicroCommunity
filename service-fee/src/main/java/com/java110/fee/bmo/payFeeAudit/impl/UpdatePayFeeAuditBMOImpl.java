package com.java110.fee.bmo.payFeeAudit.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.fee.bmo.payFeeAudit.IUpdatePayFeeAuditBMO;
import com.java110.intf.fee.IPayFeeAuditInnerServiceSMO;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePayFeeAuditBMOImpl")
public class UpdatePayFeeAuditBMOImpl implements IUpdatePayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    /**
     * @param payFeeAuditPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(PayFeeAuditPo payFeeAuditPo) {

        int flag = payFeeAuditInnerServiceSMOImpl.updatePayFeeAudit(payFeeAuditPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
