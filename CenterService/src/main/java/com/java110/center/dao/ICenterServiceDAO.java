package com.java110.center.dao;

import com.java110.common.exception.DAOException;
import com.java110.entity.mapping.Mapping;

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
     * @param businesses 订单项信息
     * @return
     */
    public void saveBusiness(List<Map> businesses) throws DAOException;

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

    /**
     * 根据bId 修改业务项信息
     * @param business
     * @throws DAOException
     */
    public void updateBusinessByBId(Map business) throws DAOException;

    /**
     * 当所有业务动作是否都是C，将订单信息改为 C
     * @param bId
     * @return
     * @throws DAOException
     */
    public void completeOrderByBId(String bId) throws DAOException;

    /**
     * 根据bId查询订单信息
     * @param bId
     * @return
     * @throws DAOException
     */
    public Map getOrderInfoByBId(String bId)throws DAOException;

    /**
     * 获取同个订单中已经完成的订单项
     * @param bId
     * @return
     * @throws DAOException
     */
    public List<Map> getCommonOrderCompledBusinessByBId(String bId) throws DAOException;

    /**
     * 查询所有组件
     * @return
     */
    public List<Map> getAppRouteAndServiceInfoAll();

    /**
     * 查询映射表
     * @return
     */
    public List<Mapping> getMappingInfoAll();

}
