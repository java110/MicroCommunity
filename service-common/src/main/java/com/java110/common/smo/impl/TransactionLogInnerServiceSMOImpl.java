package com.java110.common.smo.impl;


import com.java110.common.dao.ITransactionLogServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.transactionLog.TransactionLogDto;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.intf.common.ITransactionLogMessageInnerServiceSMO;
import com.java110.po.transactionLog.TransactionLogMessagePo;
import com.java110.po.transactionLog.TransactionLogPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 交互日志内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class TransactionLogInnerServiceSMOImpl extends BaseServiceSMO implements ITransactionLogInnerServiceSMO {

    @Autowired
    private ITransactionLogServiceDao transactionLogServiceDaoImpl;

    @Autowired
    private ITransactionLogMessageInnerServiceSMO transactionLogMessageInnerServiceSMOImpl;


    @Override
    public int saveTransactionLog(@RequestBody TransactionLogPo transactionLogPo) {
        int saveFlag = 1;

        if (StringUtil.isEmpty(transactionLogPo.getLogId()) || transactionLogPo.getLogId().startsWith("-")) {
            transactionLogPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        }
        transactionLogServiceDaoImpl.saveTransactionLogInfo(BeanConvertUtil.beanCovertMap(transactionLogPo));
        TransactionLogMessagePo transactionLogMessagePo = BeanConvertUtil.covertBean(transactionLogPo, TransactionLogMessagePo.class);
        transactionLogMessageInnerServiceSMOImpl.saveTransactionLogMessage(transactionLogMessagePo);
        return saveFlag;
    }

    @Override
    public int updateTransactionLog(@RequestBody TransactionLogPo transactionLogPo) {
        int saveFlag = 1;
        transactionLogServiceDaoImpl.updateTransactionLogInfo(BeanConvertUtil.beanCovertMap(transactionLogPo));
        return saveFlag;
    }

    @Override
    public int deleteTransactionLog(@RequestBody TransactionLogPo transactionLogPo) {
        int saveFlag = 1;
        transactionLogServiceDaoImpl.updateTransactionLogInfo(BeanConvertUtil.beanCovertMap(transactionLogPo));
        return saveFlag;
    }

    @Override
    public List<TransactionLogDto> queryTransactionLogs(@RequestBody TransactionLogDto transactionLogDto) {

        //校验是否传了 分页信息

        int page = transactionLogDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            transactionLogDto.setPage((page - 1) * transactionLogDto.getRow());
        }

        List<TransactionLogDto> transactionLogs = BeanConvertUtil.covertBeanList(transactionLogServiceDaoImpl.getTransactionLogInfo(BeanConvertUtil.beanCovertMap(transactionLogDto)), TransactionLogDto.class);

        return transactionLogs;
    }


    @Override
    public int queryTransactionLogsCount(@RequestBody TransactionLogDto transactionLogDto) {
        return transactionLogServiceDaoImpl.queryTransactionLogsCount(BeanConvertUtil.beanCovertMap(transactionLogDto));
    }

    public ITransactionLogServiceDao getTransactionLogServiceDaoImpl() {
        return transactionLogServiceDaoImpl;
    }

    public void setTransactionLogServiceDaoImpl(ITransactionLogServiceDao transactionLogServiceDaoImpl) {
        this.transactionLogServiceDaoImpl = transactionLogServiceDaoImpl;
    }
}
