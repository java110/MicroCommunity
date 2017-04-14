package com.java110.order.dao;

import com.java110.entity.order.BusiOrder;
import com.java110.entity.order.BusiOrderAttr;
import com.java110.entity.order.OrderList;
import com.java110.entity.order.OrderListAttr;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.entity.user.CustAttr;

import java.util.List;

/**
 * 购物车，订单项信息管理
 *
 * 增加，查询
 * 没有查询，理论上购物车信息，不允许删除和修改
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOrderServiceDao {

    /**
     * 保存购物车信息(过程表)
     * @param orderList 购物车信息
     * @return
     */
    public int saveDataToBoOrderList(OrderList orderList) throws RuntimeException;

    /**
     * 保存购物车属性(过程表)
     * @param orderListAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    public int saveDataToOrderListAttr(OrderListAttr orderListAttr) throws RuntimeException ;

    /**
     *  保存订单项(过程表)
     * @param busiOrder 用户信息
     * @return
     * @throws RuntimeException
     */
    public int saveDataToBusiOrder(BusiOrder busiOrder) throws RuntimeException;

    /**
     * 保存订单项属性
     * @param busiOrderAttr
     * @return
     * @throws RuntimeException
     */

    public int saveDataToBusiOrderAttr(BusiOrderAttr busiOrderAttr) throws RuntimeException;




}