package com.java110.order.smo;

import com.alibaba.fastjson.JSONObject;

/**
 * 订单服务业务处理接口
 *
 * 订单受理
 * Created by wuxw on 2017/4/11.
 */
public interface IOrderServiceSMO {

    /**
     * 订单调度，
     * 根据订单类型 调用不同服务 处理
     * @param orderInfo
     * @return
     * @throws Exception
     */
    public String orderDispatch(JSONObject orderInfo) throws Exception;


}
