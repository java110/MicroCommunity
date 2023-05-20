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
}
