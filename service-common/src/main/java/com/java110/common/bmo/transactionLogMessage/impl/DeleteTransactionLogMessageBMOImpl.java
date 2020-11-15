package com.java110.common.bmo.transactionLogMessage.impl;

import com.java110.common.bmo.transactionLogMessage.IDeleteTransactionLogMessageBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ITransactionLogMessageInnerServiceSMO;
import com.java110.po.transactionLog.TransactionLogMessagePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteTransactionLogMessageBMOImpl")
public class DeleteTransactionLogMessageBMOImpl implements IDeleteTransactionLogMessageBMO {

    @Autowired
    private ITransactionLogMessageInnerServiceSMO transactionLogMessageInnerServiceSMOImpl;

    /**
     * @param transactionLogMessagePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(TransactionLogMessagePo transactionLogMessagePo) {

        int flag = transactionLogMessageInnerServiceSMOImpl.deleteTransactionLogMessage(transactionLogMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
