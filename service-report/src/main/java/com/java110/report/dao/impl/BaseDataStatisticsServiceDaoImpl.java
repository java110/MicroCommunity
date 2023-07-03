package com.java110.report.dao.impl;

import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IBaseDataStatisticsServiceDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 基础数据dao
 */
@Service
public class BaseDataStatisticsServiceDaoImpl extends BaseServiceDao implements IBaseDataStatisticsServiceDao {
    @Override
    public int getRoomCount(Map info) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getRoomCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    /**
     * 查询 数据库
     * @param info
     * @return
     */
    @Override
    public List<Map> getRoomInfo(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getRoomInfo", info);
        return infos;
    }

    /**
     * 查询实收房屋数
     * @param info
     * @return
     */
    @Override
    public int getReceivedRoomCount(Map info) {
        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getReceivedRoomCount", info);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    /**
     * 查询实收房屋
     * @param info
     * @return
     */
    @Override
    public List<Map> getReceivedRoomInfo(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getReceivedRoomInfo", info);
        return infos;
    }

    @Override
    public int getOweRoomCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getOweRoomCount", info);
        if (infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getOweRoomInfo(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getOweRoomInfo", info);
        return infos;
    }
}
