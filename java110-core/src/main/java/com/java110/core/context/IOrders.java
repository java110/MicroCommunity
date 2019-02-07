package com.java110.core.context;

import com.alibaba.fastjson.JSONArray;

/**
 * 获取订单信息
 * Created by wuxw on 2018/5/18.
 */
public interface IOrders {


    public String getTransactionId();

    /**
     * 获取 数据流ID
     * @return
     */
    public String getDataFlowId();

    public String getOrderTypeCd();

    public String getRequestTime();

    public String getAppId();

    public String getUserId();

    public String getRemark();

    public String getReqSign();

    public JSONArray getAttrs();

    public String getBusinessType();

}



