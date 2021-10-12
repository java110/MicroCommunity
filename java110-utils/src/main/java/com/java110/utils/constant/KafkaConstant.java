package com.java110.utils.constant;

/**
 * kafka常量配置
 * Created by wuxw on 2018/4/17.
 */
public class KafkaConstant {

    /**
     * 写日志topic 名称
     */
    public final static String TOPIC_LOG_NAME = "LOG";
    /**
     * 耗时日志topic 名称
     */
    public final static String TOPIC_COST_TIME_LOG_NAME = "COST_TIME_LOG";

    /**
     * 通知 中心服务
     */
    public final static String TOPIC_NOTIFY_CENTER_SERVICE_NAME = "NOTIFY_CENTER_SERVICE";


    public final static String TOPIC_API_SEND_WEB = "webSentMessageTopic";
    public final static String TOPIC_API_SEND_PARKING_AREA_WEB = "webSentParkingAreaTopic";
}
