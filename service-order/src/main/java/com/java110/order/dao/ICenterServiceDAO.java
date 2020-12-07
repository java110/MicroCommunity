package com.java110.order.dao;

import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.utils.exception.DAOException;
import com.java110.entity.mapping.Mapping;

import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2018/4/14.
 */
public interface ICenterServiceDAO {


    /**
     * 查询订单
     * @param order
     * @return
     * @throws DAOException
     */
    public Map getOrder(Map order) throws DAOException;

    /**
     * 查询订单项
     * @param orderItem
     * @return
     * @throws DAOException
     */
    public List<Map> getOrderItems(Map orderItem) throws DAOException;

    /**
     * 保存订单信息
     *
     * @param orderItem 订单信息
     * @return
     */
    public void saveOrderItem(Map orderItem) throws DAOException;

    /**
     * 完成订单信息
     *
     * @param orderItem 订单信息
     * @return
     */
    public void updateOrderItem(Map orderItem) throws DAOException;

    /**
     * 完成订单信息
     *
     * @param orderItem 订单信息
     * @return
     */
    public void deleteUnItemLog(Map orderItem) throws DAOException;


    /**
     * 保存订单信息
     *
     * @param order 订单信息
     * @return
     */
    public void saveOrder(Map order) throws DAOException;

    /**
     * 保存属性信息
     *
     * @param orderAttrs
     * @return
     */
    public void saveOrderAttrs(List<Map> orderAttrs) throws DAOException;


    /**
     * 保存订单项信息
     *
     * @param businesses 订单项信息
     * @return
     */
    public void saveBusiness(List<Map> businesses) throws DAOException;

    /**
     * 保存订单项信息
     *
     * @param business 订单项信息
     */
    public void saveBusiness(Map business) throws DAOException;

    /**
     * 保存属性信息
     *
     * @param businessAttrs
     * @return
     */
    public void saveBusinessAttrs(List<Map> businessAttrs) throws DAOException;

    /**
     * 更新订单信息（一般就更新订单状态）
     *
     * @param order
     * @throws DAOException
     */
    public void updateOrder(Map order) throws DAOException;

    /**
     * 更新订单项信息（一般就更新订单状态）
     *
     * @param order
     * @throws DAOException
     */
    public void updateBusiness(Map order) throws DAOException;

    /**
     * 根据bId 修改业务项信息
     *
     * @param business
     * @throws DAOException
     */
    public void updateBusinessByBId(Map business) throws DAOException;

    /**
     * 当所有业务动作是否都是C，将订单信息改为 C
     *
     * @param bId
     * @return
     * @throws DAOException
     */
    public void completeOrderByBId(String bId) throws DAOException;

    /**
     * 当所有业务动作是否都是C，将订单信息改为 C
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public void completeOrderByOId(String oId) throws DAOException;

    /**
     * 判断 business 过程是否完成 1 表示完成 0表示未完成
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public int judgeAllBusinessCompleted(String oId, String statusCd) throws DAOException;

    /**
     * 判断 business 过程是否是否满足撤单条件
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public int judgeAllBusinessDeleteOrder(String oId, String statusCd) throws DAOException;

    /**
     * 根据bId查询订单信息
     *
     * @param bId
     * @return
     * @throws DAOException
     */
    public Map getOrderInfoByBId(String bId) throws DAOException;

    /**
     * 根据oId查询订单信息
     *
     * @param oId
     * @return
     * @throws DAOException
     */
    public Map getDeleteOrderBusinessByOId(String oId) throws DAOException;

    /**
     * 获取同个订单中已经完成的订单项
     *
     * @param bId
     * @return
     * @throws DAOException
     */
    public List<Map> getCommonOrderCompledBusinessByBId(String bId) throws DAOException;

    /**
     * 根据oId 查询Business
     *
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> getBusinessByOId(Map info) throws DAOException;

    /**
     * 查询所有组件
     *
     * @return
     */
    public List<Map> getAppRouteAndServiceInfoAll();

    /**
     * 查询映射表
     *
     * @return
     */
    public List<Mapping> getMappingInfoAll();

    /**
     * 查询映射表
     *
     * @return
     */
    public List<BasePrivilegeDto> getPrivilegeAll();
    /**
     * 查询映射表
     *
     * @return
     */
    public List<BusinessDatabusDto> getDatabusAll();

    /**
     * 查询业主 添加 修改 删除订单
     *
     * @param info
     * @return
     */
    public List<Map> queryOwenrOrders(Map info);

    /**
     * 根据业务类型查询 订单
     * @param info
     * @return
     */
    public List<Map> queryOrderByBusinessType(Map info);


    /**
     * 根据业务类型查询 订单
     * @param info
     * @return
     */
    public List<Map> queryOrderByBId(Map info);


    /**
     * 查询业主 添加 修改 删除订单
     *
     * @param info
     * @return
     */
    public int updateBusinessStatusCd(Map info);


    public List<Map> queryManchineOrders(Map info);

    public List<Map> queryApplicationKeyOrders(Map info);

    public List<Map> querySameOrderBusiness(Map info);


}
