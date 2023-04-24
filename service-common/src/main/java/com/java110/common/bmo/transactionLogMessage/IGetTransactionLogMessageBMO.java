package com.java110.common.bmo.transactionLogMessage;

import com.java110.dto.transactionLog.TransactionLogMessageDto;
import org.springframework.http.ResponseEntity;

public interface IGetTransactionLogMessageBMO {


    /**
     * 查询交互日志
     * add by wuxw
     *
     * @param transactionLogMessageDto
     * @return
     */
    ResponseEntity<String> get(TransactionLogMessageDto transactionLogMessageDto);


}
