package com.java110.user.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 业主房屋组件内部之间使用，没有给外围系统提供服务能力
 * 业主房屋服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IOwnerRoomRelServiceDao {

    /**
     * 保存 业主房屋信息
     *
     * @param businessOwnerRoomRelInfo 业主房屋信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOwnerRoomRelInfo(Map businessOwnerRoomRelInfo) throws DAOException;


    /**
     * 查询业主房屋信息（business过程）
     * 根据bId 查询业主房屋信息
     *
     * @param info bId 信息
     * @return 业主房屋信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOwnerRoomRelInfo(Map info) throws DAOException;


    /**
     * 保存 业主房屋信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOwnerRoomRelInfoInstance(Map info) throws DAOException;


    /**
     * 查询业主房屋信息（instance过程）
     * 根据bId 查询业主房屋信息
     *
     * @param info bId 信息
     * @return 业主房屋信息
     * @throws DAOException DAO异常
     */
    List<Map<Object, Object>> getOwnerRoomRelInfo(Map<Object, Object> info) throws DAOException;


    /**
     * 修改业主房屋信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOwnerRoomRelInfoInstance(Map info) throws DAOException;


    /**
     * 查询业主房屋总数
     *
     * @param info 业主房屋信息
     * @return 业主房屋数量
     */
    int queryOwnerRoomRelsCount(Map info);

    /**
     * 保存关系
     *
     * @param beanCovertMap
     * @return
     */
    int saveOwnerRoomRels(Map beanCovertMap);

    /**
     * 修改关系
     *
     * @param beanCovertMap
     * @return
     */
    int updateOwnerRoomRels(Map beanCovertMap);
}
