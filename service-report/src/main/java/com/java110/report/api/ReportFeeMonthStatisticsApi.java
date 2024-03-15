package com.java110.report.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.room.RoomDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.report.ReportDeposit;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.po.reportFee.ReportFeeMonthStatisticsPo;
import com.java110.report.bmo.reportFeeMonthStatistics.IDeleteReportFeeMonthStatisticsBMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IGetReportFeeMonthStatisticsBMO;
import com.java110.report.bmo.reportFeeMonthStatistics.ISaveReportFeeMonthStatisticsBMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IUpdateReportFeeMonthStatisticsBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
     * @serviceCode /reportFeeMonthStatistics/queryFloorUnitFeeSummary
     * @path /app/reportFeeMonthStatistics/queryFloorUnitFeeSummary
     */
    @RequestMapping(value = "/queryFloorUnitFeeSummary", method = RequestMethod.GET)
    public ResponseEntity<String> queryFloorUnitFeeSummary(@RequestParam(value = "communityId") String communityId,
                                                           @RequestParam(value = "floorId", required = false) String floorId,
                                                           @RequestParam(value = "floorNum", required = false) String floorNum,
                                                           @RequestParam(value = "unitNum", required = false) String unitNum,
                                                           @RequestParam(value = "unitId", required = false) String unitId,
                                                           @RequestParam(value = "roomId", required = false) String roomId,
                                                           @RequestParam(value = "roomNum", required = false) String roomNum,
                                                           @RequestParam(value = "startTime", required = false) String startTime,
                                                           @RequestParam(value = "endTime", required = false) String endTime,
                                                           @RequestParam(value = "configIds", required = false) String configIds,
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
        if (!StringUtil.isEmpty(configIds)) {
            reportFeeMonthStatisticsDto.setConfigIds(configIds.split(","));
        }
        return getReportFeeMonthStatisticsBMOImpl.queryReportFloorUnitFeeSummary(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryFeeBreakdown
     * @path /app/reportFeeMonthStatistics/queryFeeBreakdown
     */
    @RequestMapping(value = "/queryFeeBreakdown", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeBreakdown(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "floorId", required = false) String floorId,
                                                    @RequestParam(value = "floorNum", required = false) String floorNum,
                                                    @RequestParam(value = "unitNum", required = false) String unitNum,
                                                    @RequestParam(value = "unitId", required = false) String unitId,
                                                    @RequestParam(value = "roomId", required = false) String roomId,
                                                    @RequestParam(value = "roomNum", required = false) String roomNum,
                                                    @RequestParam(value = "configId", required = false) String configId,
                                                    @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                    @RequestParam(value = "startTime", required = false) String startTime,
                                                    @RequestParam(value = "endTime", required = false) String endTime,
                                                    @RequestParam(value = "yearMonth", required = false) String yearMonth,
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
        reportFeeMonthStatisticsDto.setConfigId(configId);
        reportFeeMonthStatisticsDto.setFeeTypeCd(feeTypeCd);
        reportFeeMonthStatisticsDto.setYearMonth(yearMonth);
        if (!StringUtil.isEmpty(startTime)) {
            reportFeeMonthStatisticsDto.setStartTime(startTime + " 00:00:00");
        }
        if (!StringUtil.isEmpty(endTime)) {
            reportFeeMonthStatisticsDto.setEndTime(endTime + " 23:59:59");
        }
        return getReportFeeMonthStatisticsBMOImpl.queryFeeBreakdown(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryFeeDetail
     * @path /app/reportFeeMonthStatistics/queryFeeDetail
     */
    @RequestMapping(value = "/queryFeeDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeDetail(@RequestParam(value = "communityId") String communityId,
                                                 @RequestParam(value = "floorId", required = false) String floorId,
                                                 @RequestParam(value = "floorNum", required = false) String floorNum,
                                                 @RequestParam(value = "unitNum", required = false) String unitNum,
                                                 @RequestParam(value = "unitId", required = false) String unitId,
                                                 @RequestParam(value = "roomId", required = false) String roomId,
                                                 @RequestParam(value = "roomNum", required = false) String roomNum,
                                                 @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                 @RequestParam(value = "startTime", required = false) String startTime,
                                                 @RequestParam(value = "endTime", required = false) String endTime,
                                                 @RequestParam(value = "feeName", required = false) String feeName,
                                                 @RequestParam(value = "configId", required = false) String configId,
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
        reportFeeMonthStatisticsDto.setFeeName(feeName);
        reportFeeMonthStatisticsDto.setConfigId(configId);
        reportFeeMonthStatisticsDto.setFeeTypeCd(feeTypeCd);
        reportFeeMonthStatisticsDto.setStartTime(startTime);
        reportFeeMonthStatisticsDto.setEndTime(endTime);
        return getReportFeeMonthStatisticsBMOImpl.queryFeeDetail(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryOweFeeDetail
     * @path /app/reportFeeMonthStatistics/queryOweFeeDetail
     */
    @RequestMapping(value = "/queryOweFeeDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryOweFeeDetail(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "floorId", required = false) String floorId,
                                                    @RequestParam(value = "floorNum", required = false) String floorNum,
                                                    @RequestParam(value = "unitNum", required = false) String unitNum,
                                                    @RequestParam(value = "unitId", required = false) String unitId,
                                                    @RequestParam(value = "roomId", required = false) String roomId,
                                                    @RequestParam(value = "roomNum", required = false) String roomNum,
                                                    @RequestParam(value = "startTime", required = false) String startTime,
                                                    @RequestParam(value = "endTime", required = false) String endTime,
                                                    @RequestParam(value = "objName", required = false) String objName,
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
        reportFeeMonthStatisticsDto.setObjName(objName);
        return getReportFeeMonthStatisticsBMOImpl.queryOweFeeDetail(reportFeeMonthStatisticsDto);
    }


    /**
     * 押金报表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryPayFeeDeposit
     * @path /app/reportFeeMonthStatistics/queryPayFeeDeposit
     */
    @RequestMapping(value = "/queryPayFeeDeposit", method = RequestMethod.GET)
    public ResponseEntity<String> queryPayFeeDeposit(@RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "configId", required = false) String configId,
                                                     @RequestParam(value = "feeId", required = false) String feeId,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime,
                                                     @RequestParam(value = "payerObjType", required = false) String payerObjType,
                                                     @RequestParam(value = "state", required = false) String state,
                                                     @RequestParam(value = "detailState", required = false) String detailState,
                                                     @RequestParam(value = "floorId", required = false) String floorId,
                                                     @RequestParam(value = "unitId", required = false) String unitId,
                                                     @RequestParam(value = "roomNum", required = false) String roomNum,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        ReportDeposit reportDeposit = new ReportDeposit();
        reportDeposit.setPage(page);
        reportDeposit.setRow(row);
        reportDeposit.setConfigId(configId);
        reportDeposit.setFeeId(feeId);
        reportDeposit.setFeeStartTime(startTime);
        reportDeposit.setFeeEndTime(endTime);
        reportDeposit.setPayerObjType(payerObjType);
        reportDeposit.setState(state);
        reportDeposit.setDetailState(detailState);
        reportDeposit.setCommunityId(communityId);
        reportDeposit.setFloorId(floorId);
        reportDeposit.setUnitId(unitId);
        reportDeposit.setRoomNum(roomNum);
        return getReportFeeMonthStatisticsBMOImpl.queryPayFeeDeposit(reportDeposit);
    }

    /**
     * 报修统计表
     *
     * @param communityId 小区id
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryRepair
     * @path /app/reportFeeMonthStatistics/queryRepair
     */
    @RequestMapping(value = "/queryRepair", method = RequestMethod.GET)
    public ResponseEntity<String> queryRepair(@RequestParam(value = "communityId") String communityId,
                                              @RequestParam(value = "repairId", required = false) String repairId,
                                              @RequestParam(value = "state", required = false) String state,
                                              @RequestParam(value = "staffId", required = false) String staffId,
                                              @RequestParam(value = "staffName", required = false) String staffName,
                                              @RequestParam(value = "preStaffId", required = false) String preStaffId,
                                              @RequestParam(value = "preStaffName", required = false) String preStaffName,
                                              @RequestParam(value = "beginStartTime", required = false) String beginStartTime,
                                              @RequestParam(value = "beginEndTime", required = false) String beginEndTime,
                                              @RequestParam(value = "finishStartTime", required = false) String finishStartTime,
                                              @RequestParam(value = "finishEndTime", required = false) String finishEndTime,
                                              @RequestParam(value = "page") int page,
                                              @RequestParam(value = "row") int row) {
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setCommunityId(communityId);
        repairUserDto.setRepairId(repairId);
        repairUserDto.setState(state);
        repairUserDto.setStaffId(staffId);
        repairUserDto.setStaffName(staffName);
        repairUserDto.setPreStaffId(preStaffId);
        repairUserDto.setPreStaffName(preStaffName);
        if (!StringUtil.isEmpty(beginStartTime)) {
            repairUserDto.setBeginStartTime(beginStartTime + " 00:00:00");
        }
        if (!StringUtil.isEmpty(beginEndTime)) {
            repairUserDto.setBeginEndTime(beginEndTime + " 23:59:59");
        }
        if (!StringUtil.isEmpty(finishStartTime)) {
            repairUserDto.setFinishStartTime(finishStartTime + " 00:00:00");
        }
        if (!StringUtil.isEmpty(finishEndTime)) {
            repairUserDto.setFinishEndTime(finishEndTime + " 23:59:59");
        }
        repairUserDto.setPage(page);
        repairUserDto.setRow(row);
        return getReportFeeMonthStatisticsBMOImpl.queryRepair(repairUserDto);
    }

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryDeadlineFee
     * @path /app/reportFeeMonthStatistics/queryDeadlineFee
     */
    @RequestMapping(value = "/queryDeadlineFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryDeadlineFee(@RequestParam(value = "communityId") String communityId,
                                                   @RequestParam(value = "floorId", required = false) String floorId,
                                                   @RequestParam(value = "floorNum", required = false) String floorNum,
                                                   @RequestParam(value = "unitNum", required = false) String unitNum,
                                                   @RequestParam(value = "unitId", required = false) String unitId,
                                                   @RequestParam(value = "roomId", required = false) String roomId,
                                                   @RequestParam(value = "roomNum", required = false) String roomNum,
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
        reportFeeMonthStatisticsDto.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        reportFeeMonthStatisticsDto.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        return getReportFeeMonthStatisticsBMOImpl.queryDeadlineFee(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryPrePaymentCount
     * @path /app/reportFeeMonthStatistics/queryPrePaymentCount
     */
    @RequestMapping(value = "/queryPrePaymentCount", method = RequestMethod.GET)
    public ResponseEntity<String> queryPrePaymentCount(@RequestParam(value = "communityId") String communityId) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(communityId);
        reportFeeMonthStatisticsDto.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        reportFeeMonthStatisticsDto.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        return getReportFeeMonthStatisticsBMOImpl.queryPrePaymentCount(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询到期提醒
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryDeadlineCount
     * @path /app/reportFeeMonthStatistics/queryDeadlineCount
     */
    @RequestMapping(value = "/queryDeadlineCount", method = RequestMethod.GET)
    public ResponseEntity<String> queryDeadlineCount(@RequestParam(value = "communityId") String communityId) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(communityId);
        reportFeeMonthStatisticsDto.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        reportFeeMonthStatisticsDto.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        return getReportFeeMonthStatisticsBMOImpl.queryDeadlinePaymentCount(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryOwePaymentCount
     * @path /app/reportFeeMonthStatistics/queryOwePaymentCount
     */
    @RequestMapping(value = "/queryOwePaymentCount", method = RequestMethod.GET)
    public ResponseEntity<String> queryOwePaymentCount(@RequestParam(value = "communityId") String communityId) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(communityId);
        return getReportFeeMonthStatisticsBMOImpl.queryOwePaymentCount(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询未收费房屋
     * select t.* from building_room t
     * inner join building_unit bu on t.unit_id  = bu.unit_id and bu.status_cd = '0'
     * inner join f_floor f on bu.floor_id = f.floor_id and f.status_cd = '0'
     * where t.status_cd = '0'
     * and not exists(
     * 	select 1 from pay_fee pf where t.room_id = pf.payer_obj_id and pf.status_cd = '0' and pf.state = '2008001'
     * )
     *
     * limit 10
     */

    /**
     * 查询费用分项表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryNoFeeRooms
     * @path /app/reportFeeMonthStatistics/queryNoFeeRooms
     */
    @RequestMapping(value = "/queryNoFeeRooms", method = RequestMethod.GET)
    public ResponseEntity<String> queryNoFeeRooms(@RequestParam(value = "communityId") String communityId,
                                                  @RequestParam(value = "floorId") String floorId,
                                                  @RequestParam(value = "unitId") String unitId,
                                                  @RequestParam(value = "roomId") String roomId,
                                                  @RequestParam(value = "ownerName") String ownerName,
                                                  @RequestParam(value = "link") String link,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "row") int row) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setFloorId(floorId);
        roomDto.setUnitId(unitId);
        roomDto.setRoomId(roomId);
        roomDto.setOwnerName(ownerName);
        roomDto.setLink(link);
        roomDto.setPage(page);
        roomDto.setRow(row);
        return getReportFeeMonthStatisticsBMOImpl.queryNoFeeRooms(roomDto);
    }

    /**
     * 查询华宁物业 欠费统计报表
     * 作者： 吴学文
     * 时间：2021-08-13
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryHuaningOweFee
     * @path /app/reportFeeMonthStatistics/queryHuaningOweFee
     */
    @RequestMapping(value = "/queryHuaningOweFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryHuaningOweFee(@RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "configId", required = false) String configId,
                                                     @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                     @RequestParam(value = "floorNum", required = false) String floorNum,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = new ReportFeeMonthStatisticsDto();
        reportFeeMonthStatisticsDto.setCommunityId(communityId);
        reportFeeMonthStatisticsDto.setConfigId(configId);
        reportFeeMonthStatisticsDto.setFeeTypeCd(feeTypeCd);
        reportFeeMonthStatisticsDto.setFloorNum(floorNum);
        reportFeeMonthStatisticsDto.setPage(page);
        reportFeeMonthStatisticsDto.setRow(row);
        reportFeeMonthStatisticsDto.setStartTime(DateUtil.getYear() + "-01-01");
        reportFeeMonthStatisticsDto.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        return getReportFeeMonthStatisticsBMOImpl.queryHuaningOweFee(reportFeeMonthStatisticsDto);
    }

    /**
     * 查询华宁物业 欠费统计报表
     * 作者： 吴学文
     * 时间：2021-08-13
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryHuaningPayFee
     * @path /app/reportFeeMonthStatistics/queryHuaningPayFee
     */
    @RequestMapping(value = "/queryHuaningPayFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryHuaningPayFee(@RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "configId", required = false) String configId,
                                                     @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                     @RequestParam(value = "floorNum", required = false) String floorNum,
                                                     @RequestParam(value = "year", required = false) int year,
                                                     @RequestParam(value = "month", required = false) int month,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        Map paramInfo = new HashMap();
        paramInfo.put("communityId", communityId);
        paramInfo.put("configId", configId);
        paramInfo.put("feeTypeCd", feeTypeCd);
        paramInfo.put("floorNum", floorNum);
        paramInfo.put("year", year);
        paramInfo.put("month", month);
        paramInfo.put("page", page);
        paramInfo.put("row", row);
        paramInfo.put("startTime", DateUtil.getYear() + "-01-01");
        paramInfo.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_Q) + "-01");
        paramInfo.put("nextMonthTime", DateUtil.getFormatTimeStringB(DateUtil.getNextMonthFirstDate()));
        return getReportFeeMonthStatisticsBMOImpl.queryHuaningPayFee(paramInfo);
    }

    /**
     * 查询华宁物业 欠费统计报表
     * 作者： 吴学文
     * 时间：2021-08-13
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryHuaningPayFeeTwo
     * @path /app/reportFeeMonthStatistics/queryHuaningPayFeeTwo
     */
    @RequestMapping(value = "/queryHuaningPayFeeTwo", method = RequestMethod.GET)
    public ResponseEntity<String> queryHuaningPayFeeTwo(@RequestParam(value = "communityId") String communityId,
                                                        @RequestParam(value = "year") int year,
                                                        @RequestParam(value = "month") int month,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        Map paramInfo = new HashMap();
        paramInfo.put("communityId", communityId);
        paramInfo.put("year", year);
        paramInfo.put("month", month);
        paramInfo.put("page", page);
        paramInfo.put("row", row);
        return getReportFeeMonthStatisticsBMOImpl.queryHuaningPayFeeTwo(paramInfo);
    }

    /**
     * 查询华宁物业 欠费统计报表
     * 作者： 吴学文
     * 时间：2021-08-13
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatistics/queryHuaningOweFeeDetail
     * @path /app/reportFeeMonthStatistics/queryHuaningOweFeeDetail
     */
    @RequestMapping(value = "/queryHuaningOweFeeDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryHuaningOweFeeDetail(@RequestParam(value = "communityId") String communityId,
                                                           @RequestParam(value = "year") int year,
                                                           @RequestParam(value = "month") int month,
                                                           @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                           @RequestParam(value = "floorNum", required = false) String floorNum,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        Map paramInfo = new HashMap();
        paramInfo.put("communityId", communityId);
        paramInfo.put("year", year);
        paramInfo.put("month", month);
        paramInfo.put("page", page);
        paramInfo.put("row", row);
        paramInfo.put("feeTypeCd", feeTypeCd);
        paramInfo.put("floorNum", floorNum);
        paramInfo.put("startTime", DateUtil.getYear() + "-01-01");
        paramInfo.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        return getReportFeeMonthStatisticsBMOImpl.queryHuaningOweFeeDetail(paramInfo);
    }
}
