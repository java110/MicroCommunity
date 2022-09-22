package com.java110.report.bmo.reportFeeMonthStatistics.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.report.ReportDeposit;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsTotalDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IGetReportFeeMonthStatisticsBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("getReportFeeMonthStatisticsBMOImpl")
public class GetReportFeeMonthStatisticsBMOImpl implements IGetReportFeeMonthStatisticsBMO {

    private static final Logger logger = LoggerFactory.getLogger(GetReportFeeMonthStatisticsBMOImpl.class);

    private int MAX_ROWS = 500;  // 最大行数

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

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

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = new ArrayList<>();
        if (count > 0) {
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeSummary(reportFeeMonthStatisticsDto);
            if (reportFeeMonthStatisticsDto.getConfigIds() != null) {
                reportFeeMonthStatisticsList = dealConfigReportFeeMonthStatisticsList(reportFeeMonthStatisticsList, "FeeSummary");
            }
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsList) {
                //获取应收金额
                double receivableAmount = Double.parseDouble(reportFeeMonthStatistics.getReceivableAmount());
                //获取实收金额
                double receivedAmount = Double.parseDouble(reportFeeMonthStatistics.getReceivedAmount());
                if (receivableAmount != 0) {
                    double chargeRate = (receivedAmount / receivableAmount) * 100.0;
                    reportFeeMonthStatistics.setChargeRate(String.format("%.2f", chargeRate) + "%");
                } else {
                    reportFeeMonthStatistics.setChargeRate("0%");

                }
                reportFeeMonthStatisticsDtos.add(reportFeeMonthStatistics);

            }
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFeeSummaryMajor(reportFeeMonthStatisticsDto);
            if (reportFeeMonthStatisticsList != null && reportFeeMonthStatisticsList.size() > 0) {
                for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 : reportFeeMonthStatisticsList) {
                    reportFeeMonthStatisticsDto1.setAllReceivableAmount(tmpReportFeeMonthStatisticsDto.getAllReceivableAmount());
                    reportFeeMonthStatisticsDto1.setAllReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllReceivedAmount());
                    reportFeeMonthStatisticsDto1.setAllHisOweReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllHisOweReceivedAmount());
                }
            }
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 将configId group by 后数据 合并处理
     *
     * @param reportFeeMonthStatisticsList
     * @return
     */
    private List<ReportFeeMonthStatisticsDto> dealConfigReportFeeMonthStatisticsList(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsList, String flag) {
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = new ArrayList<>();
        BigDecimal hisOweAmountDec = null;
        BigDecimal curReceivableAmountDec = null;
        BigDecimal curReceivedAmountDec = null;
        BigDecimal hisOweReceivedAmountDec = null;
        BigDecimal preReceivedAmountDec = null;
        BigDecimal receivableAmountDec = null;
        BigDecimal receivedAmountDec = null;
        List<FeeConfigDto> feeConfigDtos = null;
        FeeConfigDto feeConfigDto = null;
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsList) {
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = hasReportFeeMonthStatisticsDto(reportFeeMonthStatisticsDtos, reportFeeMonthStatisticsDto, flag);
            if (tmpReportFeeMonthStatisticsDto == null) {
                feeConfigDtos = new ArrayList<>();
                feeConfigDto = new FeeConfigDto();
                feeConfigDto.setConfigId(reportFeeMonthStatisticsDto.getConfigId());
                feeConfigDto.setAmount(Double.parseDouble(reportFeeMonthStatisticsDto.getReceivedAmount()));
                feeConfigDtos.add(feeConfigDto);
                reportFeeMonthStatisticsDto.setFeeConfigDtos(feeConfigDtos);
                reportFeeMonthStatisticsDtos.add(reportFeeMonthStatisticsDto);
                continue;
            }
            feeConfigDtos = tmpReportFeeMonthStatisticsDto.getFeeConfigDtos();
            feeConfigDto = new FeeConfigDto();
            feeConfigDto.setConfigId(reportFeeMonthStatisticsDto.getConfigId());
            feeConfigDto.setAmount(Double.parseDouble(reportFeeMonthStatisticsDto.getReceivedAmount()));
            feeConfigDtos.add(feeConfigDto);
            tmpReportFeeMonthStatisticsDto.setFeeConfigDtos(feeConfigDtos);

            //历史欠费
            hisOweAmountDec = new BigDecimal(tmpReportFeeMonthStatisticsDto.getHisOweAmount());
            hisOweAmountDec = hisOweAmountDec.add(new BigDecimal(reportFeeMonthStatisticsDto.getHisOweAmount()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setHisOweAmount(hisOweAmountDec.doubleValue());


            //当月应收
            curReceivableAmountDec = new BigDecimal(tmpReportFeeMonthStatisticsDto.getCurReceivableAmount());
            curReceivableAmountDec = curReceivableAmountDec.add(new BigDecimal(reportFeeMonthStatisticsDto.getCurReceivableAmount()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setCurReceivableAmount(curReceivableAmountDec.doubleValue());

            //当月实收
            curReceivedAmountDec = new BigDecimal(tmpReportFeeMonthStatisticsDto.getCurReceivedAmount());
            curReceivedAmountDec = curReceivedAmountDec.add(new BigDecimal(reportFeeMonthStatisticsDto.getCurReceivedAmount()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setCurReceivedAmount(curReceivedAmountDec.doubleValue());

            //欠费追回
            hisOweReceivedAmountDec = new BigDecimal(tmpReportFeeMonthStatisticsDto.getHisOweReceivedAmount());
            hisOweReceivedAmountDec = hisOweReceivedAmountDec.add(new BigDecimal(reportFeeMonthStatisticsDto.getHisOweReceivedAmount()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setHisOweReceivedAmount(hisOweReceivedAmountDec.doubleValue());

            //预交费
            preReceivedAmountDec = new BigDecimal(tmpReportFeeMonthStatisticsDto.getPreReceivedAmount());
            preReceivedAmountDec = preReceivedAmountDec.add(new BigDecimal(reportFeeMonthStatisticsDto.getPreReceivedAmount()))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setPreReceivedAmount(preReceivedAmountDec.doubleValue());

            //总费用
            receivableAmountDec = new BigDecimal(Double.parseDouble(tmpReportFeeMonthStatisticsDto.getReceivableAmount()));
            receivableAmountDec = receivableAmountDec.add(new BigDecimal(Double.parseDouble(reportFeeMonthStatisticsDto.getReceivableAmount())))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setReceivableAmount(receivableAmountDec.doubleValue() + "");

            //实收
            receivedAmountDec = new BigDecimal(Double.parseDouble(tmpReportFeeMonthStatisticsDto.getReceivedAmount()));
            receivedAmountDec = receivedAmountDec.add(new BigDecimal(Double.parseDouble(reportFeeMonthStatisticsDto.getReceivedAmount())))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpReportFeeMonthStatisticsDto.setReceivedAmount(receivedAmountDec.doubleValue() + "");
        }

        return reportFeeMonthStatisticsDtos;
    }

    private ReportFeeMonthStatisticsDto hasReportFeeMonthStatisticsDto(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos, ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto, String flag) {
        for (ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            if ("FeeSummary".equals(flag) && tmpReportFeeMonthStatisticsDto.getFeeYear().equals(reportFeeMonthStatisticsDto.getFeeYear())
                    && tmpReportFeeMonthStatisticsDto.getFeeMonth().equals(reportFeeMonthStatisticsDto.getFeeMonth())) {
                return tmpReportFeeMonthStatisticsDto;
            }
            if ("FloorUnitFeeSummary".equals(flag) && tmpReportFeeMonthStatisticsDto.getFeeYear().equals(reportFeeMonthStatisticsDto.getFeeYear())
                    && tmpReportFeeMonthStatisticsDto.getFeeMonth().equals(reportFeeMonthStatisticsDto.getFeeMonth())
                    && tmpReportFeeMonthStatisticsDto.getFloorNum().equals(reportFeeMonthStatisticsDto.getFloorNum())
                    && tmpReportFeeMonthStatisticsDto.getUnitNum().equals(reportFeeMonthStatisticsDto.getUnitNum())
            ) {
                return tmpReportFeeMonthStatisticsDto;
            }
        }

        return null;
    }

    @Override
    public ResponseEntity<String> queryReportFloorUnitFeeSummary(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeSummaryCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeSummary(reportFeeMonthStatisticsDto);
            if (reportFeeMonthStatisticsDto.getConfigIds() != null) {
                reportFeeMonthStatisticsDtos = dealConfigReportFeeMonthStatisticsList(reportFeeMonthStatisticsDtos, "FloorUnitFeeSummary");
            }
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeSummaryMajor(reportFeeMonthStatisticsDto);
            if (reportFeeMonthStatisticsDtos != null && reportFeeMonthStatisticsDtos.size() > 0) {
                for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 : reportFeeMonthStatisticsDtos) {
                    reportFeeMonthStatisticsDto1.setAllReceivableAmount(tmpReportFeeMonthStatisticsDto.getAllReceivableAmount());
                    reportFeeMonthStatisticsDto1.setAllReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllReceivedAmount());
                    reportFeeMonthStatisticsDto1.setAllOweAmount(tmpReportFeeMonthStatisticsDto.getAllOweAmount());
                    reportFeeMonthStatisticsDto1.setAllHisOweReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllHisOweReceivedAmount());
                }
            }
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryFeeBreakdown(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        if (StringUtil.isEmpty(reportFeeMonthStatisticsDto.getYearMonth())) {
            reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
            reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        }

        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdownCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdown(reportFeeMonthStatisticsDto);
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdownMajor(reportFeeMonthStatisticsDto);
            reportFeeMonthStatisticsDto.setPage(PageDto.DEFAULT_PAGE);
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatistics = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeBreakdown(reportFeeMonthStatisticsDto);
            BigDecimal allOweAmount = new BigDecimal(0.0);
            BigDecimal allHisOweReceivedAmount = new BigDecimal(0.0);
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistic : reportFeeMonthStatistics) {
                //获取历史欠费
                BigDecimal hisOweAmount = new BigDecimal(reportFeeMonthStatistic.getHisOweAmount());
                //获取当月应收
                BigDecimal curReceivableAmount = new BigDecimal(reportFeeMonthStatistic.getCurReceivableAmount());
                //获取当月实收
                BigDecimal curReceivedAmount = new BigDecimal(reportFeeMonthStatistic.getCurReceivedAmount());
                //获取欠债追回
                BigDecimal hisOweReceivedAmount = new BigDecimal(reportFeeMonthStatistic.getHisOweReceivedAmount());
                //计算欠费金额
                BigDecimal oweAmount = hisOweAmount.add(curReceivableAmount).subtract(curReceivedAmount).subtract(hisOweReceivedAmount);
                if (oweAmount.compareTo(BigDecimal.ZERO) == 1) { //欠费金额大于0
                    allOweAmount = allOweAmount.add(oweAmount);
                }
                if (hisOweReceivedAmount.compareTo(BigDecimal.ZERO) == 1) { //欠费追回大于0
                    allHisOweReceivedAmount = allHisOweReceivedAmount.add(hisOweReceivedAmount);
                }
            }
            if (reportFeeMonthStatisticsDtos != null && reportFeeMonthStatisticsDtos.size() > 0) {
                for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 : reportFeeMonthStatisticsDtos) {
                    reportFeeMonthStatisticsDto1.setAllReceivableAmount(tmpReportFeeMonthStatisticsDto.getAllReceivableAmount());
                    reportFeeMonthStatisticsDto1.setAllReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllReceivedAmount());
                    reportFeeMonthStatisticsDto1.setAllOweAmount(allOweAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    reportFeeMonthStatisticsDto1.setAllHisOweReceivedAmount(allHisOweReceivedAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {


        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");

        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetailCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetail(reportFeeMonthStatisticsDto);
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryAllFeeDetail(reportFeeMonthStatisticsDto);
            reportFeeMonthStatisticsDto.setPage(PageDto.DEFAULT_PAGE);
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatistics = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetail(reportFeeMonthStatisticsDto);
            BigDecimal allOweAmount = new BigDecimal(0.0);
            BigDecimal allHisOweReceivedAmount = new BigDecimal(0.0);
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistic : reportFeeMonthStatistics) {
                //获取历史欠费
                BigDecimal hisOweAmount = new BigDecimal(reportFeeMonthStatistic.getHisOweAmount());
                //获取当月应收
                BigDecimal curReceivableAmount = new BigDecimal(reportFeeMonthStatistic.getCurReceivableAmount());
                //获取当月实收
                BigDecimal curReceivedAmount = new BigDecimal(reportFeeMonthStatistic.getCurReceivedAmount());
                //获取欠费追回
                BigDecimal hisOweReceivedAmount = new BigDecimal(reportFeeMonthStatistic.getHisOweReceivedAmount());
                //计算欠费金额
                BigDecimal oweAmount = hisOweAmount.add(curReceivableAmount).subtract(curReceivedAmount).subtract(hisOweReceivedAmount);
                if (oweAmount.compareTo(BigDecimal.ZERO) == 1) { //欠费金额大于0
                    allOweAmount = allOweAmount.add(oweAmount);
                }
                if (hisOweReceivedAmount.compareTo(BigDecimal.ZERO) == 1) { //欠费追回大于0
                    allHisOweReceivedAmount = allHisOweReceivedAmount.add(hisOweReceivedAmount);
                }
            }
            if (reportFeeMonthStatisticsDtos != null && reportFeeMonthStatisticsDtos.size() > 0) {
                for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 : reportFeeMonthStatisticsDtos) {
                    reportFeeMonthStatisticsDto1.setAllReceivableAmount(reportFeeMonthStatisticsList.get(0).getAllReceivableAmount());
                    reportFeeMonthStatisticsDto1.setAllReceivedAmount(reportFeeMonthStatisticsList.get(0).getAllReceivedAmount());
                    reportFeeMonthStatisticsDto1.setAllOweAmount(allOweAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    reportFeeMonthStatisticsDto1.setAllHisOweReceivedAmount(allHisOweReceivedAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
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
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetailMajor(reportFeeMonthStatisticsDto);
            if (reportFeeMonthStatisticsDtos != null && reportFeeMonthStatisticsDtos.size() > 0) {
                for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 : reportFeeMonthStatisticsDtos) {
//                    reportFeeMonthStatisticsDto1.setAllReceivableAmount(tmpReportFeeMonthStatisticsDto.getAllReceivableAmount());
//                    reportFeeMonthStatisticsDto1.setAllReceivedAmount(tmpReportFeeMonthStatisticsDto.getAllReceivedAmount());
                    reportFeeMonthStatisticsDto1.setAllOweAmount(tmpReportFeeMonthStatisticsDto.getOweAmount());
                }
            }
            freshReportOweDay(reportFeeMonthStatisticsDtos);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryPayFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {

        JSONObject countInfo = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetailCount(reportFeeMonthStatisticsDto);

        int count = Integer.parseInt(countInfo.get("count").toString());

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        ReportFeeMonthStatisticsTotalDto reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        List<ReportFeeMonthStatisticsDto> reportList = new ArrayList<>();
        //查询该小区下的费用项目
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportFeeMonthStatisticsDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        //应收总金额(大计)
        Double allReceivableAmount = 0.0;
        //实收金额(大计)
        Double allReceivedAmount = 0.0;
        //优惠金额(大计)
        Double allPreferentialAmount = 0.0;
        //减免金额(大计)
        Double allDeductionAmount = 0.0;
        //滞纳金(大计)
        Double allLateFee = 0.0;
        //空置房打折(大计)
        Double allVacantHousingDiscount = 0.0;
        //空置房减免(大计)
        Double allVacantHousingReduction = 0.0;
        //赠送金额(大计)
        Double allGiftAmount = 0.0;
        //吴学文 注释 感觉和上面的369 功能重复
        //int size = 0;
        if (count > 0) {
            //查询缴费明细
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetail(reportFeeMonthStatisticsDto);
            //吴学文 注释 感觉和上面的369 功能重复
//            if (reportFeeMonthStatisticsDtos != null && reportFeeMonthStatisticsDtos.size() > 0) {
//                //查询所有缴费明细记录
//                ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto1 = BeanConvertUtil.covertBean(reportFeeMonthStatisticsDto, ReportFeeMonthStatisticsDto.class);
//                reportFeeMonthStatisticsDto1.setPage(PageDto.DEFAULT_PAGE);
//                List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos1 = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetail(reportFeeMonthStatisticsDto1);
//                size = reportFeeMonthStatisticsDtos1.size();
//            }
            //查询应收、实收总金额(大计)
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryAllPayFeeDetail(reportFeeMonthStatisticsDto);
            //查询(优惠、减免、滞纳金、空置房打折、空置房减免金额等)大计总金额
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsSum = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetailSum(reportFeeMonthStatisticsDto);
            allReceivableAmount = Double.valueOf(reportFeeMonthStatisticsList.get(0).getAllReceivableAmount());
            allReceivedAmount = Double.valueOf(reportFeeMonthStatisticsList.get(0).getAllReceivedAmount());
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsSum) {
                //这里是查询出的金额总和
                String discountPrice = reportFeeMonthStatistics.getDiscountPrice();
                //优惠金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("1")) {
                    allPreferentialAmount = Double.valueOf(discountPrice);
                }
                //减免金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("2")) {
                    allDeductionAmount = Double.valueOf(discountPrice);
                }
                //滞纳金(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("3")) {
                    allLateFee = Double.valueOf(discountPrice);
                }
                //空置房打折金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("4")) {
                    allVacantHousingDiscount = Double.valueOf(discountPrice);
                }
                //空置房减免金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("5")) {
                    allVacantHousingReduction = Double.valueOf(discountPrice);
                }
                //赠送金额(大计)
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("6")) {
                    allGiftAmount = Double.valueOf(discountPrice);
                }
            }
            //应收总金额(小计)
            Double totalReceivableAmount = 0.0;
            //实收总金额(小计)
            Double totalReceivedAmount = 0.0;
            //优惠金额(小计)
            Double totalPreferentialAmount = 0.0;
            //减免金额(小计)
            Double totalDeductionAmount = 0.0;
            //空置房打折金额(小计)
            Double totalVacantHousingDiscount = 0.0;
            //空置房减免金额(小计)
            Double totalVacantHousingReduction = 0.0;
            //赠送金额(小计)
            Double totalGiftAmount = 0.0;
            //滞纳金(小计)
            Double totalLateFee = 0.0;
            List<String> ownerIds = new ArrayList<>();
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsDtos) {
                FeeDetailDto feeDetailDto = new FeeDetailDto();
                feeDetailDto.setDetailId(reportFeeMonthStatistics.getDetailId());
                List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
                Assert.listOnlyOne(feeDetailDtos, "查询费用明细表错误");
                reportFeeMonthStatistics.setReceivableAmount(feeDetailDtos.get(0).getReceivableAmount());
                reportFeeMonthStatistics.setPayableAmount(feeDetailDtos.get(0).getPayableAmount());
                //应收金额
                Double receivableAmount = Double.valueOf(reportFeeMonthStatistics.getReceivableAmount());
                //实收金额
                Double receivedAmount = Double.valueOf(reportFeeMonthStatistics.getReceivedAmount());
                totalReceivableAmount = totalReceivableAmount + receivableAmount;
                totalReceivedAmount = totalReceivedAmount + receivedAmount;

                if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    ownerIds.add(reportFeeMonthStatistics.getOwnerId());
                }

                // 最大记录时 就去刷新
                //如果是车位刷房屋信息
                if (ownerIds.size() == MAX_ROWS) {
                    refreshReportFeeMonthStatistics(ownerIds, reportFeeMonthStatisticsDtos);
                    ownerIds = new ArrayList<>();
                }

                //优惠金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("1")) {
                    //获取优惠金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalPreferentialAmount = totalPreferentialAmount + discountPrice;
                    //优惠金额
                    reportFeeMonthStatistics.setPreferentialAmount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setPreferentialAmount("0");
                }
                //减免金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("2")) {
                    //获取减免金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalDeductionAmount = totalDeductionAmount + discountPrice;
                    //减免金额
                    reportFeeMonthStatistics.setDeductionAmount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setDeductionAmount("0");
                }
                //滞纳金
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("3")) {
                    //获取滞纳金金额
                    Double discountPrice = (Double.valueOf(reportFeeMonthStatistics.getDiscountPrice()));
                    totalLateFee = totalLateFee + discountPrice;
                    //滞纳金
                    reportFeeMonthStatistics.setLateFee(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setLateFee("0");
                }
                //空置房打折
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("4")) {
                    //空置房打折金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalVacantHousingDiscount = totalVacantHousingDiscount + discountPrice;
                    //空置房打折
                    reportFeeMonthStatistics.setVacantHousingDiscount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setVacantHousingDiscount("0");
                }
                //空置房减免
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("5")) {
                    //空置房减免金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalVacantHousingReduction = totalVacantHousingReduction + discountPrice;
                    //空置房减免
                    reportFeeMonthStatistics.setVacantHousingReduction(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setVacantHousingReduction("0");
                }
                //赠送金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("6")) {
                    //赠送金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalGiftAmount = totalGiftAmount + discountPrice;
                    //赠送金额
                    reportFeeMonthStatistics.setGiftAmount(reportFeeMonthStatistics.getDiscountPrice());
                } else {
                    reportFeeMonthStatistics.setGiftAmount("0");
                }
                if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getFloorNum()
                            + "栋" + reportFeeMonthStatistics.getUnitNum()
                            + "单元" + reportFeeMonthStatistics.getRoomNum() + "室");
                } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getCarNum());
                } else {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getContractCode());

                }
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getImportFeeName())) {
                    reportFeeMonthStatistics.setFeeName(reportFeeMonthStatistics.getImportFeeName());
                }
                //费用项目
                reportFeeMonthStatistics.setFeeConfigDtos(feeConfigDtos);
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getRepairId())) {
                    RepairDto repairDto = new RepairDto();
                    repairDto.setRepairId(reportFeeMonthStatistics.getRepairId());
                    //查询报修单
                    List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
                    //Assert.listOnlyOne(repairDtos, "查询报修单错误！");
                    if (repairDtos != null && repairDtos.size() == 1) {
                        if (!StringUtil.isEmpty(repairDtos.get(0).getRepairObjType()) && repairDtos.get(0).getRepairObjType().equals("004")) {
                            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                            ownerRoomRelDto.setRoomId(repairDtos.get(0).getRepairObjId());
                            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
                            if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() == 0) { //查询条数为0条
                                OwnerRoomRelDto ownerRoomRel = new OwnerRoomRelDto();
                                ownerRoomRel.setRoomId(repairDtos.get(0).getRepairObjId());
                                ownerRoomRel.setStatusCd("1"); //看看业主房屋关系数据是否删除了
                                List<OwnerRoomRelDto> ownerRoomRels = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRel);
                                Assert.listOnlyOne(ownerRoomRels, "查询业主房屋关系表错误！");
                                OwnerDto owner = new OwnerDto();
                                owner.setOwnerId(ownerRoomRels.get(0).getOwnerId());
                                owner.setOwnerTypeCd("1001"); //业主本人
                                List<OwnerDto> owners = ownerInnerServiceSMOImpl.queryOwners(owner);
                                if (owners != null && owners.size() == 0) { //查出条数为0条
                                    //判断业主是否删除了
                                    OwnerDto newOwner = new OwnerDto();
                                    newOwner.setOwnerId(ownerRoomRels.get(0).getOwnerId());
                                    newOwner.setOwnerTypeCd("1001"); //业主本人
                                    newOwner.setStatusCd("1");
                                    List<OwnerDto> newOwners = ownerInnerServiceSMOImpl.queryOwners(newOwner);
                                    Assert.listOnlyOne(newOwners, "查询业主信息错误！");
                                    reportFeeMonthStatistics.setOwnerName(newOwners.get(0).getName());
                                } else if (owners != null && owners.size() == 1) { //查出条数为1条
                                    reportFeeMonthStatistics.setOwnerName(owners.get(0).getName());
                                } else {
                                    throw new IllegalArgumentException("查询业主信息错误！");
                                }
                            } else if (ownerRoomRelDtos != null && ownerRoomRelDtos.size() == 1) { //查询条数为1条
                                OwnerDto ownerDto = new OwnerDto();
                                ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
                                ownerDto.setOwnerTypeCd("1001"); //业主本人
                                List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
                                if (ownerDtos != null && ownerDtos.size() == 0) { //业主查询条数为0条
                                    //判断业主是否删除了
                                    OwnerDto newOwner = new OwnerDto();
                                    newOwner.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
                                    newOwner.setOwnerTypeCd("1001"); //业主本人
                                    newOwner.setStatusCd("1");
                                    List<OwnerDto> newOwners = ownerInnerServiceSMOImpl.queryOwners(newOwner);
                                    Assert.listOnlyOne(newOwners, "查询业主信息错误！");
                                    reportFeeMonthStatistics.setOwnerName(newOwners.get(0).getName());
                                } else if (ownerDtos != null || ownerDtos.size() == 1) {
                                    reportFeeMonthStatistics.setOwnerName(ownerDtos.get(0).getName());
                                } else {
                                    throw new IllegalArgumentException("查询业主信息错误！");
                                }
                            } else {
                                throw new IllegalArgumentException("查询业主房屋关系表错误！");
                            }
                        }
                    }
                }
                if (!hasInReportListAndMerge(reportList, reportFeeMonthStatistics)) {
                    reportList.add(reportFeeMonthStatistics);
                }
            }

            //如果是车位刷房屋信息
            if (ownerIds.size() > 0) {
                refreshReportFeeMonthStatistics(ownerIds, reportFeeMonthStatisticsDtos);
            }

            //应收总金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount));
            //实收金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount));
            //优惠金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalPreferentialAmount(String.format("%.2f", totalPreferentialAmount));
            //减免金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalDeductionAmount(String.format("%.2f", totalDeductionAmount));
            //滞纳金(小计)
            reportFeeMonthStatisticsTotalDto.setTotalLateFee(String.format("%.2f", totalLateFee));
            //空置房打折(小计)
            reportFeeMonthStatisticsTotalDto.setTotalVacantHousingDiscount(String.format("%.2f", totalVacantHousingDiscount));
            //空置房减免(小计)
            reportFeeMonthStatisticsTotalDto.setTotalVacantHousingReduction(String.format("%.2f", totalVacantHousingReduction));
            //赠送规则金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalGiftAmount(String.format("%.2f", totalGiftAmount));
            //应收金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllReceivableAmount(String.format("%.2f", allReceivableAmount));
            //实收金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllReceivedAmount(String.format("%.2f", allReceivedAmount));
            //优惠金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllPreferentialAmount(String.format("%.2f", allPreferentialAmount));
            //减免金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllDeductionAmount(String.format("%.2f", allDeductionAmount));
            //滞纳金(大计)
            reportFeeMonthStatisticsTotalDto.setAllLateFee(String.format("%.2f", allLateFee));
            //空置房打折金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllVacantHousingDiscount(String.format("%.2f", allVacantHousingDiscount));
            //空置房减免金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllVacantHousingReduction(String.format("%.2f", allVacantHousingReduction));
            //赠送规则金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllGiftAmount(String.format("%.2f", allGiftAmount));
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
            reportList.addAll(reportFeeMonthStatisticsDtos);
            reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportList, reportFeeMonthStatisticsTotalDto);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * @param ownerIds
     * @param reportFeeMonthStatisticsDtos
     */
    private void refreshReportFeeMonthStatistics(List<String> ownerIds, List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {

        if (ownerIds == null || ownerIds.size() < 1) {
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        List<OwnerDto> ownerDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRoomAndParkingSpace(ownerDto);
        String objName = "";
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(reportFeeMonthStatisticsDto.getPayerObjType())) {
                continue;
            }
            for (OwnerDto ownerDto1 : ownerDtos) {
                if (!StringUtil.isEmpty(reportFeeMonthStatisticsDto.getOwnerId()) && !reportFeeMonthStatisticsDto.getOwnerId().equals(ownerDto1.getOwnerId())) {
                    continue;
                }
                objName = reportFeeMonthStatisticsDto.getObjName() + "(" + ownerDto1.getFloorNum() + "栋" + ownerDto1.getUnitNum() + "单元" + ownerDto1.getRoomNum() + "室)";
                reportFeeMonthStatisticsDto.setObjName(objName);
            }
        }
    }

    private boolean hasInReportListAndMerge(List<ReportFeeMonthStatisticsDto> reportList, ReportFeeMonthStatisticsDto reportFeeMonthStatistics) {
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportList) {
            if (reportFeeMonthStatisticsDto.getDetailId().equals(reportFeeMonthStatistics.getDetailId())) {
                combineSydwCore(reportFeeMonthStatistics, reportFeeMonthStatisticsDto);
                return true;
            }
        }
        return false;
    }

    //针对所用对象
    private static ReportFeeMonthStatisticsDto combineSydwCore(ReportFeeMonthStatisticsDto sourceBean, ReportFeeMonthStatisticsDto targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }

    @Override
    public ResponseEntity<String> queryDeadlineFee(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryDeadlineFeeCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryDeadlineFee(reportFeeMonthStatisticsDto);

            freshReportDeadlineDay(reportFeeMonthStatisticsDtos);
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
        List<ReportFeeMonthStatisticsDto> reportAllFeeMonthStatisticsDtos = null;

        reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOwePaymentCount(reportFeeMonthStatisticsDto);

        reportAllFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryAllPaymentCount(reportFeeMonthStatisticsDto);
        int normalFee = 0;
        for (ReportFeeMonthStatisticsDto aReportFeeMonthStatisticsDto : reportAllFeeMonthStatisticsDtos) {
            for (ReportFeeMonthStatisticsDto oweReportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
                String objCount = aReportFeeMonthStatisticsDto.getObjCount();
                if (aReportFeeMonthStatisticsDto.getFeeName().equals(oweReportFeeMonthStatisticsDto.getFeeName())) {
                    aReportFeeMonthStatisticsDto.setObjCount(oweReportFeeMonthStatisticsDto.getObjCount());
                    normalFee = Integer.parseInt(objCount) - Integer.parseInt(oweReportFeeMonthStatisticsDto.getObjCount());
                    aReportFeeMonthStatisticsDto.setNormalCount(normalFee + "");
                }
            }
        }


        ResultVo resultVo = new ResultVo(reportAllFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 查询报表专家 统计信息
     *
     * @param reportFeeMonthStatisticsDto
     * @return
     */
    @Override
    public ResponseEntity<String> queryReportProficientCount(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        reportFeeMonthStatisticsDto.setFeeYear(DateUtil.getYear() + "");
        reportFeeMonthStatisticsDto.setFeeMonth(DateUtil.getMonth() + "");
        JSONObject result = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportProficientCount(reportFeeMonthStatisticsDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(result.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 查询报修信息
     *
     * @param repairUserDto
     * @return
     */
    @Override
    public ResponseEntity<String> queryRepair(RepairUserDto repairUserDto) {
        //查询员工报修表员工信息
        List<RepairUserDto> repairUsers = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairForStaff(repairUserDto);
        int count = repairUsers.size();
        //获取员工id和姓名集合
        List<RepairUserDto> staffs;
        if (StringUtil.isEmpty(repairUserDto.getStaffId())) {
            repairUserDto.setPage(-1);
            staffs = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairForStaff(repairUserDto);
        } else {
            repairUserDto.setPage(-1);
            repairUserDto.setStaffId(null);
            staffs = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairForStaff(repairUserDto);
        }
        List<RepairUserDto> repairUserList = new ArrayList<>();
        //处理中总数量
        int dealNumber = 0;
        //结单总数量
        int statementNumber = 0;
        //退单总数量
        int chargebackNumber = 0;
        //转单总数量
        int transferOrderNumber = 0;
        //派单总数量
        int dispatchNumber = 0;
        //已回访总数量
        int returnNumber = 0;
        if (count > 0) {
            for (RepairUserDto repairUser : repairUsers) {
                RepairUserDto repairUserInfo = new RepairUserDto();
                //员工id
                repairUserDto.setStaffId(repairUser.getStaffId());
                List<RepairUserDto> repairUserDtoList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryRepairWithOutPage(repairUserDto);
                if (repairUserDtoList != null && repairUserDtoList.size() > 0) {
                    //处理中数量
                    int dealAmount = 0;
                    //结单数量
                    int statementAmount = 0;
                    //退单数量
                    int chargebackAmount = 0;
                    //转单数量
                    int transferOrderAmount = 0;
                    //派单数量
                    int dispatchAmount = 0;
                    //回访数量
                    int returnAmount = 0;
                    //评分
                    String score = "";
                    for (RepairUserDto repair : repairUserDtoList) {
                        //处理中状态
                        if (repair.getState().equals("10001")) {
                            //获取数量
                            int amount = Integer.parseInt(repair.getAmount());
                            dealAmount = dealAmount + amount;
                        } else if (repair.getState().equals("10002")) {  //结单状态
                            //获取数量
                            int amount = Integer.parseInt(repair.getAmount());
                            statementAmount = statementAmount + amount;
                        } else if (repair.getState().equals("10003")) {  //退单状态
                            //获取数量
                            int amount = Integer.parseInt(repair.getAmount());
                            chargebackAmount = chargebackAmount + amount;
                        } else if (repair.getState().equals("10004")) {  //转单状态
                            //获取数量
                            int amount = Integer.parseInt(repair.getAmount());
                            transferOrderAmount = transferOrderAmount + amount;
                        } else if (repair.getState().equals("10006")) {  //派单状态
                            int amount = Integer.parseInt(repair.getAmount());
                            dispatchAmount = dispatchAmount + amount;
                        } else if (repair.getState().equals("10008")) {  //已回访状态
                            int amount = Integer.parseInt(repair.getAmount());
                            returnAmount = returnAmount + amount;
                        }
                        if (!StringUtil.isEmpty(repair.getScore())) {
                            score = repair.getScore();
                        }
                    }
                    //员工id
                    repairUserInfo.setStaffId(repairUser.getStaffId());
                    //员工姓名
                    repairUserInfo.setStaffName(repairUser.getStaffName());
                    //处理中报修数量
                    repairUserInfo.setDealAmount(Integer.toString(dealAmount));
                    //处理中报修总数量
                    repairUserInfo.setDealNumber(Integer.toString(dealNumber));
                    //结单报修数量
                    repairUserInfo.setStatementAmount(Integer.toString(statementAmount));
                    //结单报修总数量
                    repairUserInfo.setStatementNumber(Integer.toString(statementNumber));
                    //退单报修数量
                    repairUserInfo.setChargebackAmount(Integer.toString(chargebackAmount));
                    //退单报修总数量
                    repairUserInfo.setChargebackNumber(Integer.toString(chargebackNumber));
                    //转单报修数量
                    repairUserInfo.setTransferOrderAmount(Integer.toString(transferOrderAmount));
                    //转单报修总数量
                    repairUserInfo.setTransferOrderNumber(Integer.toString(transferOrderNumber));
                    //派单报修数量
                    repairUserInfo.setDispatchAmount(Integer.toString(dispatchAmount));
                    //派单报修总数量
                    repairUserInfo.setDispatchNumber(Integer.toString(dispatchNumber));
                    //回访数量
                    repairUserInfo.setReturnAmount(Integer.toString(returnAmount));
                    //回访总数量
                    repairUserInfo.setReturnNumber(Integer.toString(returnNumber));
                    //员工id和姓名信息集合
                    repairUserInfo.setRepairList(staffs);
                    //员工评分
                    repairUserInfo.setScore(score);
                    repairUserList.add(repairUserInfo);
                } else {
                    continue;
                }
                dealNumber = Integer.parseInt(repairUserInfo.getDealAmount()) + dealNumber;
                statementNumber = Integer.parseInt(repairUserInfo.getStatementAmount()) + statementNumber;
                chargebackNumber = Integer.parseInt(repairUserInfo.getChargebackAmount()) + chargebackNumber;
                transferOrderNumber = Integer.parseInt(repairUserInfo.getTransferOrderAmount()) + transferOrderNumber;
                dispatchNumber = Integer.parseInt(repairUserInfo.getDispatchAmount()) + dispatchNumber;
                returnNumber = Integer.parseInt(repairUserInfo.getReturnAmount()) + returnNumber;
            }
        } else {
            repairUserList = new ArrayList<>();
        }

        RepairUserDto repairUser = new RepairUserDto();
        repairUser.setDealNumber(Integer.toString(dealNumber));
        repairUser.setStatementNumber(Integer.toString(statementNumber));
        repairUser.setChargebackNumber(Integer.toString(chargebackNumber));
        repairUser.setTransferOrderNumber(Integer.toString(transferOrderNumber));
        repairUser.setDispatchNumber(Integer.toString(dispatchNumber));
        repairUser.setReturnNumber(Integer.toString(returnNumber));

        //获取总条数
        int size = staffs.size();

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) size / (double) repairUserDto.getRow()), repairUserList.size(), repairUserList, staffs, repairUser);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryNoFeeRooms(RoomDto roomDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryNoFeeRoomsCount(roomDto);

        List<RoomDto> roomDtos = null;
        if (count > 0) {
            roomDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryNoFeeRooms(roomDto);
        } else {
            roomDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomDto.getRow()), count, roomDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryPayFeeDeposit(ReportDeposit reportDeposit) {
        //查询押金
        List<ReportDeposit> reportDeposits = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDeposit(reportDeposit);
        //查询押金退费总金额
        List<ReportDeposit> reportDepositAmounts = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDepositAmount(reportDeposit);

        //查询押金费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportDeposit.getCommunityId());
        feeConfigDto.setFeeTypeCd("888800010006");
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        List<ReportDeposit> newReportDeposits = new ArrayList<>();
        BigDecimal unpaidfeeAmount = new BigDecimal(0);//未交费
        BigDecimal unpaidfeeAmounts = new BigDecimal(0);//未缴费总金额
        BigDecimal paidfeeAmount = new BigDecimal(0);//已缴费
        BigDecimal paidfeeAmounts = new BigDecimal(0);//已缴费总金额
        BigDecimal refundedAmount = new BigDecimal(0);//已退费
        BigDecimal refundedAmounts = new BigDecimal(0);//已退费总金额
        BigDecimal refundInProgressAmount = new BigDecimal(0); //退费中
        BigDecimal refundInProgressAmounts = new BigDecimal(0);//退费中总金额
        BigDecimal refundFailedAmount = new BigDecimal(0); //退费失败
        BigDecimal refundFailedAmounts = new BigDecimal(0);//退费失败总金额
        for (ReportDeposit deposit : reportDeposits) {
            deposit.setFeeConfigDtos(feeConfigDtos);
            newReportDeposits.add(deposit);
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(deposit.getPayerObjType())) {
                deposit.setObjName(deposit.getFloorNum()
                        + "栋" + deposit.getUnitNum()
                        + "单元" + deposit.getRoomNum() + "室");
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(deposit.getPayerObjType())) {
                deposit.setObjName(deposit.getCarNum());
            }
            //收费中（未交费）
            if ("2008001".equals(deposit.getState())) {
                unpaidfeeAmount = unpaidfeeAmount.add(new BigDecimal(deposit.getAdditionalAmount()));
            }
            //收费结束（已收费）
            if ("2009001".equals(deposit.getState()) && !StringUtil.isEmpty(deposit.getDetailState()) && "1400".equals(deposit.getDetailState())) {
                paidfeeAmount = paidfeeAmount.add(new BigDecimal(deposit.getAdditionalAmount()));
            }
            if (!StringUtil.isEmpty(deposit.getDetailState()) && "1100".equals(deposit.getDetailState())) {//已退费
                refundedAmount = refundedAmount.add(new BigDecimal(deposit.getAdditionalAmount()));
            }
            if (!StringUtil.isEmpty(deposit.getDetailState()) && "1000".equals(deposit.getDetailState())) {//退费中
                refundInProgressAmount = refundInProgressAmount.add(new BigDecimal(deposit.getAdditionalAmount()));
            }
            if (!StringUtil.isEmpty(deposit.getDetailState()) && "1200".equals(deposit.getDetailState())) {//退费失败
                refundFailedAmount = refundFailedAmount.add(new BigDecimal(deposit.getAdditionalAmount()));
            }
        }
        for (ReportDeposit reportDeposit1 : reportDepositAmounts) {
            if (StringUtil.isEmpty(reportDeposit1.getAllAmount())) {
                throw new IllegalArgumentException("查询总金额错误！");
            }
            //获取总金额
            BigDecimal bd = new BigDecimal(reportDeposit1.getAllAmount());
            if (StringUtil.isEmpty(reportDeposit1.getDetailState())) { //获取未缴费总金额
                unpaidfeeAmounts = bd;
            } else if (reportDeposit1.getDetailState().equals("1000")) { //获取退费中总金额
                refundInProgressAmounts = bd;
            } else if (reportDeposit1.getDetailState().equals("1100")) { //获取已退费总金额
                refundedAmounts = bd;
            } else if (reportDeposit1.getDetailState().equals("1200")) { //获取退费失败总金额
                refundFailedAmounts = bd;
            } else if (reportDeposit1.getDetailState().equals("1400")) { //获取已缴费总金额
                paidfeeAmounts = bd;
            }
        }
        HashMap<String, String> mp = new HashMap<>();
        mp.put("unpaidfeeAmount", unpaidfeeAmount.toString());
        mp.put("paidfeeAmount", paidfeeAmount.toString());
        mp.put("refundedAmount", refundedAmount.toString());
        mp.put("refundInProgressAmount", refundInProgressAmount.toString());
        mp.put("refundFailedAmount", refundFailedAmount.toString());
        mp.put("unpaidfeeAmounts", unpaidfeeAmounts.toString());
        mp.put("paidfeeAmounts", paidfeeAmounts.toString());
        mp.put("refundedAmounts", refundedAmounts.toString());
        mp.put("refundInProgressAmounts", refundInProgressAmounts.toString());
        mp.put("refundFailedAmounts", refundFailedAmounts.toString());
        int size = 0;
        if (newReportDeposits != null && newReportDeposits.size() > 0) {
            //查询所有条数
            reportDeposit.setPage(PageDto.DEFAULT_PAGE);
            List<ReportDeposit> reportDeposits1 = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDeposit(reportDeposit);
            size = reportDeposits1.size();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) size / (double) reportDeposit.getRow()), size, newReportDeposits, mp);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryHuaningOweFee(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningOweFeeCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningOweFee(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryHuaningPayFee(Map paramInfo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, (int) paramInfo.get("year"));
        calendar.set(Calendar.MONTH, (int) paramInfo.get("month") - 1);
        paramInfo.put("yearMonth", DateUtil.getFormatTimeString(calendar.getTime(), "YYYY-MM"));
        calendar.add(Calendar.MONTH, 1);
        paramInfo.put("nextYear", calendar.get(Calendar.YEAR));
        paramInfo.put("nextMonth", calendar.get(Calendar.MONTH) + 1);
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningPayFeeCount(paramInfo);

        List<Map> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningPayFee(paramInfo);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (int) paramInfo.get("row")), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryHuaningPayFeeTwo(Map paramInfo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, (int) paramInfo.get("year"));
        calendar.set(Calendar.MONTH, (int) paramInfo.get("month") - 1);
        paramInfo.put("yearMonth", DateUtil.getFormatTimeString(calendar.getTime(), "YYYY-MM"));
        calendar.add(Calendar.MONTH, 1);
        paramInfo.put("nextYear", calendar.get(Calendar.YEAR));
        paramInfo.put("nextMonth", calendar.get(Calendar.MONTH) + 1);
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningPayFeeTwoCount(paramInfo);

        List<Map> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningPayFeeTwo(paramInfo);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (int) paramInfo.get("row")), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryHuaningOweFeeDetail(Map paramInfo) {
        Calendar calendar = Calendar.getInstance();
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningOweFeeDetailCount(paramInfo);
        List<Map> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryHuaningOweFeeDetail(paramInfo);
            refreshOweFee(reportFeeMonthStatisticsDtos, paramInfo);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (int) paramInfo.get("row")), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void refreshOweFee(List<Map> reportFeeMonthStatisticsDtos, Map paramInfo) {
        Date startTime = null;
        Date endTime = null;
        Calendar calendar = Calendar.getInstance();
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        calendar.set(Calendar.MONTH, 0);
        Date curStart = calendar.getTime();


        for (Map paramIn : reportFeeMonthStatisticsDtos) {

            startTime = (Date) paramIn.get("startTime");
            endTime = (Date) paramIn.get("endTime");
            BigDecimal money = (BigDecimal) paramIn.get("oweAmount");
            double month = Math.ceil(computeFeeSMOImpl.dayCompare(startTime, endTime));
            if (month < 1) {
                paramIn.put("btAmount", 0);
                paramIn.put("bfAmount", 0);
                continue;
            }

            //每月金额
            BigDecimal monthAmount = money.divide(new BigDecimal(month), 2, BigDecimal.ROUND_HALF_EVEN);

            if (startTime.getTime() < curStart.getTime()) {
                BigDecimal btAmountDec = monthAmount.multiply(new BigDecimal(curMonth)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                paramIn.put("btAmount", btAmountDec.doubleValue());
                double preMonth = Math.ceil(computeFeeSMOImpl.dayCompare(startTime, curStart));
                BigDecimal bfAmountDec = monthAmount.multiply(new BigDecimal(preMonth)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                paramIn.put("bfAmount", bfAmountDec.doubleValue());
                continue;
            }

            if (startTime.getTime() >= curStart.getTime()) {
                paramIn.put("btAmount", money.doubleValue());
                paramIn.put("bfAmount", 0);
            }
        }

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
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int day = DateUtil.daysBetween(DateUtil.getDateFromString(format.format(new Date()), DateUtil.DATE_FORMATE_STRING_A), DateUtil.getDateFromString(reportFeeMonthStatisticsDto.getFeeCreateTime(),
                        DateUtil.DATE_FORMATE_STRING_A));
                reportFeeMonthStatisticsDto.setOweDay(day);
            } catch (Exception e) {
                logger.error("计算欠费天数失败", e);
            }

        }
    }


    private void freshReportDeadlineDay(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {

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
