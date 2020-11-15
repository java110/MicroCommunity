package com.java110.api.smo.impl;

import com.java110.api.smo.ISaveTransactionLogSMO;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.po.transactionLog.TransactionLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName SaveTransactionLogSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/11/16 0:43
 * @Version 1.0
 * add by wuxw 2020/11/16
 **/
@Service
public class SaveTransactionLogSMOImpl implements ISaveTransactionLogSMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    @Override
    @Async
    public void saveLog(TransactionLogPo transactionLogPo) {
        transactionLogInnerServiceSMOImpl.saveTransactionLog(transactionLogPo);
    }
}
