package com.java110.api.smo;

import com.java110.po.transactionLog.TransactionLogPo;

/**
 * @ClassName ISaveTransactionLog
 * @Description TODO
 * @Author wuxw
 * @Date 2020/11/16 0:42
 * @Version 1.0
 * add by wuxw 2020/11/16
 **/
public interface ISaveTransactionLogSMO {

    public void saveLog(TransactionLogPo transactionLogPo);
}
