package com.java110.community.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 小区组件内部之间使用，没有给外围系统提供服务能力
 * 小区服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IInitializeBuildingUnitDao {

    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int deleteBuildingUnit(Map info) throws DAOException;

    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int deletefFloor(Map info) throws DAOException;
    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int deleteBuildingRoom(Map info) throws DAOException;


    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int deleteParkingArea(Map info) throws DAOException;
    /**
     * 修改服务信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    int deleteParkingSpace(Map info) throws DAOException;

}