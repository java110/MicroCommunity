package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.reportFeeMonthStatistics.ReportFeeMonthStatisticsDto;
import com.java110.intf.community.IDataPrivilegeUnitV1InnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.report.bmo.reportFeeMonthStatistics.IGetReportFeeMonthStatisticsBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询费用汇总表
 */
@Java110Cmd(serviceCode = "/reportFeeMonthStatistics/queryReportFeeSummary")
public class QueryReportFeeSummaryCmd extends Cmd {

    @Autowired
    private IGetReportFeeMonthStatisticsBMO getReportFeeMonthStatisticsBMOImpl;

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Autowired
    private IDataPrivilegeUnitV1InnerServiceSMO dataPrivilegeUnitV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String configIds = "";
        if(reqJson.containsKey("configIds")){
            configIds = reqJson.getString("configIds");
            reqJson.remove("configIds");
        }
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson,ReportFeeMonthStatisticsDto.class);

        String staffId = context.getReqHeaders().get("user-id");
        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(staffId);
        String[] unitIds = dataPrivilegeUnitV1InnerServiceSMOImpl.queryDataPrivilegeUnitsByStaff(dataPrivilegeStaffDto);

        if(unitIds != null && unitIds.length>0){
            reportFeeMonthStatisticsDto.setUnitIds(unitIds);
        }

        if (!StringUtil.isEmpty(configIds)) {
            reportFeeMonthStatisticsDto.setConfigIds(configIds.split(","));
        }
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
        context.setResponseEntity(responseEntity);
    }

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
}
