package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.report.ReportDeposit;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service("reportPayFeeDeposit")
public class ReportPayFeeDepositAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;
    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {

        JSONObject reqJson = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("押金报表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用ID");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("费用类型");
        row.createCell(4).setCellValue("费用项");
        row.createCell(5).setCellValue("费用开始时间");
        row.createCell(6).setCellValue("费用结束时间");
        row.createCell(7).setCellValue("创建时间");
        row.createCell(8).setCellValue("付费对象类型");
        row.createCell(9).setCellValue("付款方ID");
        row.createCell(10).setCellValue("应收金额");
        row.createCell(11).setCellValue("状态");
        row.createCell(12).setCellValue("退费状态");

        List<ReportDeposit> reportPayFeeDeposits = this.getReportPayFeeDeposit(reqJson);
        if (ListUtil.isNull(reportPayFeeDeposits)) {
            return workbook;
        }
        ReportDeposit dataObj = null;
        for (int roomIndex = 0; roomIndex < reportPayFeeDeposits.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = reportPayFeeDeposits.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.getFeeId());
            if (!StringUtil.isEmpty(dataObj.getPayerObjType()) && dataObj.getPayerObjType().equals("3333")) { //房屋
                row.createCell(1).setCellValue(dataObj.getFloorNum() + "-" + dataObj.getUnitNum() + "-" + dataObj.getRoomNum());
            } else {
                row.createCell(1).setCellValue(dataObj.getObjName());
            }
            row.createCell(2).setCellValue(dataObj.getOwnerName());
            row.createCell(3).setCellValue(dataObj.getFeeTypeCdName());
            row.createCell(4).setCellValue(dataObj.getFeeName());
            row.createCell(5).setCellValue(dataObj.getStartTime());
            row.createCell(6).setCellValue(dataObj.getDeadlineTime());
            row.createCell(7).setCellValue(dataObj.getCreateTime());
            row.createCell(8).setCellValue(dataObj.getPayerObjTypeName());
            row.createCell(9).setCellValue(dataObj.getPayerObjId());
            row.createCell(10).setCellValue(dataObj.getAdditionalAmount());
            row.createCell(11).setCellValue(dataObj.getStateName());
            if (dataObj.getState().equals("2009001")) {
                row.createCell(12).setCellValue(dataObj.getDetailStateName());
            } else {
                row.createCell(12).setCellValue("未缴费");
            }
        }
        return workbook;
    }

    private List<ReportDeposit> getReportPayFeeDeposit(JSONObject reqJson) {

        ReportDeposit reportDeposit = BeanConvertUtil.covertBean(reqJson,ReportDeposit.class);
        reportDeposit.setPage(1);
        reportDeposit.setRow(10000);
        List<ReportDeposit> reportDeposits = reportFeeMonthStatisticsInnerServiceSMOImpl.queryPayFeeDeposit(reportDeposit);
        //查询押金费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reportDeposit.getCommunityId());
        feeConfigDto.setFeeTypeCd("888800010006");
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);


        for (ReportDeposit deposit : reportDeposits) {
            deposit.setFeeConfigDtos(feeConfigDtos);
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(deposit.getPayerObjType())) {
                deposit.setObjName(deposit.getFloorNum()
                        + "栋" + deposit.getUnitNum()
                        + "单元" + deposit.getRoomNum() + "室");
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(deposit.getPayerObjType())) {
                deposit.setObjName(deposit.getCarNum());
            }
        }

        return reportDeposits;
    }
}
