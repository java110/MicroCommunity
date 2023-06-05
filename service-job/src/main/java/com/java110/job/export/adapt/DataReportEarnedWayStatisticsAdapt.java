package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 收款方式统计 导出
 */
@Service("dataReportEarnedWayStatistics")
public class DataReportEarnedWayStatisticsAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        JSONObject reqJson = exportDataDto.getReqJson();
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if (!StringUtil.isEmpty(startDate) && !startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("startDate", startDate);
        }
        if (!StringUtil.isEmpty(endDate) && !endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("endDate", endDate);
        }

        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("收款方式统计");
        Row row = sheet.createRow(0);

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));

        List<Map> datas = null;
        // todo 根据缴费方式统计
        datas = reportFeeStatisticsInnerServiceSMOImpl.getReceivedFeeByPrimeRate(queryStatisticsDto);

        if(datas == null || datas.size() < 1){
            return workbook;
        }

        for(int dataIndex = 0;dataIndex < datas.size();dataIndex ++){
            row.createCell(dataIndex+1).setCellValue(datas.get(dataIndex).get("name").toString());
        }

        row = sheet.createRow( 1);
        for(int dataIndex = 0;dataIndex < datas.size();dataIndex ++){
            row.createCell(dataIndex+1).setCellValue(datas.get(dataIndex).get("receivedAmount").toString());
        }
        return workbook;
    }
}
