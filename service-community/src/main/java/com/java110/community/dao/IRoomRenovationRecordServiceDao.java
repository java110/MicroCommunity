package com.java110.community.dao;

import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface IRoomRenovationRecordServiceDao {

    /**
     * 保存 装修记录信息
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveRoomRenovationRecordInfo(Map info) throws DAOException;

    /**
     * 查询装修记录信息（instance过程）(与文件表关联)
     * 根据bId 查询装修记录信息
     *
     * @param info bId 信息
     * @return 装修记录信息
     * @throws DAOException DAO异常
     */
    List<Map> getRoomRenovationRecordsInfo(Map info) throws DAOException;

    /**
     * 查询装修记录
     *
     * @param info
     * @return
     * @throws DAOException
     */
    List<Map> findRoomRenovationRecordsInfo(Map info);

    /**
     * 查询装修记录总数(与文件表关联)
     *
     * @param info 装修记录信息
     * @return 装修记录数量
     */
    int queryRoomRenovationRecordsCount(Map info);

    /**
     * 查询装修记录数
     *
     * @param info
     * @return
     */
    int getRoomRenovationRecordsCount(Map info);

    /**
     * 修改装修记录信息
     *
     * @param info
     * @return
     */
    void updateRoomRenovationRecordInfo(Map info);
}
