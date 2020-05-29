package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 进出场组件内部之间使用，没有给外围系统提供服务能力
 * 进出场服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICarInoutServiceDao {

    /**
     * 保存 进出场信息
     * @param businessCarInoutInfo 进出场信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCarInoutInfo(Map businessCarInoutInfo) throws DAOException;



    /**
     * 查询进出场信息（business过程）
     * 根据bId 查询进出场信息
     * @param info bId 信息
     * @return 进出场信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessCarInoutInfo(Map info) throws DAOException;




    /**
     * 保存 进出场信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveCarInoutInfoInstance(Map info) throws DAOException;




    /**
     * 查询进出场信息（instance过程）
     * 根据bId 查询进出场信息
     * @param info bId 信息
     * @return 进出场信息
     * @throws DAOException DAO异常
     */
    List<Map> getCarInoutInfo(Map info) throws DAOException;



    /**
     * 修改进出场信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateCarInoutInfoInstance(Map info) throws DAOException;


    /**
     * 查询进出场总数
     *
     * @param info 进出场信息
     * @return 进出场数量
     */
    int queryCarInoutsCount(Map info);

}
