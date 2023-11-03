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
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getReceivedRoomCount", info);
        if (infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
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

    @Override
    public List<Map> getCommunityFeeDetailCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityFeeDetailCount", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityRepairCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityRepairCount", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityFeeDetailCountAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityFeeDetailCountAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityRepairCountAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityRepairCountAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityInspectionAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityInspectionAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityMaintainanceAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityMaintainanceAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityItemInAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityItemInAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityItemOutAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityItemOutAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityCarInAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityCarInAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityPersonInAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityPersonInAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getCommunityContractAnalysis(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getCommunityContractAnalysis", info);
        return infos;
    }

    @Override
    public List<Map> getPropertyFeeSummaryData(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getPropertyFeeSummaryData", info);
        return infos;
    }

    @Override
    public int getPropertyFeeSummaryDataCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getPropertyFeeSummaryDataCount", info);
        if (infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }

    @Override
    public List<Map> computeEveryMonthFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.computeEveryMonthFee", info);
        return infos;
    }

    @Override
    public int getParkingFeeSummaryDataCount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getParkingFeeSummaryDataCount", info);
        if (infos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(infos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getParkingFeeSummaryData(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("baseDataStatisticsServiceDaoImpl.getParkingFeeSummaryData", info);
        return infos;
    }
}
