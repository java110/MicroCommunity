package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.transactionLog.IDeleteTransactionLogBMO;
import com.java110.common.bmo.transactionLog.IGetTransactionLogBMO;
import com.java110.common.bmo.transactionLog.ISaveTransactionLogBMO;
import com.java110.common.bmo.transactionLog.IUpdateTransactionLogBMO;
import com.java110.common.bmo.transactionLogMessage.IGetTransactionLogMessageBMO;
import com.java110.dto.transactionLog.TransactionLogDto;
import com.java110.dto.transactionLog.TransactionLogMessageDto;
import com.java110.po.transactionLog.TransactionLogPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transactionLog")
public class TransactionLogApi {

    @Autowired
    private ISaveTransactionLogBMO saveTransactionLogBMOImpl;
    @Autowired
    private IUpdateTransactionLogBMO updateTransactionLogBMOImpl;
    @Autowired
    private IDeleteTransactionLogBMO deleteTransactionLogBMOImpl;

    @Autowired
    private IGetTransactionLogBMO getTransactionLogBMOImpl;

    @Autowired
    private IGetTransactionLogMessageBMO getTransactionLogMessageBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /transactionLog/saveTransactionLog
     * @path /app/transactionLog/saveTransactionLog
     */
    @RequestMapping(value = "/saveTransactionLog", method = RequestMethod.POST)
    public ResponseEntity<String> saveTransactionLog(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "serviceCode", "请求报文中未包含serviceCode");
        TransactionLogPo transactionLogPo = BeanConvertUtil.covertBean(reqJson, TransactionLogPo.class);
        return saveTransactionLogBMOImpl.save(transactionLogPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /transactionLog/updateTransactionLog
     * @path /app/transactionLog/updateTransactionLog
     */
    @RequestMapping(value = "/updateTransactionLog", method = RequestMethod.POST)
    public ResponseEntity<String> updateTransactionLog(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "serviceCode", "请求报文中未包含serviceCode");
        Assert.hasKeyAndValue(reqJson, "logId", "logId不能为空");


        TransactionLogPo transactionLogPo = BeanConvertUtil.covertBean(reqJson, TransactionLogPo.class);
        return updateTransactionLogBMOImpl.update(transactionLogPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /transactionLog/deleteTransactionLog
     * @path /app/transactionLog/deleteTransactionLog
     */
    @RequestMapping(value = "/deleteTransactionLog", method = RequestMethod.POST)
    public ResponseEntity<String> deleteTransactionLog(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "logId", "logId不能为空");


        TransactionLogPo transactionLogPo = BeanConvertUtil.covertBean(reqJson, TransactionLogPo.class);
        return deleteTransactionLogBMOImpl.delete(transactionLogPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param serviceCode 服务编码
     * @return
     * @serviceCode /transactionLog/queryTransactionLog
     * @path /app/transactionLog/queryTransactionLog
     */
    @RequestMapping(value = "/queryTransactionLog", method = RequestMethod.GET)
    public ResponseEntity<String> queryTransactionLog(@RequestParam(value = "serviceCode", required = false) String serviceCode,
                                                      @RequestParam(value = "appId", required = false) String appId,
                                                      @RequestParam(value = "transactionId", required = false) String transactionId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        TransactionLogDto transactionLogDto = new TransactionLogDto();
        transactionLogDto.setPage(page);
        transactionLogDto.setRow(row);
        transactionLogDto.setServiceCode(serviceCode);
        transactionLogDto.setAppId(appId);
        transactionLogDto.setTransactionId(transactionId);
        return getTransactionLogBMOImpl.get(transactionLogDto);
    }

    /**
     * 微信删除消息模板
     *
     * @param logId 日志ID
     * @return
     * @serviceCode /transactionLog/queryTransactionLogMessage
     * @path /app/transactionLog/queryTransactionLogMessage
     */
    @RequestMapping(value = "/queryTransactionLogMessage", method = RequestMethod.GET)
    public ResponseEntity<String> queryTransactionLogMessage(@RequestParam(value = "logId") String logId) {
        TransactionLogMessageDto transactionLogMessageDto = new TransactionLogMessageDto();
        transactionLogMessageDto.setLogId(logId);
        return getTransactionLogMessageBMOImpl.get(transactionLogMessageDto);
    }
}
