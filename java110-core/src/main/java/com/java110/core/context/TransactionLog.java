package com.java110.core.context;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 交互日志
 *
 * id 业务ID，每次接口交互时的唯一编码 如订单ID 业务ID等
 * logContextId 上下文ID ，每次调用 每个系统只有一个contextId 每次不一样 生成规则为uuId
 * ip 当前主机IP
 * port 当前应用监听端口
 * url 交互地址 调用端和服务提供端 必须一致
 * serviceCode 服务编码
 * serviceName 服务名称
 * type S 调用端 C服务提供端
 * timestamp 时间戳
 * status 状态 S表示成功 F表示失败
 * messageHeaders 消息头部信息 目前只有调用端保存日志
 * message 消息 目前只有调用端保存
 * Created by wuxw on 2018/6/7.
 */
public interface TransactionLog extends Serializable {

    //业务ID
    public String getTransactionId();

    /**
     * 日志上下文ID
     * @return
     */
    public String getDataFlowId();

    //获取主机Ip
    public String getHostIp();

    //获取监听端口
    public String getPort();

    public String getAppId();

    public String getUserId();

    public void setAppId(String appId);

    public void setUserId(String userId);

    //业务编码,如果是批量受理就取第一个
    public String getServiceCode();

    //业务名称 如果是批量受理就取第一个
    public String getServiceName();

    //时间
    public String getTimestamp();

    //编码 S 表示成功 F其他表示失败

    /**
     * 当前状态 调用的时候和接受的时候统一写成S
     * 返回时如果返回失败则写为F
     * @return S 表示成功 F其他表示失败
     */
    public String getLogStatus();

    /**
     * 耗时
     * @return
     */
    public long getCostTime();

    /**
     * 接口请求消息
     * @return
     */
    public String getRequestMessage();


    public String getResponseMessage();

    /**
     * 重新构建 TransactionLog 对象 主要用于服务调用方
     * @return
     */
    public TransactionLog reBuilder(String appId,String userId,String requestMessage,String responseMessage,String logStatus,long costTime);

    /**
     * 重新构建 TransactionLog 对象 主要用于服务提供方
     * @param requestMessage 请求数据
     * @param responseMessage 返回数据
     * @param logStatus 数据交互状态
     * @return
     */
    public TransactionLog reBuilder(String requestMessage,String responseMessage,String logStatus,long costTime);

    /**
     * 转换成json模式
     * @return
     */
    public String toString();

}
