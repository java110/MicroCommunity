package com.java110.report.bmo.reportFeeMonthStatistics.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsTotalDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IGetReportFeeMonthStatisticsBMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
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

        //查询该小区下的费用项目
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportFeeMonthStatisticsDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        List<ReportFeeMonthStatisticsDto> reportList = new ArrayList<>();
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsDtos) {
            reportFeeMonthStatistics.setFeeConfigDtos(feeConfigDtos);
            reportList.add(reportFeeMonthStatistics);
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportList);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryFeeDetail(ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto) {
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetailCount(reportFeeMonthStatisticsDto);

        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        //应收总金额(大计)
        Double allReceivableAmount = 0.0;
        //实收金额(大计)
        Double allReceivedAmount = 0.0;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeDetail(reportFeeMonthStatisticsDto);
            List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryAllFeeDetail(reportFeeMonthStatisticsDto);
            allReceivableAmount = Double.valueOf(reportFeeMonthStatisticsList.get(0).getAllReceivableAmount());
            allReceivedAmount = Double.valueOf(reportFeeMonthStatisticsList.get(0).getAllReceivedAmount());
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        //应收总金额(小计)
        Double totalReceivableAmount = 0.0;
        //实收总金额(小计)
        Double totalReceivedAmount = 0.0;
        List<ReportFeeMonthStatisticsDto> reportList = new ArrayList<>();
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsDtos) {
            //应收金额
            Double receivableAmount = Double.valueOf(reportFeeMonthStatistics.getReceivableAmount());
            //实收金额
            Double receivedAmount = Double.valueOf(reportFeeMonthStatistics.getReceivedAmount());
            totalReceivableAmount = totalReceivableAmount + receivableAmount;
            totalReceivedAmount = totalReceivedAmount + receivedAmount;
        }

        //查询该小区下的费用项目
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportFeeMonthStatisticsDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsDtos) {
            reportFeeMonthStatistics.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount));
            reportFeeMonthStatistics.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount));
            reportFeeMonthStatistics.setAllReceivableAmount(String.format("%.2f", allReceivableAmount));
            reportFeeMonthStatistics.setAllReceivedAmount(String.format("%.2f", allReceivedAmount));
            reportFeeMonthStatistics.setFeeConfigDtos(feeConfigDtos);
            reportList.add(reportFeeMonthStatistics);
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportList);

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
        if (count > 0) {
            //查询缴费明细
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDetail(reportFeeMonthStatisticsDto);
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
            //滞纳金(小计)
            Double totalLateFee = 0.0;
            for (ReportFeeMonthStatisticsDto reportFeeMonthStatistics : reportFeeMonthStatisticsDtos) {
                //应收金额
                Double receivableAmount = Double.valueOf(reportFeeMonthStatistics.getReceivableAmount());
                //实收金额
                Double receivedAmount = Double.valueOf(reportFeeMonthStatistics.getReceivedAmount());
                totalReceivableAmount = totalReceivableAmount + receivableAmount;
                totalReceivedAmount = totalReceivedAmount + receivedAmount;
                //优惠金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("1")) {
                    //获取优惠金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalPreferentialAmount = totalPreferentialAmount + discountPrice;
                    //优惠金额
                    reportFeeMonthStatistics.setPreferentialAmount(reportFeeMonthStatistics.getDiscountPrice());
                }
                //减免金额
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("2")) {
                    //获取减免金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalDeductionAmount = totalDeductionAmount + discountPrice;
                    //减免金额
                    reportFeeMonthStatistics.setDeductionAmount(reportFeeMonthStatistics.getDiscountPrice());
                }
                //滞纳金
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("3")) {
                    //获取滞纳金金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalLateFee = totalLateFee + discountPrice;
                    //滞纳金
                    reportFeeMonthStatistics.setLateFee(reportFeeMonthStatistics.getDiscountPrice());
                }
                //空置房打折
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("4")) {
                    //空置房打折金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalVacantHousingDiscount = totalVacantHousingDiscount + discountPrice;
                    //空置房打折
                    reportFeeMonthStatistics.setVacantHousingDiscount(reportFeeMonthStatistics.getDiscountPrice());
                }
                //空置房减免
                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getDiscountSmallType()) && reportFeeMonthStatistics.getDiscountSmallType().equals("5")) {
                    //空置房减免金额
                    Double discountPrice = Double.valueOf(reportFeeMonthStatistics.getDiscountPrice());
                    totalVacantHousingReduction = totalVacantHousingReduction + discountPrice;
                    //空置房减免
                    reportFeeMonthStatistics.setVacantHousingReduction(reportFeeMonthStatistics.getDiscountPrice());
                }

                if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reportFeeMonthStatistics.getPayerObjType())) {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getFloorNum()
                            + "栋" + reportFeeMonthStatistics.getUnitNum()
                            + "单元" + reportFeeMonthStatistics.getRoomNum() + "室");
                } else {
                    reportFeeMonthStatistics.setObjName(reportFeeMonthStatistics.getCarNum());
                }

                if (!StringUtil.isEmpty(reportFeeMonthStatistics.getImportFeeName())) {
                    reportFeeMonthStatistics.setFeeName(reportFeeMonthStatistics.getImportFeeName());
                }

                //费用项目
                reportFeeMonthStatistics.setFeeConfigDtos(feeConfigDtos);
                reportList.add(reportFeeMonthStatistics);
            }
            //应收总金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalReceivableAmount(String.format("%.2f", totalReceivableAmount));
            //实收金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalReceivedAmount(String.format("%.2f", totalReceivedAmount));
            //优惠金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalPreferentialAmount(String.valueOf(totalPreferentialAmount));
            //减免金额(小计)
            reportFeeMonthStatisticsTotalDto.setTotalDeductionAmount(String.valueOf(totalDeductionAmount));
            //滞纳金(小计)
            reportFeeMonthStatisticsTotalDto.setTotalLateFee(String.valueOf(totalLateFee));
            //空置房打折(小计)
            reportFeeMonthStatisticsTotalDto.setTotalVacantHousingDiscount(String.valueOf(totalVacantHousingDiscount));
            //空置房减免(小计)
            reportFeeMonthStatisticsTotalDto.setTotalVacantHousingReduction(String.valueOf(totalVacantHousingReduction));
            //应收金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllReceivableAmount(String.format("%.2f", allReceivableAmount));
            //实收金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllReceivedAmount(String.format("%.2f", allReceivedAmount));
            //优惠金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllPreferentialAmount(String.valueOf(allPreferentialAmount));
            //减免金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllDeductionAmount(String.valueOf(allDeductionAmount));
            //滞纳金(大计)
            reportFeeMonthStatisticsTotalDto.setAllLateFee(String.valueOf(allLateFee));
            //空置房打折金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllVacantHousingDiscount(String.valueOf(allVacantHousingDiscount));
            //空置房减免金额(大计)
            reportFeeMonthStatisticsTotalDto.setAllVacantHousingReduction(String.valueOf(allVacantHousingReduction));
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
            reportList.addAll(reportFeeMonthStatisticsDtos);
            reportFeeMonthStatisticsTotalDto = new ReportFeeMonthStatisticsTotalDto();
        }

        /*FeeDetailResultVo resultVo = new FeeDetailResultVo(countInfo.getDouble("totalReceivableAmount"), countInfo.getDouble("totalReceivedAmount"),
                (int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportList);*/

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportList, reportFeeMonthStatisticsTotalDto);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
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
                int day = DateUtil.daysBetween(nowDate, DateUtil.getDateFromString(reportFeeMonthStatisticsDto.getFeeCreateTime(),
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
