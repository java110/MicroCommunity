package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.transactionLog.TransactionLogMessageDto;
import com.java110.po.transactionLog.TransactionLogMessagePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITransactionLogMessageInnerServiceSMO
 * @Description 交互日志接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/transactionLogMessageApi")
public interface ITransactionLogMessageInnerServiceSMO {


    @RequestMapping(value = "/saveTransactionLogMessage", method = RequestMethod.POST)
    public int saveTransactionLogMessage(@RequestBody TransactionLogMessagePo transactionLogMessagePo);

    @RequestMapping(value = "/updateTransactionLogMessage", method = RequestMethod.POST)
    public int updateTransactionLogMessage(@RequestBody  TransactionLogMessagePo transactionLogMessagePo);

    @RequestMapping(value = "/deleteTransactionLogMessage", method = RequestMethod.POST)
    public int deleteTransactionLogMessage(@RequestBody  TransactionLogMessagePo transactionLogMessagePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param transactionLogMessageDto 数据对象分享
     * @return TransactionLogMessageDto 对象数据
     */
    @RequestMapping(value = "/queryTransactionLogMessages", method = RequestMethod.POST)
    List<TransactionLogMessageDto> queryTransactionLogMessages(@RequestBody TransactionLogMessageDto transactionLogMessageDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param transactionLogMessageDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTransactionLogMessagesCount", method = RequestMethod.POST)
    int queryTransactionLogMessagesCount(@RequestBody TransactionLogMessageDto transactionLogMessageDto);
}
