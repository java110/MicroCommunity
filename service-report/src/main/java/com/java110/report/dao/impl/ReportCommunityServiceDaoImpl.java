package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.report.dao.IReportCommunityServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportCommunityServiceDaoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 22:15
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@Service("reportCommunityServiceDaoImpl")
public class ReportCommunityServiceDaoImpl extends BaseServiceDao implements IReportCommunityServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportCommunityServiceDaoImpl.class);

    @Override
    public int getRoomCount(ReportRoomDto reportRoomDto) {
        logger.debug("查询费用月统计数据 入参 info : {}", JSONObject.toJSONString(reportRoomDto));

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.getRoomCount", reportRoomDto);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<ReportRoomDto> getRoomFloorUnitAndOwner(ReportRoomDto reportRoomDto) {
        logger.debug("查询房屋信息 入参 info : {}", JSONObject.toJSONString(reportRoomDto));

        List<ReportRoomDto> roomDtos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.getRoomFloorUnitAndOwner", reportRoomDto);

        return roomDtos;
    }

    /**
     * 统计车辆
     *
     * @param reportCarDto
     * @return
     */
    @Override
    public int getCarCount(ReportCarDto reportCarDto) {
        logger.debug("查询费用月统计数据 入参 info : {}", JSONObject.toJSONString(reportCarDto));

        List<Map> businessReportFeeMonthStatisticsInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.getCarCount", reportCarDto);
        if (businessReportFeeMonthStatisticsInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthStatisticsInfos.get(0).get("count").toString());
    }

    @Override
    public List<ReportCarDto> getCarParkingSpace(ReportCarDto reportCarDto) {
        logger.debug("查询房屋信息 入参 info : {}", JSONObject.toJSONString(reportCarDto));

        List<ReportCarDto> carDtos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.getCarParkingSpace", reportCarDto);

        return carDtos;
    }

    @Override
    public List<Map> getCommunitys(Map communityDto) {
        logger.debug("查询getCommunitys信息 入参 info : {}", JSONObject.toJSONString(communityDto));

        List<Map> communityDtos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.getCommunitys", communityDto);

        return communityDtos;
    }

    @Override
    public List<Map> queryRoomStructures(Map info) {
        logger.debug("查询queryRoomStructures信息 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> communityDtos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryRoomStructures", info);

        return communityDtos;
    }

    @Override
    public List<Map> queryCarStructures(Map info) {
        logger.debug("查询queryCarStructures信息 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> communityDtos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryCarStructures", info);

        return communityDtos;
    }

    public int deleteInvalidFee(Map info){
        logger.debug("deleteInvalidFee 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportCommunityServiceDaoImpl.deleteInvalidFee", info);

        return saveFlag;
    }

    @Override
    public List<Map> queryInvalidFeeMonthStatistics(Map info) {
        logger.debug("查询押金退费总金额信息 入参 info : {}", info);

        List<Map> deposits = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryInvalidFeeMonthStatistics", info);

        return deposits;
    }


    @Override
    public List<Map> queryRoomsTree(Map info) {
        logger.debug("查询queryRoomsTree信息 入参 info : {}", JSONObject.toJSONString(info));

        List<Map> communityDtos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryRoomsTree", info);

        return communityDtos;
    }

    @Override
    public int queryHisOwnerCarCount(Map info) {
        logger.debug("查询车辆管理数据 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryHisOwnerCarCount", info);
        if (businessOwnerCarInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerCarInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHisOwnerCars(Map info) {
        logger.debug("查询车辆管理信息 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryHisOwnerCars", info);

        return businessOwnerCarInfos;
    }

    @Override
    public int queryHisOwnerCount(Map info) {
        logger.debug("查询queryHisOwnerCount 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryHisOwnerCount", info);
        if (businessOwnerCarInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerCarInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHisOwners(Map info) {
        logger.debug("查询queryHisOwners 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryHisOwners", info);

        return businessOwnerCarInfos;
    }

    @Override
    public int queryHisFeeCount(Map info) {
        logger.debug("查询queryHisFeeCount 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryHisFeeCount", info);
        if (businessOwnerCarInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerCarInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryHisFees(Map info) {
        logger.debug("查询 queryHisFees 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("reportCommunityServiceDaoImpl.queryHisFees", info);

        return businessOwnerCarInfos;
    }
}
