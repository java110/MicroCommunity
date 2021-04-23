package com.java110.common.bmo.transactionLog;

import com.java110.po.transactionLog.TransactionLogPo;
import org.springframework.http.ResponseEntity;

public interface ISaveTransactionLogBMO {


    /**
     * 添加交互日志
     * add by wuxw
     *
     * @param transactionLogPo
     * @return
     */
    ResponseEntity<String> save(TransactionLogPo transactionLogPo);


}
