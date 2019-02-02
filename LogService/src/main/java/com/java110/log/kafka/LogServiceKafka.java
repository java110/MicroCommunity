package com.java110.log.kafka;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.KafkaConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.InitConfigDataException;
import com.java110.common.exception.InitDataFlowContextException;
import com.java110.common.kafka.KafkaFactory;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.log.smo.ILogServiceSMO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka侦听
 * 接受到的信息报文格式为：
 * {
 *     "transactionId":"交易流水号",
 *     "dataFlowId":"上下文对象",
 *     "ip":"ip",
 *     "port":"端口",
 *     "srcIp":"调用方IP",
 *     "srcPort":"调用方端口",
 *     "appId":"应用ID",
 *     "userId":"用户ID",
 *     "serviceCode":"服务编码",
 *     "serviceName":"服务名称",
 *     "timestamp":"时间储",
 *     "logStatus":"记录状态",
 *     "requestMessage":"请求信息",
 *     "responseMessage":"返回信息"
 * }
 * Created by wuxw on 2018/4/15.
 */
public class LogServiceKafka extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(LogServiceKafka.class);


    @Autowired
    private ILogServiceSMO logServiceSMOImpl;

    @KafkaListener(topics = {"LOG"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("LogServiceKafka receive message: {}", record.value().toString());
        String logMessage = record.value().toString();
        logServiceSMOImpl.saveLogMessage(logMessage);
    }



    public ILogServiceSMO getLogServiceSMOImpl() {
        return logServiceSMOImpl;
    }

    public void setLogServiceSMOImpl(ILogServiceSMO logServiceSMOImpl) {
        this.logServiceSMOImpl = logServiceSMOImpl;
    }
}
