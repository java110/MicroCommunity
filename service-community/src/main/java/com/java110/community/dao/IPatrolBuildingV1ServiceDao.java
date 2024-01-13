package com.java110.community.dao;

import java.util.List;
import java.util.Map;

public interface IPatrolBuildingV1ServiceDao {

    /**
     * 保存巡楼信息
     *
     * @param info
     */
    int savePatrolBuildingInfo(Map info);

    /**
     * 修改巡楼信息
     *
     * @param info 修改信息
     */
    int updatePatrolBuildingInfo(Map info);

    /**
     * 查询巡楼信息
     *
     * @param info 查询巡楼信息
     */
    List<Map> getPatrolBuildingInfo(Map info);


    /**
     * 查询巡检项目总数
     *
     * @param info 巡检项目信息
     * @return 巡检项目数量
     */
    int queryPatrolBuildingsCount(Map info);

}
