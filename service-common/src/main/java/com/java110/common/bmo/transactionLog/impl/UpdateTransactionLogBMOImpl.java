package com.java110.common.bmo.transactionLog.impl;

import com.java110.common.bmo.transactionLog.IUpdateTransactionLogBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.po.transactionLog.TransactionLogPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateTransactionLogBMOImpl")
public class UpdateTransactionLogBMOImpl implements IUpdateTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * @param transactionLogPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(TransactionLogPo transactionLogPo) {

        int flag = transactionLogInnerServiceSMOImpl.updateTransactionLog(transactionLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
