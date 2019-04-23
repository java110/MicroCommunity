package com.java110.log.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.code.ICodeApi;
import com.java110.log.dao.LogServiceDao;
import com.java110.log.smo.ILogServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志 消息处理类
 * Created by wuxw on 2017/4/11.
 */
@Service("logServiceSMOImpl")
@Transactional
public class LogServiceSMOImpl extends BaseServiceSMO implements ILogServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(LogServiceSMOImpl.class);

    @Autowired
    private ICodeApi codeApi;

    @Autowired
    private LogServiceDao logServiceDaoImpl;

    /**
     * 保存日志信息
     *
     * @param logMessage 需要保存的日志信息
     */
    @Override
    public void saveLogMessage(String logMessage) {
        logger.debug("received log message : {}", logMessage);

        try {
            doSaveLogMessage(logMessage);
        } catch (Exception e) {
            logger.error("save log message error :", e);
        }
    }


    /**
     * @param logMessage 需要保存的日志信息
     * @throws Exception 异常信息
     */
    private void doSaveLogMessage(String logMessage) throws Exception {

        JSONObject logMessageObj = JSONObject.parseObject(logMessage);
        //校验
        validateLogMessage(logMessageObj);

        // 调用服务生成log_id
        String logId = GenerateCodeFactory.getInnerTransactionId();
        //String log_id = codeApi.generateCode("1000001");

        // 封装日志保存参数
        Map logMessageParams = new HashMap();
        logMessageParams.put("logId", logId);
        builderLogMessageParams(logMessageParams, logMessageObj);

        //保存日志
        logServiceDaoImpl.saveTransactionLog(logMessageParams);
        //保存日志（交互报文）
        logServiceDaoImpl.saveTransactionLogMessage(logMessageParams);

    }

    /**
     * 封装日志保存参数
     *
     * @param logMessageParams 日志保存参数
     * @param logMessageObj 日志消息对象
     * @throws Exception 异常信息
     */
    private void builderLogMessageParams(Map logMessageParams, JSONObject logMessageObj) throws Exception {

        logMessageParams.putAll(logMessageObj);
        //#{requestHeader},#{responseHeader},#{requestMessage},#{responseMessage},#{remark}
        String requestMessage = logMessageObj.getString("requestMessage");
        String responseMessage = logMessageObj.getString("responseMessage");

        JSONObject requestMessageObj = JSONObject.parseObject(requestMessage);
        JSONObject responseMessageObj = JSONObject.parseObject(responseMessage);

        logMessageParams.put("requestHeader", requestMessageObj.getString("headers"));
        logMessageParams.put("requestMessage", requestMessageObj.getString("body"));
        logMessageParams.put("responseHeader", responseMessageObj.getString("headers"));
        logMessageParams.put("responseMessage", responseMessageObj.getString("body"));

        logMessageParams.put("timestamp", DateUtil.getDefaultDateFromString(logMessageObj.getString("timestamp")));

        logMessageParams.put("month", DateUtil.getCurrentMonth());
    }

    /**
     * 校验 日志报文 必填节点是否存在
     *
     * @param logMessageObj JSON 化的日志信息
     */
    private void validateLogMessage(JSONObject logMessageObj) {

        Assert.hasKeyAndValue(logMessageObj, "transactionId", "can not find transactionId node or transactionId is null");

        Assert.hasKeyAndValue(logMessageObj, "dataFlowId", "can not find dataFlowId node or dataFlowId is null");

        Assert.hasKeyAndValue(logMessageObj, "ip", "can not find ip node or ip is null");

        Assert.hasKeyAndValue(logMessageObj, "port", "can not find port node or port is null");

        Assert.hasKeyAndValue(logMessageObj, "appId", "can not find appId node or appId is null");

        Assert.hasKeyAndValue(logMessageObj, "timestamp", "can not find timestamp node or timestamp is null");

        Assert.hasKeyAndValue(logMessageObj, "logStatus", "can not find logStatus node or logStatus is null");

        Assert.hasKey(logMessageObj, "requestMessage", "can not find requestMessage node ");

        Assert.hasKey(logMessageObj, "responseMessage", "can not find responseMessage node ");
    }

    public ICodeApi getCodeApi() {
        return codeApi;
    }

    public void setCodeApi(ICodeApi codeApi) {
        this.codeApi = codeApi;
    }

    public LogServiceDao getLogServiceDaoImpl() {
        return logServiceDaoImpl;
    }

    public void setLogServiceDaoImpl(LogServiceDao logServiceDaoImpl) {
        this.logServiceDaoImpl = logServiceDaoImpl;
    }
}
