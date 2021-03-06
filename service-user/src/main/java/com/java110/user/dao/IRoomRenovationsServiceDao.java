package com.java110.user.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 房屋装修
 *
 * @author fqz
 * @date 2021-02-25 8:52
 */
public interface IRoomRenovationsServiceDao {

    /**
     * 查询装修申请信息（instance过程）
     * 根据bId 查询装修申请信息
     *
     * @param info bId 信息
     * @return 装修申请信息
     * @throws DAOException DAO异常
     */
    List<Map> getRoomRenovationInfo(Map info);

    /**
     * 查询装修申请总数
     *
     * @param info 装修申请信息
     * @return 装修申请数量
     */
    int queryRoomRenovationsCount(Map info);

}
