package com.java110.user.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 业主组件内部之间使用，没有给外围系统提供服务能力
 * 业主服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOwnerServiceDao {

    /**
     * 保存 业主信息
     * @param businessOwnerInfo 业主信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOwnerInfo(Map businessOwnerInfo) throws DAOException;



    /**
     * 查询业主信息（business过程）
     * 根据bId 查询业主信息
     * @param info bId 信息
     * @return 业主信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOwnerInfo(Map info) throws DAOException;




    /**
     * 保存 业主信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOwnerInfoInstance(Map info) throws DAOException;




    /**
     * 查询业主信息（instance过程）
     * 根据bId 查询业主信息
     * @param info bId 信息
     * @return 业主信息
     * @throws DAOException DAO异常
     */
    List<Map> getOwnerInfo(Map info) throws DAOException;

    /**
     * 查询业主信息（instance过程）
     * 根据bId 查询业主信息
     * @param info bId 信息
     * @return 业主信息
     * @throws DAOException DAO异常
     */
    int getOwnerInfoCount(Map info) throws DAOException;



    /**
     * 修改业主信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOwnerInfoInstance(Map info) throws DAOException;


    /**
     * 查询业主总数
     *
     * @param info 业主信息
     * @return 业主数量
     */
    int queryOwnersCount(Map info);


    /**
     * 查询业主总数
     *
     * @param info 业主信息
     * @return 业主数量
     */
    int queryOwnersCountByCondition(Map info);

    /**
     * 查询业主信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
     List<Map> getOwnerInfoByCondition(Map info) throws DAOException;

    /**
     * 查询没有入驻的 业主数量
     * @param info 信息
     * @return 未入驻业主数量
     */
     int queryNoEnterRoomOwnerCount(Map info);

    /**
     * 根据房屋查询业主信息
     * @param info 房屋信息
     * @return 业主信息
     * @throws DAOException 异常信息
     */
     List<Map> queryOwnersByRoom(Map info) throws DAOException;

    /**
     * 根据停车位查询业主信息
     * @param info 查询条件
     * @return 业主信息
     * @throws DAOException 异常信息
     */
    List<Map> queryOwnersByParkingSpace(Map info) throws DAOException;

    int queryOwnerLogsCountByRoom(Map info);

    List<Map> queryOwnerLogsByRoom(Map info);
}
