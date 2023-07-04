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
 * 月实收数据导出
 */
@Service("dataMonthOweStatistics")
public class DataMonthOweStatisticsAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    private static final int MAX_ROW = 100;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        JSONObject reqJson = exportDataDto.getReqJson();
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

        String startDate = reqJson.getString("feeStartDate");
        String endDate = reqJson.getString("feeEndDate");
        if (!StringUtil.isEmpty(startDate) && !startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("feeStartDate", startDate);
        }
        if (!StringUtil.isEmpty(endDate) && !endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("feeEndDate", endDate);
        }

        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("实收明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("房屋");
        row.createCell(1).setCellValue("业主");
        row.createCell(2).setCellValue("费用名称");
        row.createCell(3).setCellValue("欠费时间段");
        row.createCell(4).setCellValue("欠费金额");

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("feeStartDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("feeEndDate"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        long count = reportFeeStatisticsInnerServiceSMOImpl.getMonthOweDetailCount(queryStatisticsDto);

        int maxPage  = (int) Math.ceil((double) count / (double) MAX_ROW);
        List<Map> infos = null;
        for (int page = 1; page <= maxPage; page++) {
            queryStatisticsDto.setPage(page);
            queryStatisticsDto.setRow(MAX_ROW);
            infos = reportFeeStatisticsInnerServiceSMOImpl.getMonthOweDetailInfo(queryStatisticsDto);
            appendData(infos, sheet, (page - 1) * MAX_ROW);
        }

        //todo 欠费金额
        double oweAmount = reportFeeStatisticsInnerServiceSMOImpl.getMonthOweDetailAmount(queryStatisticsDto);

        row = sheet.createRow((int)count+1);
        row.createCell(0).setCellValue("总欠费");
        row.createCell(1).setCellValue(oweAmount);
        return workbook;
    }

    /**
     * 封装数据到Excel中
     *
     * @param datas
     * @param sheet
     */
    private void appendData(List<Map> datas, Sheet sheet, int step) {
        Row row = null;
        Map dataObj = null;
        for (int roomIndex = 0; roomIndex < datas.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = datas.get(roomIndex);
            row.createCell(0).setCellValue(getValue(dataObj, "objName"));
            row.createCell(1).setCellValue(getValue(dataObj, "ownerName") + "(" + getValue(dataObj, "link") + ")");
            row.createCell(2).setCellValue(getValue(dataObj, "feeName"));
            row.createCell(3).setCellValue(getValue(dataObj, "startTime") + "~" + getValue(dataObj, "endTime"));
            row.createCell(4).setCellValue(getValue(dataObj, "receivableAmount"));
        }

    }

    private String getValue(Map data, String key) {
        Object value = data.get(key);
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
