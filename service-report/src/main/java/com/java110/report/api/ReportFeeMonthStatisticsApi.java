package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.bmo.reportFeeMonthStatistics.IDeleteReportFeeMonthStatisticsBMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IGetReportFeeMonthStatisticsBMO;
import com.java110.report.bmo.reportFeeMonthStatistics.ISaveReportFeeMonthStatisticsBMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IUpdateReportFeeMonthStatisticsBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/reportFeeMonthStatistics")
public class ReportFeeMonthStatisticsApi {

    @Autowired
    private ISaveReportFeeMonthStatisticsBMO saveReportFeeMonthStatisticsBMOImpl;
    @Autowired
    private IUpdateReportFeeMonthStatisticsBMO updateReportFeeMonthStatisticsBMOImpl;
    @Autowired
    private IDeleteReportFeeMonthStatisticsBMO deleteReportFeeMonthStatisticsBMOImpl;

    @Autowired
    private IGetReportFeeMonthStatisticsBMO getReportFeeMonthStatisticsBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeMonthStatistics/saveReportFeeMonthStatistics
     * @path /app/reportFeeMonthStatistics/saveReportFeeMonthStatistics
     */
    @RequestMapping(value = "/saveReportFeeMonthStatistics", method = RequestMethod.POST)
    public ResponseEntity<String> saveReportFeeMonthStatistics(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "feeYear", "请求报文中未包含feeYear");


        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsPo.class);
        return saveReportFeeMonthStatisticsBMOImpl.save(reportFeeMonthStatisticsPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeMonthStatistics/updateReportFeeMonthStatistics
     * @path /app/reportFeeMonthStatistics/updateReportFeeMonthStatistics
     */
    @RequestMapping(value = "/updateReportFeeMonthStatistics", method = RequestMethod.POST)
    public ResponseEntity<String> updateReportFeeMonthStatistics(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "feeYear", "请求报文中未包含feeYear");
        Assert.hasKeyAndValue(reqJson, "statisticsId", "statisticsId不能为空");


        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsPo.class);
        return updateReportFeeMonthStatisticsBMOImpl.update(reportFeeMonthStatisticsPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /reportFeeMonthStatistics/deleteReportFeeMonthStatistics
     * @path /app/reportFeeMonthStatistics/deleteReportFeeMonthStatistics
     */
    @RequestMapping(value = "/deleteReportFeeMonthStatistics", method = RequestMethod.POST)
    public ResponseEntity<String> deleteReportFeeMonthStatistics(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "statisticsId", "statisticsId不能为空");


        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsPo.class);
        return deleteReportFeeMonthStatisticsBMOImpl.delete(reportFeeMonthStatisticsPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryReportFeeMonthStatistics
     * @path /app/reportFeeMonthStatistics/queryReportFeeMonthStatistics
     */
    @RequestMapping(value = "/queryReportFeeMonthStatistics", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportFeeMonthStatistics(@RequestParam(value = "communityId") String communityId,
                                                                @RequestParam(value = "page") int page,
                                                                @RequestParam(value = "row") int row) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setPage(page);
        reportFeeMonthStatisticsDto.setRow(row);
        reportFeeMonthStatisticsDto.setCommunityId(communityId);
        return getReportFeeMonthStatisticsBMOImpl.get(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询费用汇总表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryReportFeeSummary
     * @path /app/reportFeeMonthStatistics/queryReportFeeSummary
     */
    @RequestMapping(value = "/queryReportFeeSummary", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportFeeSummary(@RequestParam(value = "communityId") String communityId,
                                                        @RequestParam(value = "floorId", required = false) String floorId,
                                                        @RequestParam(value = "floorNum", required = false) String floorNum,
                                                        @RequestParam(value = "unitNum", required = false) String unitNum,
                                                        @RequestParam(value = "unitId", required = false) String unitId,
                                                        @RequestParam(value = "roomId", required = false) String roomId,
                                                        @RequestParam(value = "roomNum", required = false) String roomNum,
                                                        @RequestParam(value = "startTime", required = false) String startTime,
                                                        @RequestParam(value = "endTime", required = false) String endTime,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setPage(page);
        reportFeeMonthStatisticsDto.setRow(row);
        reportFeeMonthStatisticsDto.setCommunityId(communityId);
        reportFeeMonthStatisticsDto.setFloorId(floorId);
        reportFeeMonthStatisticsDto.setFloorNum(floorNum);
        reportFeeMonthStatisticsDto.setUnitId(unitId);
        reportFeeMonthStatisticsDto.setUnitNum(unitNum);
        reportFeeMonthStatisticsDto.setRoomId(roomId);
        reportFeeMonthStatisticsDto.setRoomNum(roomNum);
        reportFeeMonthStatisticsDto.setStartTime(startTime);
        reportFeeMonthStatisticsDto.setEndTime(endTime);
        return getReportFeeMonthStatisticsBMOImpl.queryReportFeeSummary(reportFeeMonthStatisticsDto);
    }
}
