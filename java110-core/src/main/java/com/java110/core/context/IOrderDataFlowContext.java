package com.java110.core.context;

import com.java110.entity.order.Business;
import com.java110.entity.order.Orders;

import java.util.List;

/**
 * 订单类 数据层封装接口
 */
public interface IOrderDataFlowContext extends IDataFlowContextPlus {

    /**
     * 获取订单信息
     * @return
     */
    public Orders getOrders();

    /**
     * 获取 业务信息
     * @return
     */
    public List<Business> getBusinessList();

}
