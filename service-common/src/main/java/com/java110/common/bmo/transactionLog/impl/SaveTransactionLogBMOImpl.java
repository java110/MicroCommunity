package com.java110.common.bmo.transactionLog.impl;

import com.java110.common.bmo.transactionLog.ISaveTransactionLogBMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.po.transactionLog.TransactionLogPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveTransactionLogBMOImpl")
public class SaveTransactionLogBMOImpl implements ISaveTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param transactionLogPo
     * @return 订单服务能够接受的报文
     */
    //@Java110Transactional
    public ResponseEntity<String> save(TransactionLogPo transactionLogPo) {

        transactionLogPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        int flag = transactionLogInnerServiceSMOImpl.saveTransactionLog(transactionLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
