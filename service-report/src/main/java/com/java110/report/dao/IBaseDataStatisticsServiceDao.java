package com.java110.report.dao;

import java.util.List;
import java.util.Map;

public interface IBaseDataStatisticsServiceDao {
    /**
     * 查询房屋数量
     *
     * @param info
     * @return
     */
    int getRoomCount(Map info);

    /**
     * 查询房屋信息
     *
     * @param info
     * @return
     */
    List<Map> getRoomInfo(Map info);

    /**
     * 查询实收房屋数
     *
     * @param info
     * @return
     */
    int getReceivedRoomCount(Map info);

    /**
     * 查询实收房屋
     *
     * @param info
     * @return
     */
    List<Map> getReceivedRoomInfo(Map info);

    /**
     * 查询欠费房屋总数
     * @param info
     * @return
     */
    int getOweRoomCount(Map info);

    /**
     * 查询欠费房屋数
     * @param info
     * @return
     */
    List<Map> getOweRoomInfo(Map info);
}
