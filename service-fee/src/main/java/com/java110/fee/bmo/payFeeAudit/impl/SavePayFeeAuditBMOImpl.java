package com.java110.fee.bmo.payFeeAudit.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.payFeeAudit.ISavePayFeeAuditBMO;
import com.java110.intf.IPayFeeAuditInnerServiceSMO;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePayFeeAuditBMOImpl")
public class SavePayFeeAuditBMOImpl implements ISavePayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param payFeeAuditPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(PayFeeAuditPo payFeeAuditPo) {

        payFeeAuditPo.setAuditId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditId));
        int flag = payFeeAuditInnerServiceSMOImpl.savePayFeeAudit(payFeeAuditPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
