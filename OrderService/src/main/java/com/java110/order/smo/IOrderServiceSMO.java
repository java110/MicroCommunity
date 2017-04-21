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

    /**
     * 作废订单
     * 根据业务动作作废
     * 请求协议：
     * {
     *     "orderList":{
     *     "transactionId": "1000000200201704113137002690",
     "channelId": "700212896",
     "remarks": "",
     "custId": "701008023904",
     "statusCd": "S",
     "reqTime": "20170411163709",
     "extSystemId": "310013698777",
     "olTypeCd": "15",
     *        "oldOlId":"123456789",
     *        "asyn":"S"
     *     },
     *     "busiOrder":[{
     *         "actionTypeCd":"ALL"
     *     }]
     * }
     *
     * ，
     *
     * 根据 订单项ID作废
     *
     * {
     *     "orderList":{
     *         "transactionId": "1000000200201704113137002690",
     "channelId": "700212896",
     "remarks": "",
     "custId": "701008023904",
     "statusCd": "S",
     "reqTime": "20170411163709",
     "extSystemId": "310013698777",
     "olTypeCd": "15",
     "asyn":"A"
     *     },
     *     "busiOrder":[{
     *          "oldBoId":"123456789"
     *     },
     *     {
     *          "oldBoId":"123456799"
     *     }]
     * }
     * @param orderInfo
     * @return
     * @throws Exception
     */
    public String deleteOrder(JSONObject orderInfo) throws Exception;


}
