package com.java110.report.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.report.ReportFeeDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.intf.report.IGeneratorFeeMonthStatisticsInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.dao.IReportCommunityServiceDao;
import com.java110.report.dao.IReportFeeMonthStatisticsServiceDao;
import com.java110.report.dao.IReportFeeServiceDao;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName GeneratorFeeMonthStatisticsInnerServiceSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 21:53
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@RestController
public class GeneratorFeeMonthStatisticsInnerServiceSMOImpl implements IGeneratorFeeMonthStatisticsInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorFeeMonthStatisticsInnerServiceSMOImpl.class);

    //默认 处理房屋数量
    private static final int DEFAULT_DEAL_ROOM_COUNT = 1000;

    @Autowired
    private IReportFeeMonthStatisticsServiceDao reportFeeMonthStatisticsServiceDaoImpl;

    @Autowired
    private IReportCommunityServiceDao reportCommunityServiceDaoImpl;

    @Autowired
    private IReportFeeServiceDao reportFeeServiceDaoImpl;

    @Override
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        String communityId = reportFeeMonthStatisticsPo.getCommunityId();

        Assert.hasLength(communityId, "未包含小区信息");

        //处理房屋费用
        dealRoomFee(reportFeeMonthStatisticsPo);


        return 0;
    }


    /**
     * 处理 房屋费用
     *
     * @param reportFeeMonthStatisticsPo
     */
    private void dealRoomFee(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        int page = 0;
        int max = DEFAULT_DEAL_ROOM_COUNT;

        ReportRoomDto reportRoomDto = new ReportRoomDto();
        reportRoomDto.setCommunityId(reportFeeMonthStatisticsPo.getCommunityId());
        int count = reportCommunityServiceDaoImpl.getRoomCount(reportRoomDto);


        if (count < DEFAULT_DEAL_ROOM_COUNT) {
            page = 1;
            max = count;
        } else {
            page = (int) Math.ceil((double) count / (double) DEFAULT_DEAL_ROOM_COUNT);
            max = DEFAULT_DEAL_ROOM_COUNT;
        }

        for (int pageIndex = 0; pageIndex < page; pageIndex++) {
            reportRoomDto.setPage(pageIndex * max);
            reportRoomDto.setRow(max);
            List<ReportRoomDto> reportRoomDtos = reportCommunityServiceDaoImpl.getRoomFloorUnitAndOwner(reportRoomDto);
            for (ReportRoomDto tmpReportRoomDto : reportRoomDtos) {
                try {
                    doDealRoomFee(tmpReportRoomDto);
                } catch (Exception e) {
                    logger.error("生成费用报表失败" + JSONObject.toJSONString(tmpReportRoomDto), e);
                }
            }
        }
    }

    /**
     * 处理费用
     *
     * @param reportRoomDto
     */
    private void doDealRoomFee(ReportRoomDto reportRoomDto) {
        ReportFeeDto reportFeeDto = new ReportFeeDto();
        reportFeeDto.setPayerObjId(reportRoomDto.getRoomId());
        reportFeeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        reportFeeDto.setState(FeeDto.STATE_DOING);
        List<ReportFeeDto> feeDtos = reportFeeServiceDaoImpl.getFees(reportFeeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }


    }
}
