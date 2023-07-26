package com.java110.report.api;

import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.report.bmo.reportFeeMonthStatisticsPrepayment.IGetReportFeeMonthStatisticsPrepaymentBMO;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reportFeeMonthStatisticsPrepayment")
public class ReportFeeMonthStatisticsPrepaymentApi {

    @Autowired
    private IGetReportFeeMonthStatisticsPrepaymentBMO getReportFeeMonthStatisticsPrepaymentBMOImpl;

    /**
     * 账单明细表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatisticsPrepayment/queryPayFeePrepaymentDetail
     * @path /app/reportFeeMonthStatisticsPrepayment/queryPayFeePrepaymentDetail
     */
    @RequestMapping(value = "/queryPayFeePrepaymentDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryPayFeeDetail(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "floorId", required = false) String floorId,
                                                    @RequestParam(value = "floorNum", required = false) String floorNum,
                                                    @RequestParam(value = "unitNum", required = false) String unitNum,
                                                    @RequestParam(value = "unitId", required = false) String unitId,
                                                    @RequestParam(value = "roomId", required = false) String roomId,
                                                    @RequestParam(value = "roomNum", required = false) String roomNum,
                                                    @RequestParam(value = "primeRate", required = false) String primeRate,
                                                    @RequestParam(value = "state", required = false) String state,
                                                    @RequestParam(value = "prepaymentState", required = false) String prepaymentState,
                                                    @RequestParam(value = "billState", required = false) String billState,
                                                    @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                    @RequestParam(value = "configId", required = false) String configId,
                                                    @RequestParam(value = "startTime", required = false) String startTime,
                                                    @RequestParam(value = "endTime", required = false) String endTime,
                                                    @RequestParam(value = "startBeginTime", required = false) String startBeginTime,
                                                    @RequestParam(value = "startFinishTime", required = false) String startFinishTime,
                                                    @RequestParam(value = "endBeginTime", required = false) String endBeginTime,
                                                    @RequestParam(value = "endFinishTime", required = false) String endFinishTime,
                                                    @RequestParam(value = "objId", required = false) String objId,
                                                    @RequestParam(value = "roomName", required = false) String roomName,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
        reportFeeMonthStatisticsPrepaymentDto.setPage(page);
        reportFeeMonthStatisticsPrepaymentDto.setRow(row);
        reportFeeMonthStatisticsPrepaymentDto.setCommunityId(communityId);
        reportFeeMonthStatisticsPrepaymentDto.setFloorId(floorId);
        reportFeeMonthStatisticsPrepaymentDto.setFloorNum(floorNum);
        reportFeeMonthStatisticsPrepaymentDto.setUnitId(unitId);
        reportFeeMonthStatisticsPrepaymentDto.setUnitNum(unitNum);
        reportFeeMonthStatisticsPrepaymentDto.setRoomId(roomId);
        reportFeeMonthStatisticsPrepaymentDto.setRoomNum(roomNum);
        reportFeeMonthStatisticsPrepaymentDto.setPrimeRate(primeRate);
        reportFeeMonthStatisticsPrepaymentDto.setState(state);
        reportFeeMonthStatisticsPrepaymentDto.setPrepaymentState(prepaymentState);
        reportFeeMonthStatisticsPrepaymentDto.setbillState(billState);
        reportFeeMonthStatisticsPrepaymentDto.setFeeTypeCd(feeTypeCd);
        reportFeeMonthStatisticsPrepaymentDto.setConfigId(configId);
        reportFeeMonthStatisticsPrepaymentDto.setStartTime(startTime);
        reportFeeMonthStatisticsPrepaymentDto.setEndTime(endTime);
        reportFeeMonthStatisticsPrepaymentDto.setStartBeginTime(startBeginTime);
        reportFeeMonthStatisticsPrepaymentDto.setStartFinishTime(startFinishTime);
        reportFeeMonthStatisticsPrepaymentDto.setEndBeginTime(endBeginTime);
        reportFeeMonthStatisticsPrepaymentDto.setEndFinishTime(endFinishTime);
        reportFeeMonthStatisticsPrepaymentDto.setObjId(objId);
        if (!StringUtil.isEmpty(roomName)) {
            String[] roomNameArray = roomName.split("-", 3);
            reportFeeMonthStatisticsPrepaymentDto.setFloorNum(roomNameArray[0]);
            reportFeeMonthStatisticsPrepaymentDto.setUnitNum(roomNameArray[1]);
            reportFeeMonthStatisticsPrepaymentDto.setRoomNum(roomNameArray[2]);
        }
        return getReportFeeMonthStatisticsPrepaymentBMOImpl.queryPayFeeDetail(reportFeeMonthStatisticsPrepaymentDto);
    }

    /**
     * 收费状况表
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /reportFeeMonthStatisticsPrepayment/queryReportCollectFees
     * @path /app/reportFeeMonthStatisticsPrepayment/queryReportCollectFees
     */
    @RequestMapping(value = "/queryReportCollectFees", method = RequestMethod.GET)
    public ResponseEntity<String> queryReportCollectFees(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "floorId", required = false) String floorId,
                                                         @RequestParam(value = "floorNum", required = false) String floorNum,
                                                         @RequestParam(value = "unitNum", required = false) String unitNum,
                                                         @RequestParam(value = "unitId", required = false) String unitId,
                                                         @RequestParam(value = "roomId", required = false) String roomId,
                                                         @RequestParam(value = "roomNum", required = false) String roomNum,
                                                         @RequestParam(value = "primeRate", required = false) String primeRate,
                                                         @RequestParam(value = "state", required = false) String state,
                                                         @RequestParam(value = "prepaymentState", required = false) String prepaymentState,
                                                         @RequestParam(value = "billState", required = false) String billState,
                                                         @RequestParam(value = "feeTypeCd", required = false) String feeTypeCd,
                                                         @RequestParam(value = "configId", required = false) String configId,
                                                         @RequestParam(value = "startTime", required = false) String startTime,
                                                         @RequestParam(value = "endTime", required = false) String endTime,
                                                         @RequestParam(value = "startBeginTime", required = false) String startBeginTime,
                                                         @RequestParam(value = "startFinishTime", required = false) String startFinishTime,
                                                         @RequestParam(value = "endBeginTime", required = false) String endBeginTime,
                                                         @RequestParam(value = "endFinishTime", required = false) String endFinishTime,
                                                         @RequestParam(value = "objId", required = false) String objId,
                                                         @RequestParam(value = "roomName", required = false) String roomName,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
        reportFeeMonthStatisticsPrepaymentDto.setPage(page);
        reportFeeMonthStatisticsPrepaymentDto.setRow(row);
        reportFeeMonthStatisticsPrepaymentDto.setCommunityId(communityId);
        reportFeeMonthStatisticsPrepaymentDto.setFloorId(floorId);
        reportFeeMonthStatisticsPrepaymentDto.setFloorNum(floorNum);
        reportFeeMonthStatisticsPrepaymentDto.setUnitId(unitId);
        reportFeeMonthStatisticsPrepaymentDto.setUnitNum(unitNum);
        reportFeeMonthStatisticsPrepaymentDto.setRoomId(roomId);
        reportFeeMonthStatisticsPrepaymentDto.setRoomNum(roomNum);
        reportFeeMonthStatisticsPrepaymentDto.setPrimeRate(primeRate);
        reportFeeMonthStatisticsPrepaymentDto.setState(state);
        reportFeeMonthStatisticsPrepaymentDto.setPrepaymentState(prepaymentState);
        reportFeeMonthStatisticsPrepaymentDto.setbillState(billState);
        reportFeeMonthStatisticsPrepaymentDto.setFeeTypeCd(feeTypeCd);
        reportFeeMonthStatisticsPrepaymentDto.setConfigId(configId);
        reportFeeMonthStatisticsPrepaymentDto.setStartTime(startTime);
        reportFeeMonthStatisticsPrepaymentDto.setEndTime(endTime);
        reportFeeMonthStatisticsPrepaymentDto.setStartBeginTime(startBeginTime);
        reportFeeMonthStatisticsPrepaymentDto.setStartFinishTime(startFinishTime);
        reportFeeMonthStatisticsPrepaymentDto.setEndBeginTime(endBeginTime);
        reportFeeMonthStatisticsPrepaymentDto.setEndFinishTime(endFinishTime);
        reportFeeMonthStatisticsPrepaymentDto.setObjId(objId);
        if (!StringUtil.isEmpty(roomName)) {
            String[] roomNameArray = roomName.split("-", 3);
            reportFeeMonthStatisticsPrepaymentDto.setFloorNum(roomNameArray[0]);
            reportFeeMonthStatisticsPrepaymentDto.setUnitNum(roomNameArray[1]);
            reportFeeMonthStatisticsPrepaymentDto.setRoomNum(roomNameArray[2]);
        }
        return getReportFeeMonthStatisticsPrepaymentBMOImpl.queryReportCollectFees(reportFeeMonthStatisticsPrepaymentDto);
    }

}
