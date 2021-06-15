package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.report.ReportCarDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.report.dao.IReportCommunityServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
