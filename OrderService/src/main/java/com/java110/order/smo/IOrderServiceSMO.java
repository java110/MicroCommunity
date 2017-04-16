package com.java110.order.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.order.OrderList;

/**
 * 订单服务业务处理接口
 *
 * 订单受理
 * Created by wuxw on 2017/4/11.
 */
public interface IOrderServiceSMO {


    /**
     * 根据购物车ID 或者 外部系统ID 或者 custId 或者 channelId 查询订单信息
     *
     * @param orderList
     * @return
     */
    public String queryOrderInfo(OrderList orderList)  throws Exception;

    /**
     * 订单调度，
     * 根据订单类型 调用不同服务 处理
     * @param orderInfo
     * @return
     * @throws Exception
     */
    public String orderDispatch(JSONObject orderInfo) throws Exception;


}
