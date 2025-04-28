package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component("reportOweFeeDetail")
public class ReportOweFeeDetailAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {

        JSONObject paramIn = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("欠费明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("业主电话");
        row.createCell(4).setCellValue("面积");
        row.createCell(5).setCellValue("费用项");
        row.createCell(6).setCellValue("开始时间");
        row.createCell(7).setCellValue("结束时间");
        row.createCell(8).setCellValue("欠费时长（天）");
        row.createCell(9).setCellValue("欠费时长（月）");
        row.createCell(10).setCellValue("欠费金额");

        List<ReportFeeMonthStatisticsDto> rooms = this.getReportOweFeeDetail(paramIn);
        if (ListUtil.isNull(rooms)) {
            return workbook;
        }
        ReportFeeMonthStatisticsDto dataObj = null;
        BigDecimal monthDec = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.get(roomIndex);
            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getObjName());
            row.createCell(2).setCellValue(dataObj.getOwnerName());
            row.createCell(3).setCellValue(dataObj.getOwnerTel());
            row.createCell(4).setCellValue(dataObj.getBuiltUpArea());
            row.createCell(5).setCellValue(dataObj.getFeeName());
            row.createCell(6).setCellValue(dataObj.getStartTime());
            row.createCell(7).setCellValue(dataObj.getEndTime());
            row.createCell(8).setCellValue(dataObj.getOweDay());
            monthDec = new BigDecimal(dataObj.getOweDay());
            monthDec = monthDec.divide(new BigDecimal("30"), 2, BigDecimal.ROUND_HALF_UP);
            row.createCell(9).setCellValue(monthDec.doubleValue());
            row.createCell(10).setCellValue(dataObj.getOweAmount());
        }
        return workbook;
    }

    private List<ReportFeeMonthStatisticsDto> getReportOweFeeDetail(JSONObject paramIn) {

        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(paramIn, ReportFeeMonthStatisticsDto.class);
        reportFeeMonthStatisticsDto.setPage(1);
        reportFeeMonthStatisticsDto.setRow(10000);
        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetailCount(reportFeeMonthStatisticsDto);
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetail(reportFeeMonthStatisticsDto);
            ReportFeeMonthStatisticsDto tmpReportFeeMonthStatisticsDto = reportFeeMonthStatisticsInnerServiceSMOImpl.queryOweFeeDetailMajor(reportFeeMonthStatisticsDto);
            if (!ListUtil.isNull(reportFeeMonthStatisticsDtos)) {
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

        return reportFeeMonthStatisticsDtos;


    }

    private void freshReportOweDay(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos) {
        int day = 0;
        for (ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto : reportFeeMonthStatisticsDtos) {
            try {
                day = DateUtil.daysBetween(DateUtil.getDateFromStringB(reportFeeMonthStatisticsDto.getEndTime()),
                        DateUtil.getDateFromStringB(reportFeeMonthStatisticsDto.getStartTime()));
                reportFeeMonthStatisticsDto.setOweDay(day);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
