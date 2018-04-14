package com.java110.center.dao;

import com.java110.common.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2018/4/14.
 */
public interface ICenterServiceDAO {

    /**
     * 保存订单信息
     * @param order 订单信息
     * @return
     */
    public void saveOrder(Map order) throws DAOException;

    /**
     * 保存属性信息
     * @param orderAttrs
     * @return
     */
    public void saveOrderAttrs(List<Map> orderAttrs) throws DAOException;


    /**
     * 保存订单项信息
     * @param business 订单项信息
     * @return
     */
    public void saveBusiness(Map business) throws DAOException;

    /**
     * 保存属性信息
     * @param businessAttrs
     * @return
     */
    public void saveBusinessAttrs(List<Map> businessAttrs) throws DAOException;

    /**
     * 更新订单信息（一般就更新订单状态）
     * @param order
     * @throws DAOException
     */
    public void updateOrder(Map order) throws DAOException;

    /**
     * 更新订单项信息（一般就更新订单状态）
     * @param order
     * @throws DAOException
     */
    public void updateBusiness(Map order) throws DAOException;
}
