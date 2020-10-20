package com.java110.report.bmo.reportFeeMonthStatistics.impl;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IGetReportFeeMonthStatisticsBMO;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("getReportFeeMonthStatisticsBMOImpl")
public class GetReportFeeMonthStatisticsBMOImpl implements IGetReportFeeMonthStatisticsBMO {

    private static final Logger logger = LoggerFactory.getLogger(GetReportFeeMonthStatisticsBMOImpl.class);

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    /**
     * @param reportFeeMonthStatisticsDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {


        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeMonthStatisticssCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeMonthStatisticss(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryReportFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeSummaryCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeSummary(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryReportFloorUnitFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeSummaryCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeSummary(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryFeeBreakdown(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdownCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdown(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetailCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetail(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryOweFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetailCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetail(reportFeeMonthStatisticsDto);

            freshReportOweDay(reportFeeMonthStatisticsDtos);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryDeadlineFee(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryDeadlineFeeCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryDeadlineFee(reportFeeMonthStatisticsDto);

            freshReportOweDay(reportFeeMonthStatisticsDtos);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryPrePaymentCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;

        reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPrePaymentCount(reportFeeMonthStatisticsDto);

        ResultVo resultVo = new ResultVo(reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryDeadlinePaymentCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;

        reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryDeadlinePaymentCount(reportFeeMonthStatisticsDto);

        ResultVo resultVo = new ResultVo(reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryOwePaymentCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;

        reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOwePaymentCount(reportFeeMonthStatisticsDto);

        ResultVo resultVo = new ResultVo(reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryPrePayment(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPrePaymentNewCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPrePayment(reportFeeMonthStatisticsDto);
            for (ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
                if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(tmpReportFeeMonthStatisticsDto.getPayerObjType())) {
                    tmpReportFeeMonthStatisticsDto.setObjName(tmpReportFeeMonthStatisticsDto.getFloorNum()
                            + "栋" + tmpReportFeeMonthStatisticsDto.getUnitNum()
                            + "单元" + tmpReportFeeMonthStatisticsDto.getRoomNum() + "室");
                } else {
                    tmpReportFeeMonthStatisticsDto.setObjName(tmpReportFeeMonthStatisticsDto.getCarNum());
                }

                try {
                    tmpReportFeeMonthStatisticsDto.setOweDay(DateUtil.daysBetween(DateUtil.getDateFromString(tmpReportFeeMonthStatisticsDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A), DateUtil.getCurrentDate()));
                } catch (ParseException e) {
                    logger.error("费用结束时间错误" + tmpReportFeeMonthStatisticsDto.getEndTime(), e);
                }
            }
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshReportOweDay(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {

        Date nowDate = DateUtil.getCurrentDate();

        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            try {
                int day = DateUtil.daysBetween(DateUtil.getDateFromString(reportFeeMonthStatisticsDto.getDeadlineTime(),
                        DateUtil.DATE_FORMATE_STRING_A), nowDate);
                reportFeeMonthStatisticsDto.setOweDay(day);
            } catch (Exception e) {
                logger.error("计算欠费天数失败", e);
            }

        }
    }


}
