package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * 费用提醒-到期提醒导出
 *
 * @author fqz
 * @date 2024-01-30
 */
@Service(value = "reportDeadlineFee")
public class ReportDeadlineFeeAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("预交费提醒");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号/车辆/合同");
        row.createCell(2).setCellValue("费用项");
        row.createCell(3).setCellValue("费用开始时间");
        row.createCell(4).setCellValue("距离费用开始时间（天）");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getReportDeadlineFee(sheet, reqJson);
        return workbook;
    }

    private void getReportDeadlineFee(Sheet sheet, JSONObject reqJson) {
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsDto.class);
        reportFeeMonthStatisticsDto.setPage(1);
        reportFeeMonthStatisticsDto.setRow(MAX_ROW);
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryDeadlineFee(reportFeeMonthStatisticsDto);
        appendData(reportFeeMonthStatisticsDtos, sheet);
    }

    private void appendData(List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < reportFeeMonthStatisticsDtos.size(); index++) {
            row = sheet.createRow(index + 1);
            ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = reportFeeMonthStatisticsDtos.get(index);
            row.createCell(0).setCellValue(index + 1);
            row.createCell(1).setCellValue(reportFeeMonthStatisticsDto.getObjName());
            row.createCell(2).setCellValue(reportFeeMonthStatisticsDto.getFeeName());
            row.createCell(3).setCellValue(reportFeeMonthStatisticsDto.getDeadlineTime());
            row.createCell(4).setCellValue(reportFeeMonthStatisticsDto.getOweDay());
        }
    }
}
