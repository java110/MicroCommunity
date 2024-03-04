package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.reportFee.ReportFeeYearCollectionDetailDto;
import com.java110.dto.reportFee.ReportFeeYearCollectionDto;
import com.java110.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
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
 * 报表专家导出
 */
@Service("reportProficient")
public class ReportProficientAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    @Autowired
    private IReportFeeYearCollectionDetailInnerServiceSMO reportFeeYearCollectionDetailInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        JSONObject reqJson = exportDataDto.getReqJson();
        //获取付费对象类型
        String objType = reqJson.getString("objType");
        if (!StringUtil.isEmpty(objType) && objType.equals("3333")) { //房屋
            Sheet sheet = workbook.createSheet("房屋费台账");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("姓名");
            row.createCell(1).setCellValue("房号");
            row.createCell(2).setCellValue("联系电话");
            row.createCell(3).setCellValue("面积");
            row.createCell(4).setCellValue("收费类型");
            row.createCell(5).setCellValue("费用名称");
            ReportFeeYearCollectionDto reportFeeYearCollectionDto = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionDto.class);
            reportFeeYearCollectionDto.setPage(1);
            reportFeeYearCollectionDto.setRow(MAX_ROW);
            List<ReportFeeYearCollectionDto> reportFeeYearCollections = reportFeeYearCollectionInnerServiceSMOImpl.queryReportFeeYearCollections(reportFeeYearCollectionDto);
            JSONObject dataObj = null;
            for (int index = 0; index < reportFeeYearCollections.size(); index++) {
                dataObj = JSONObject.parseObject(JSONObject.toJSONString(reportFeeYearCollections.get(index)));
                ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
                reportFeeYearCollectionDetailDto.setCollectionId(dataObj.getString("collectionId"));
                List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetails =
                        reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetails(reportFeeYearCollectionDetailDto);
                for (int detailIndex = 0; detailIndex < reportFeeYearCollectionDetails.size(); detailIndex++) {
                    row.createCell(6 + detailIndex).setCellValue(reportFeeYearCollectionDetails.get(detailIndex).getCollectionYear() + "年");
                }
                appendRoomData(dataObj, sheet, 0, index);
            }
        } else if (!StringUtil.isEmpty(objType) && objType.equals("6666")) { //车辆
            Sheet sheet = workbook.createSheet("车辆费台账");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("姓名");
            row.createCell(1).setCellValue("车牌号");
            row.createCell(2).setCellValue("联系电话");
            row.createCell(3).setCellValue("收费类型");
            row.createCell(4).setCellValue("费用名称");
            ReportFeeYearCollectionDto reportFeeYearCollectionDto = BeanConvertUtil.covertBean(reqJson, ReportFeeYearCollectionDto.class);
            reportFeeYearCollectionDto.setPage(1);
            reportFeeYearCollectionDto.setRow(MAX_ROW);
            List<ReportFeeYearCollectionDto> reportFeeYearCollections = reportFeeYearCollectionInnerServiceSMOImpl.queryReportFeeYearCollections(reportFeeYearCollectionDto);
            JSONObject dataObj = null;
            for (int index = 0; index < reportFeeYearCollections.size(); index++) {
                dataObj = JSONObject.parseObject(JSONObject.toJSONString(reportFeeYearCollections.get(index)));
                ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
                reportFeeYearCollectionDetailDto.setCollectionId(dataObj.getString("collectionId"));
                List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetails =
                        reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetails(reportFeeYearCollectionDetailDto);
                for (int detailIndex = 0; detailIndex < reportFeeYearCollectionDetails.size(); detailIndex++) {
                    row.createCell(6 + detailIndex).setCellValue(reportFeeYearCollectionDetails.get(detailIndex).getCollectionYear() + "年");
                }
                appendCarData(dataObj, sheet, 0, index);
            }
        }
        return workbook;
    }

    //房屋费台账
    private void appendRoomData(JSONObject dataObj, Sheet sheet, int step, int index) {
        Row row = null;
        row = sheet.createRow(index + step + 1);
        ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
        reportFeeYearCollectionDetailDto.setCollectionId(dataObj.getString("collectionId"));
        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetails =
                reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetails(reportFeeYearCollectionDetailDto);
        row.createCell(0).setCellValue(dataObj.getString("ownerName"));
        row.createCell(1).setCellValue(dataObj.getString("objName"));
        row.createCell(2).setCellValue(dataObj.getString("ownerLink"));
        row.createCell(3).setCellValue(dataObj.getString("builtUpArea"));
        row.createCell(4).setCellValue(dataObj.getString("feeTypeCdName"));
        row.createCell(5).setCellValue(dataObj.getString("feeName"));
        for (int detailIndex = 0; detailIndex < reportFeeYearCollectionDetails.size(); detailIndex++) {
            row.createCell(6 + detailIndex).setCellValue(reportFeeYearCollectionDetails.get(detailIndex).getReceivedAmount());
        }
    }

    //车辆费台账
    private void appendCarData(JSONObject dataObj, Sheet sheet, int step, int index) {
        Row row = null;
        row = sheet.createRow(index + step + 1);
        ReportFeeYearCollectionDetailDto reportFeeYearCollectionDetailDto = new ReportFeeYearCollectionDetailDto();
        reportFeeYearCollectionDetailDto.setCollectionId(dataObj.getString("collectionId"));
        List<ReportFeeYearCollectionDetailDto> reportFeeYearCollectionDetails =
                reportFeeYearCollectionDetailInnerServiceSMOImpl.queryReportFeeYearCollectionDetails(reportFeeYearCollectionDetailDto);
        row.createCell(0).setCellValue(dataObj.getString("ownerName"));
        row.createCell(1).setCellValue(dataObj.getString("objName"));
        row.createCell(2).setCellValue(dataObj.getString("ownerLink"));
        row.createCell(3).setCellValue(dataObj.getString("feeTypeCdName"));
        row.createCell(4).setCellValue(dataObj.getString("feeName"));
        for (int detailIndex = 0; detailIndex < reportFeeYearCollectionDetails.size(); detailIndex++) {
            row.createCell(5 + detailIndex).setCellValue(reportFeeYearCollectionDetails.get(detailIndex).getReceivedAmount());
        }
    }
}
