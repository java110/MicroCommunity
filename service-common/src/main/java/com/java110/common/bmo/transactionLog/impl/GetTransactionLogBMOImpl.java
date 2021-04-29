package com.java110.common.bmo.transactionLog.impl;

import com.java110.common.bmo.transactionLog.IGetTransactionLogBMO;
import com.java110.dto.transactionLog.TransactionLogDto;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getTransactionLogBMOImpl")
public class GetTransactionLogBMOImpl implements IGetTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * @param transactionLogDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(TransactionLogDto transactionLogDto) {


        int count = transactionLogInnerServiceSMOImpl.queryTransactionLogsCount(transactionLogDto);

        List<TransactionLogDto> transactionLogDtos = null;
        if (count > 0) {
            transactionLogDtos = transactionLogInnerServiceSMOImpl.queryTransactionLogs(transactionLogDto);
        } else {
            transactionLogDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) transactionLogDto.getRow()), count, transactionLogDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
