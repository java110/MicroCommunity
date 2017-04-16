package com.java110.order.dao.impl;

import com.java110.common.log.LoggerEngine;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.order.BusiOrder;
import com.java110.entity.order.BusiOrderAttr;
import com.java110.entity.order.OrderList;
import com.java110.entity.order.OrderListAttr;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.entity.user.CustAttr;
import com.java110.order.dao.IOrderServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

/**
 * 用户信息实现工程
 * Created by wuxw on 2016/12/27.
 */
@Service("orderServiceDaoImpl")
@Transactional
public class OrderServiceDaoImpl extends BaseServiceDao implements IOrderServiceDao {


    /**
     * 保存购物车信息
     * @param orderList 购物车信息
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToBoOrderList(OrderList orderList) throws RuntimeException {
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToBoOrderList】保存数据入参 : " + orderList);
        int saveFlag = 0;
            saveFlag = sqlSessionTemplate.insert("orderServiceDaoImpl.saveDataToBoOrderList",orderList);
            LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToBoOrderList】保存数据出参 : saveFlag:" + saveFlag);
            return saveFlag;
    }

    /**
     * 购物车信息属性保存
     * @param orderListAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToOrderListAttr(OrderListAttr orderListAttr) throws RuntimeException {
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToOrderListAttr】保存数据入参 : " + orderListAttr);
        int saveFlag = 0;
        saveFlag = sqlSessionTemplate.insert("orderServiceDaoImpl.saveDataToOrderListAttr",orderListAttr);
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToOrderListAttr】保存数据出参 : saveFlag:" + saveFlag);
        return saveFlag;
    }

    @Override
    public int saveDataToBusiOrder(BusiOrder busiOrder) throws RuntimeException {
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToBusiOrder】保存数据入参 : " + busiOrder);
        int saveFlag = 0;
        saveFlag = sqlSessionTemplate.insert("orderServiceDaoImpl.saveDataToBusiOrder",busiOrder);
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToBusiOrder】保存数据出参 : saveFlag:" + saveFlag);
        return saveFlag;
    }

    @Override
    public int saveDataToBusiOrderAttr(BusiOrderAttr busiOrderAttr) throws RuntimeException {
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToBusiOrderAttr】保存数据入参 : " + busiOrderAttr);
        int saveFlag = 0;
        saveFlag = sqlSessionTemplate.insert("orderServiceDaoImpl.saveDataToBusiOrderAttr",busiOrderAttr);
        LoggerEngine.debug("----【OrderServiceDaoImpl.saveDataToBusiOrderAttr】保存数据出参 : saveFlag:" + saveFlag);
        return saveFlag;
    }

    /**
     * 查询订单信息(order_list order_list_attr)
     * @param orderList
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<OrderList> queryOrderListAndAttr(OrderList orderList) throws RuntimeException {

        LoggerEngine.debug("----【OrderServiceDaoImpl.queryOrderListAndAttr】保存数据入参 : " + orderList);
        return sqlSessionTemplate.selectList("orderServiceDaoImpl.queryOrderListAndAttr",orderList);
    }

    /**
     * 查询订单信息(busi_order busi_order_attr)
     * @param busiOrder
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<BusiOrder> queryBusiOrderAndAttr(BusiOrder busiOrder) throws RuntimeException {
        LoggerEngine.debug("----【OrderServiceDaoImpl.queryBusiOrderAndAttr】保存数据入参 : " + busiOrder);
        return sqlSessionTemplate.selectList("orderServiceDaoImpl.queryBusiOrderAndAttr",busiOrder);
    }
}
