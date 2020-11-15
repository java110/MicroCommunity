package com.java110.common.bmo.transactionLogMessage;

import com.java110.po.transactionLog.TransactionLogMessagePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateTransactionLogMessageBMO {


    /**
     * 修改交互日志
     * add by wuxw
     *
     * @param transactionLogMessagePo
     * @return
     */
    ResponseEntity<String> update(TransactionLogMessagePo transactionLogMessagePo);


}
