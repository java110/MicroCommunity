package com.java110.community.dao.impl;

import com.java110.community.dao.IPatrolBuildingV1ServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("patrolBuildingV1ServiceDaoImpl")
public class IPatrolBuildingV1ServiceDaoImpl extends BaseServiceDao implements IPatrolBuildingV1ServiceDao {

    private static Logger logger = LoggerFactory.getLogger(IPatrolBuildingV1ServiceDaoImpl.class);

    @Override
    public int savePatrolBuildingInfo(Map info) {
        logger.debug("保存 savePatrolBuildingInfo 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("patrolBuildingV1ServiceDaoImpl.savePatrolBuildingInfo", info);
        return saveFlag;
    }

    @Override
    public int updatePatrolBuildingInfo(Map info) {
        logger.debug("修改 updatePatrolBuildingInfo 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("patrolBuildingV1ServiceDaoImpl.updatePatrolBuildingInfo", info);
        return saveFlag;
    }

    @Override
    public List<Map> getPatrolBuildingInfo(Map info) {
        logger.debug("查询 getPatrolBuildingInfo 入参 info : {}", info);
        List<Map> businessInspectionItemInfos = sqlSessionTemplate.selectList("patrolBuildingV1ServiceDaoImpl.getPatrolBuildingInfo", info);
        return businessInspectionItemInfos;
    }

    @Override
    public int queryPatrolBuildingsCount(Map info) {
        logger.debug("查询 queryPatrolBuildingsCount 入参 info : {}", info);
        List<Map> businessPatrolBuildingInfos = sqlSessionTemplate.selectList("patrolBuildingV1ServiceDaoImpl.queryPatrolBuildingsCount", info);
        if (businessPatrolBuildingInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPatrolBuildingInfos.get(0).get("count").toString());
    }
}
