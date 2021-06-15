package com.java110.user.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 车辆管理组件内部之间使用，没有给外围系统提供服务能力
 * 车辆管理服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IOwnerCarServiceDao {

    /**
     * 保存 车辆管理信息
     *
     * @param businessOwnerCarInfo 车辆管理信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOwnerCarInfo(Map businessOwnerCarInfo) throws DAOException;


    /**
     * 查询车辆管理信息（business过程）
     * 根据bId 查询车辆管理信息
     *
     * @param info bId 信息
     * @return 车辆管理信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOwnerCarInfo(Map info) throws DAOException;


    /**
     * 保存 车辆管理信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOwnerCarInfoInstance(Map info) throws DAOException;


    /**
     * 查询车辆管理信息（instance过程）
     * 根据bId 查询车辆管理信息
     *
     * @param info bId 信息
     * @return 车辆管理信息
     * @throws DAOException DAO异常
     */
    List<Map> getOwnerCarInfo(Map info) throws DAOException;


    /**
     * 修改车辆管理信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOwnerCarInfoInstance(Map info) throws DAOException;


    /**
     * 查询车辆管理总数
     *
     * @param info 车辆管理信息
     * @return 车辆管理数量
     */
    int queryOwnerCarsCount(Map info);

    long queryOwnerParkingSpaceCount(Map beanCovertMap);
}
