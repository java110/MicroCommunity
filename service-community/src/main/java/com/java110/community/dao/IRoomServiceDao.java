package com.java110.community.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 小区房屋组件内部之间使用，没有给外围系统提供服务能力
 * 小区房屋服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IRoomServiceDao {

    /**
     * 保存 小区房屋信息
     *
     * @param businessRoomInfo 小区房屋信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessRoomInfo(Map businessRoomInfo) throws DAOException;


    /**
     * 查询小区房屋信息（business过程）
     * 根据bId 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     * @throws DAOException
     */
    List<Map> getBusinessRoomInfo(Map info) throws DAOException;


    /**
     * 保存 小区房屋信息 Business数据到 Instance中
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void saveRoomInfoInstance(Map info) throws DAOException;


    /**
     * 查询小区房屋信息（instance过程）
     * 根据bId 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     * @throws DAOException
     */
    List<Map> getRoomInfo(Map info) throws DAOException;


    /**
     * 修改小区房屋信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void updateRoomInfoInstance(Map info) throws DAOException;


    /**
     * 查询小区房屋总数
     *
     * @param info 小区房屋信息
     * @return 小区房屋数量
     */
    int queryRoomsCount(Map info);

    /**
     * 查询小区房屋总数
     *
     * @param info 小区房屋信息
     * @return 小区房屋数量
     */
    int queryRoomsByCommunityIdCount(Map info);

    /**
     * 查询小区房屋（未销售）总数
     *
     * @param info 小区房屋信息
     * @return 小区房屋数量
     */
    int queryRoomsWithOutSellByCommunityIdCount(Map info);

    /**
     * 查询小区房屋（未销售）总数
     *
     * @param info 小区房屋信息
     * @return 小区房屋数量
     */
    int queryRoomsWithSellByCommunityIdCount(Map info);


    /**
     * 查询小区房屋信息
     * 根据bId 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     */
    List<Map> getRoomInfoByCommunityId(Map info);


    /**
     * 根据业主查询小区房屋信息
     * 根据bId 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     */
    List<Map> getRoomInfoByOwner(Map info);


    /**
     * 查询小区房屋信息
     * 根据bId 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     */
    List<Map> getRoomInfoWithOutSellByCommunityId(Map info);


    /**
     * 查询小区房屋信息
     * 根据bId 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     */
    List<Map> getRoomInfoWithSellByCommunityId(Map info);


    public List<Map> getRoomInfos(Map info);

}
