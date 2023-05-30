package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.report.IReportOrderStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("doDining")
public class DoDiningAdapt implements IExportDataAdapt {

    @Autowired
    private IReportOrderStatisticsInnerServiceSMO reportOrderStatisticsInnerServiceSMOImpl;

    private static final int MAX_ROW = 200;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        JSONObject reqJson = exportDataDto.getReqJson();
        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if (!startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("startDate", startDate);
        }
        if (!endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("endDate", endDate);
        }

        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet( "就餐明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("就餐时间");
        row.createCell(1).setCellValue("商品");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("就餐人");
        row.createCell(4).setCellValue("就餐电话");
        row.createCell(5).setCellValue("订单编号");
        row.createCell(6).setCellValue("备注");


        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        ownerDto.setRow(MAX_ROW);
        int count = reportOrderStatisticsInnerServiceSMOImpl.getOwnerDiningCount(ownerDto);

        for (int page = 1; page <= count; page++) {
            ownerDto.setPage(page);
            ownerDto.setRow(MAX_ROW);
            List<Map> infos = reportOrderStatisticsInnerServiceSMOImpl.getOwnerDinings(ownerDto);
            appendData(infos, sheet, (page - 1) * MAX_ROW);
        }

        return workbook;
    }

    private void appendData(List<Map> datas, Sheet sheet, int step) {
        Row row = null;
        Map dataObj = null;
        String oweFee = "";
        String receivedFee = "";
        for (int roomIndex = 0; roomIndex < datas.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = datas.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.get("createTime").toString());
            row.createCell(1).setCellValue(dataObj.get("goodsName") == null ? "" : dataObj.get("goodsName").toString());
            row.createCell(2).setCellValue(dataObj.get("ownerName") == null ? "" : dataObj.get("ownerName").toString());
            row.createCell(3).setCellValue(dataObj.get("personName")== null ? "" : dataObj.get("personName").toString());
            row.createCell(4).setCellValue(dataObj.get("personTel") == null ? "" : dataObj.get("personTel").toString());
            row.createCell(5).setCellValue(dataObj.get("orderId")== null ? "" : dataObj.get("orderId").toString());
            row.createCell(6).setCellValue(dataObj.get("remark")== null ? "" : dataObj.get("remark").toString());

        }

    }

    private void freshStartDateAndEndDate(List<Map> infos, OwnerDto ownerDto) {
        if (infos == null || infos.size() < 1) {
            return;
        }

        for (Map info : infos) {
            info.put("startDate", ownerDto.getStartDate());
            info.put("endDate", ownerDto.getEndDate());
        }
    }

}
