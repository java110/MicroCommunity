package com.java110.core.context;

/**
 * 订单返回结果信息
 */
public interface IOrderResponse {


    /**
     * 获取返回编码
     * @return
     */
    public String getCode() ;


    /**
     * 获取返回信息
     * @return
     */
    public String getMessage() ;
}
