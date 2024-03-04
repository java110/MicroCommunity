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

import java.util.List;

/**
 * 账单明细表导出
 */
@Service("reportPrePaymentDetail")
public class ReportPrePaymentDetailAdapt implements IExportDataAdapt {

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
        Sheet sheet = workbook.createSheet("账单明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("报表ID");
        row.createCell(1).setCellValue("付费对象");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("费用项");
        row.createCell(4).setCellValue("费用类型");
        row.createCell(5).setCellValue("费用状态");
        row.createCell(6).setCellValue("账单状态");
        row.createCell(7).setCellValue("支付方式");
        row.createCell(8).setCellValue("费用开始时间");
        row.createCell(9).setCellValue("费用结束时间");
        row.createCell(10).setCellValue("缴费时间");
        row.createCell(11).setCellValue("应缴金额（元）");
        row.createCell(12).setCellValue("应收金额（元）");
        row.createCell(13).setCellValue("实收金额（元）");
        row.createCell(14).setCellValue("欠费金额（元）");
        row.createCell(15).setCellValue("优惠金额（元）");
        row.createCell(16).setCellValue("减免金额（元）");
        row.createCell(17).setCellValue("赠送金额（元）");
        row.createCell(18).setCellValue("滞纳金（元）");
        row.createCell(19).setCellValue("空置房打折金额（元）");
        row.createCell(20).setCellValue("空置房减免金额（元）");
        row.createCell(21).setCellValue("面积（平方米）");
        JSONObject reqJson = exportDataDto.getReqJson();
        ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsPrepaymentDto.class);
        if (reqJson.containsKey("roomName") && !StringUtil.isEmpty(reqJson.getString("roomName"))) {
            String[] roomNameArray = reqJson.getString("roomName").split("-", 3);
            reportFeeMonthStatisticsPrepaymentDto.setFloorNum(roomNameArray[0]);
            reportFeeMonthStatisticsPrepaymentDto.setUnitNum(roomNameArray[1]);
            reportFeeMonthStatisticsPrepaymentDto.setRoomNum(roomNameArray[2]);
        }
        //查询数据
        getReportPrePaymentDetail(sheet, reportFeeMonthStatisticsPrepaymentDto);
        return workbook;
    }

    private void getReportPrePaymentDetail(Sheet sheet, ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto) {
        reportFeeMonthStatisticsPrepaymentDto.setPage(1);
        reportFeeMonthStatisticsPrepaymentDto.setRow(MAX_ROW);
        ResultVo resultVo = queryPayFeeDetailInnerServiceSMOImpl.queryPrepayment(reportFeeMonthStatisticsPrepaymentDto);
        appendData(resultVo, sheet, 0);
        if (resultVo.getRecords() < 2) {
            return;
        }
        for (int page = 2; page <= resultVo.getRecords(); page++) {
            reportFeeMonthStatisticsPrepaymentDto.setPage(page);
            reportFeeMonthStatisticsPrepaymentDto.setRow(MAX_ROW);
            resultVo = queryPayFeeDetailInnerServiceSMOImpl.queryPrepayment(reportFeeMonthStatisticsPrepaymentDto);
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
            row.createCell(0).setCellValue(dataObj.getString("prepaymentId"));
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("feeName"));
            row.createCell(4).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(5).setCellValue(dataObj.getString("prepaymentStateName"));
            row.createCell(6).setCellValue(dataObj.getString("billStateName"));
            if(!StringUtil.isEmpty(dataObj.getString("primeRate"))) {
                row.createCell(7).setCellValue(dataObj.getString("primeRate"));
            } else {
                row.createCell(7).setCellValue("--");
            }
            row.createCell(8).setCellValue(dataObj.getString("feeBeginTime"));
            row.createCell(9).setCellValue(dataObj.getString("feeFinishTime"));
            if(!StringUtil.isEmpty(dataObj.getString("payTime"))) {
                row.createCell(10).setCellValue(dataObj.getString("payTime"));
            } else {
                row.createCell(10).setCellValue("--");
            }
            row.createCell(11).setCellValue(dataObj.getString("payableAmount"));
            row.createCell(12).setCellValue(dataObj.getDouble("prepaymentReceivableAmount"));
            row.createCell(13).setCellValue(dataObj.getDouble("prepaymentReceivedAmount"));
            row.createCell(14).setCellValue(dataObj.getDouble("oweAmount"));
            row.createCell(15).setCellValue(dataObj.getDouble("preferentialAmount"));
            row.createCell(16).setCellValue(dataObj.getDouble("deductionAmount"));
            row.createCell(17).setCellValue(dataObj.getDouble("giftAmount"));
            row.createCell(18).setCellValue(dataObj.getString("lateFee"));
            row.createCell(19).setCellValue(dataObj.getString("vacantHousingDiscount"));
            row.createCell(20).setCellValue(dataObj.getString("vacantHousingReduction"));
            row.createCell(21).setCellValue(dataObj.getString("builtUpArea"));
        }
    }
}
