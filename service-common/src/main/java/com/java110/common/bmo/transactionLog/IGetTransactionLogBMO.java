package com.java110.common.bmo.transactionLog;

import com.java110.dto.transactionLog.TransactionLogDto;
import org.springframework.http.ResponseEntity;

public interface IGetTransactionLogBMO {


    /**
     * 查询交互日志
     * add by wuxw
     *
     * @param transactionLogDto
     * @return
     */
    ResponseEntity<String> get(TransactionLogDto transactionLogDto);


}
