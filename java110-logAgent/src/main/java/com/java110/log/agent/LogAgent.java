package com.java110.log.agent;

import com.java110.common.cache.MappingCache;
import com.java110.common.constant.KafkaConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.factory.ApplicationContextFactory;
import com.java110.common.kafka.KafkaFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.core.context.DataFlow;
import com.java110.core.context.TransactionLog;

/**
 * 日志代理
 * 收集日志 发送至 日志服务
 * Created by wuxw on 2018/6/9.
 */
public class LogAgent extends LoggerEngine{

    public static final String LOG_STATUS_S = "S";
    public static final String LOG_STATUS_F = "F";
    public static final String LOG_TYPE_S = "S";
    public static final String LOG_TYPE_C = "C";


    /**
     * 发送日志
     * @param transactionLog
     * @return
     */
    public static boolean sendLog(TransactionLog transactionLog){
        if(MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_LOG_ON_OFF))) {
            try {
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_LOG_NAME, "", transactionLog.toString());
            }catch (Exception e){
                logger.error("保存日志失败："+transactionLog.toString(),e);
                return false;
            }
        }
        return true;
    }


    public static boolean sendLog(DataFlow dataFlow){
        return sendLog(dataFlow);

    }


}
