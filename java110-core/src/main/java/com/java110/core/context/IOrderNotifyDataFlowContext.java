package com.java110.core.context;

import java.util.Date;

/**
 * 订单通知上下文
 */
public interface IOrderNotifyDataFlowContext extends IDataFlowContextPlus{


    /**
     * 交易流水号
     * @return
     */
    public String getTransactionId();

    /**
     * 返回时间
     * @return
     */
    public Date getResponseTime();

    /**
     * 订单类型
     * @return
     */
    public String getOrderTypeCd();

    /**
     * 请求类型 B business过程 I instance过程 N notify过程
     * @return
     */
    public String getBusinessType();

    /**
     * 业务ID
     * @return
     */
    public String getbId();


    /**
     * 业务类型
     * @return
     */
    public String getBusinessTypeCd();


    public String getoId() ;

    public void setoId(String oId) ;

}
