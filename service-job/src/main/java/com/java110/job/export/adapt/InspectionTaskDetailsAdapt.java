package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检明细导出
 */
@Service("inspectionTaskDetails")
public class InspectionTaskDetailsAdapt implements IExportDataAdapt {

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("巡检明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("任务详情ID");
        row.createCell(1).setCellValue("巡检点名称");
        row.createCell(2).setCellValue("巡检计划名称");
        row.createCell(3).setCellValue("巡检路线名称");
        row.createCell(4).setCellValue("巡检人开始时间");
        row.createCell(5).setCellValue("巡检人结束时间");
        row.createCell(6).setCellValue("巡检点开始时间");
        row.createCell(7).setCellValue("巡检点结束时间");
        row.createCell(8).setCellValue("实际巡检时间");
        row.createCell(9).setCellValue("实际签到状态");
        row.createCell(10).setCellValue("计划巡检人");
        row.createCell(11).setCellValue("实际巡检人");
        row.createCell(12).setCellValue("巡检方式");
        row.createCell(13).setCellValue("任务状态");
        row.createCell(14).setCellValue("巡检点状态");
        row.createCell(15).setCellValue("巡检情况");


        JSONObject reqJson = exportDataDto.getReqJson();
        InspectionTaskDetailDto inspectionTaskDetailDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDetailDto.class);

        //查询数据
        getInspectionTaskDetail(sheet, inspectionTaskDetailDto);

        return workbook;

    }

    private void getInspectionTaskDetail(Sheet sheet, InspectionTaskDetailDto inspectionTaskDetailDto) {
        int count = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);

        double record = Math.ceil((double) count / MAX_ROW);

        if (count < 1) {
            return;
        }
        List<InspectionTaskDetailDto> inspectionTaskDetails = null;
        for (int page = 1; page <= record; page++) {
            inspectionTaskDetailDto.setPage(page);
            inspectionTaskDetailDto.setRow(MAX_ROW);
            inspectionTaskDetails = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);
            appendData(inspectionTaskDetails, sheet, (page - 1) * MAX_ROW);
        }
    }

    private void appendData(List<InspectionTaskDetailDto> inspectionTaskDetails, Sheet sheet, int step) {

        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < inspectionTaskDetails.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = JSONObject.parseObject(JSONObject.toJSONString(inspectionTaskDetails.get(roomIndex)));
            row.createCell(0).setCellValue(dataObj.getString("taskDetailId"));
            row.createCell(1).setCellValue(dataObj.getString("inspectionName"));
            row.createCell(2).setCellValue(dataObj.getString("inspectionPlanName"));
            row.createCell(3).setCellValue(dataObj.getString("routeName"));
            row.createCell(4).setCellValue(dataObj.getString("planInsTime"));
            row.createCell(5).setCellValue(dataObj.getString("planEndTime"));
            if (!StringUtil.isEmpty(dataObj.getString("pointStartTime"))) {
                row.createCell(6).setCellValue(dataObj.getString("pointStartTime"));
            } else {
                row.createCell(6).setCellValue("--");
            }
            if (!StringUtil.isEmpty(dataObj.getString("pointEndTime"))) {
                row.createCell(7).setCellValue(dataObj.getString("pointEndTime"));
            } else {
                row.createCell(7).setCellValue("--");
            }
            if (!StringUtil.isEmpty(dataObj.getString("inspectionTime"))) {
                row.createCell(8).setCellValue(dataObj.getString("inspectionTime"));
            } else {
                row.createCell(8).setCellValue("--");
            }
            row.createCell(9).setCellValue(dataObj.getString("inspectionStateName"));
            row.createCell(10).setCellValue(dataObj.getString("planUserName"));
            if (!StringUtil.isEmpty(dataObj.getString("actUserName"))) {
                row.createCell(11).setCellValue(dataObj.getString("actUserName"));
            } else {
                row.createCell(11).setCellValue("--");
            }
            row.createCell(12).setCellValue(dataObj.getString("signTypeName"));
            row.createCell(13).setCellValue(dataObj.getString("taskStateName"));
            row.createCell(14).setCellValue(dataObj.getString("stateName"));
            if (!StringUtil.isEmpty(dataObj.getString("description"))) {
                row.createCell(15).setCellValue(dataObj.getString("description"));
            } else {
                row.createCell(15).setCellValue("--");
            }
        }
    }
}
