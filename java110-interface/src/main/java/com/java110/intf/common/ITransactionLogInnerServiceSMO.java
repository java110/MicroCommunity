package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.transactionLog.TransactionLogDto;
import com.java110.po.transactionLog.TransactionLogPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITransactionLogInnerServiceSMO
 * @Description 交互日志接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/transactionLogApi")
public interface ITransactionLogInnerServiceSMO {


    @RequestMapping(value = "/saveTransactionLog", method = RequestMethod.POST)
    public int saveTransactionLog(@RequestBody TransactionLogPo transactionLogPo);

    @RequestMapping(value = "/updateTransactionLog", method = RequestMethod.POST)
    public int updateTransactionLog(@RequestBody  TransactionLogPo transactionLogPo);

    @RequestMapping(value = "/deleteTransactionLog", method = RequestMethod.POST)
    public int deleteTransactionLog(@RequestBody  TransactionLogPo transactionLogPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param transactionLogDto 数据对象分享
     * @return TransactionLogDto 对象数据
     */
    @RequestMapping(value = "/queryTransactionLogs", method = RequestMethod.POST)
    List<TransactionLogDto> queryTransactionLogs(@RequestBody TransactionLogDto transactionLogDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param transactionLogDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryTransactionLogsCount", method = RequestMethod.POST)
    int queryTransactionLogsCount(@RequestBody TransactionLogDto transactionLogDto);
}
