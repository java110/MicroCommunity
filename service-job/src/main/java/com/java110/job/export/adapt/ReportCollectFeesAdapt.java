package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.intf.report.IQueryPayFeeDetailInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收费状况表导出
 */
@Service("reportCollectFees")
public class ReportCollectFeesAdapt implements IExportDataAdapt {

    @Autowired
    private IQueryPayFeeDetailInnerServiceSMO queryPayFeeDetailInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("收费状况表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用项");
        row.createCell(1).setCellValue("费用类型");
        row.createCell(2).setCellValue("应缴金额（元）");
        row.createCell(3).setCellValue("应收金额（元）");
        row.createCell(4).setCellValue("未收金额（元）");
        row.createCell(5).setCellValue("实收金额（元）");
        row.createCell(6).setCellValue("欠费金额（元）");
        row.createCell(7).setCellValue("优惠金额（元）");
        row.createCell(8).setCellValue("减免金额（元）");
        row.createCell(9).setCellValue("赠送金额（元）");
        row.createCell(10).setCellValue("滞纳金（元）");
        row.createCell(11).setCellValue("空置房打折（元）");
        row.createCell(12).setCellValue("空置房减免（元）");
        row.createCell(13).setCellValue("收费率");
        JSONObject reqJson = exportDataDto.getReqJson();
        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsPrepaymentDto.class);
        if (reqJson.containsKey("roomName") && !StringUtil.isEmpty(reqJson.getString("roomName"))) {
            String[] roomNameArray = reqJson.getString("roomName").split("-", 3);
            reportFeeMonthStatisticsPrepaymentDto.setFloorNum(roomNameArray[0]);
            reportFeeMonthStatisticsPrepaymentDto.setUnitNum(roomNameArray[1]);
            reportFeeMonthStatisticsPrepaymentDto.setRoomNum(roomNameArray[2]);
        }
        //查询数据
        getReportCollectFees(sheet, reportFeeMonthStatisticsPrepaymentDto);
        return workbook;
    }

    private void getReportCollectFees(Sheet sheet, ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        reportFeeMonthStatisticsPrepaymentDto.setPage(1);
        reportFeeMonthStatisticsPrepaymentDto.setRow(MAX_ROW);
        ResultVo resultVo = queryPayFeeDetailInnerServiceSMOImpl.queryReportCollectFees(reportFeeMonthStatisticsPrepaymentDto);
        appendData(resultVo, sheet, 0);
        if (resultVo.getRecords() < 2) {
            return;
        }
        for (int page = 2; page <= resultVo.getRecords(); page++) {
            reportFeeMonthStatisticsPrepaymentDto.setPage(page);
            reportFeeMonthStatisticsPrepaymentDto.setRow(MAX_ROW);
            resultVo = queryPayFeeDetailInnerServiceSMOImpl.queryReportCollectFees(reportFeeMonthStatisticsPrepaymentDto);
            appendData(resultVo, sheet, (page - 1) * MAX_ROW);
        }
    }

    private void appendData(ResultVo resultVo, Sheet sheet, int step) {
        List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepayments = (List<ReportFeeMonthStatisticsPrepaymentDto>) resultVo.getData();
        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < reportFeeMonthStatisticsPrepayments.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = JSONObject.parseObject(JSONObject.toJSONString(reportFeeMonthStatisticsPrepayments.get(roomIndex)));
            row.createCell(0).setCellValue(dataObj.getString("feeName"));
            row.createCell(1).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(2).setCellValue(dataObj.getString("allPayableAmount"));
            row.createCell(3).setCellValue(dataObj.getString("allReceivableAmount"));
            //应收金额
            double allReceivableAmount = Double.parseDouble(dataObj.getString("allReceivableAmount"));
            //实收金额
            double allReceivedAmount = Double.parseDouble(dataObj.getString("allReceivedAmount"));
            //优惠金额
            double allPreferentialAmount = Double.parseDouble(dataObj.getString("allPreferentialAmount"));
            //减免金额
            double allDeductionAmount = Double.parseDouble(dataObj.getString("allDeductionAmount"));
            //滞纳金
            double allLateFee = Double.parseDouble(dataObj.getString("allLateFee"));
            //空置房打折金额
            double allVacantHousingDiscount = Double.parseDouble(dataObj.getString("allVacantHousingDiscount"));
            //空置房减免金额
            double allVacantHousingReduction = Double.parseDouble(dataObj.getString("allVacantHousingReduction"));
            //计算未收金额，未收金额 = 应收金额 - 实收金额 - 优惠金额 - 减免金额 + 滞纳金 - 空置房打折金额 - 空置房减免金额
            double outstandingAmount = allReceivableAmount - allReceivedAmount - allPreferentialAmount - allDeductionAmount + allLateFee
                    - allVacantHousingDiscount - allVacantHousingReduction;
            row.createCell(4).setCellValue(String.valueOf(outstandingAmount));
            row.createCell(5).setCellValue(dataObj.getString("allReceivedAmount"));
            row.createCell(6).setCellValue(dataObj.getString("allOweAmount"));
            row.createCell(7).setCellValue(dataObj.getString("allPreferentialAmount"));
            row.createCell(8).setCellValue(dataObj.getString("allDeductionAmount"));
            row.createCell(9).setCellValue(dataObj.getString("allGiftAmount"));
            row.createCell(10).setCellValue(dataObj.getString("allLateFee"));
            row.createCell(11).setCellValue(dataObj.getString("allVacantHousingDiscount"));
            row.createCell(12).setCellValue(dataObj.getDouble("allVacantHousingReduction"));
            //计算收费率，收费率：(应收金额 - 未收金额)/应收金额
            double chargeRate = ((allReceivableAmount - outstandingAmount) / allReceivableAmount) * 100;
            BigDecimal bigDecimal = new BigDecimal(chargeRate);
            double newChargeRate = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            row.createCell(13).setCellValue(newChargeRate + "%");
        }
    }
}
