package com.java110.common.bmo.transactionLogMessage;

import com.java110.po.transactionLog.TransactionLogMessagePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteTransactionLogMessageBMO {


    /**
     * 修改交互日志
     * add by wuxw
     *
     * @param transactionLogMessagePo
     * @return
     */
    ResponseEntity<String> delete(TransactionLogMessagePo transactionLogMessagePo);


}
