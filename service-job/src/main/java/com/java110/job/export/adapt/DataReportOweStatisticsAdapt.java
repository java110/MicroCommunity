package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("dataReportOweStatistics")
public class DataReportOweStatisticsAdapt implements IExportDataAdapt {

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

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

        Sheet sheet = workbook.createSheet("欠费统计");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("楼栋");
        row.createCell(1).setCellValue("户数");
        row.createCell(2).setCellValue("收费户数");
        row.createCell(3).setCellValue("欠费户数");
        row.createCell(4).setCellValue("欠费");
        DictDto dictDto = new DictDto();
        dictDto.setTableName("pay_fee_config");
        dictDto.setTableColumns("fee_type_cd_show");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
            row.createCell(5 + dictIndex).setCellValue(dictDtos.get(dictIndex).getName());
        }

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));

        List<Map> datas = null;
        // todo 按楼栋计算实收情况
        datas = reportFeeStatisticsInnerServiceSMOImpl.getOweFeeByFloor(queryStatisticsDto);

        datas = computeOweReceivedFee(datas);

        appendData(datas, sheet, dictDtos);

        return workbook;
    }

    /**
     * 封装数据到Excel中
     * @param datas
     * @param sheet
     * @param dictDtos
     */
    private void appendData(List<Map> datas, Sheet sheet, List<DictDto> dictDtos) {
        Row row = null;
        Map dataObj = null;
        String oweFee = "";
        for (int roomIndex = 0; roomIndex < datas.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = datas.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.get("floorNum").toString());
            row.createCell(1).setCellValue(dataObj.get("roomCount").toString());
            row.createCell(2).setCellValue(dataObj.get("feeRoomCount").toString());
            row.createCell(3).setCellValue(dataObj.get("oweRoomCount").toString());
            row.createCell(4).setCellValue(dataObj.get("receivedFee").toString());

            for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
                oweFee = dataObj.get("oweFee" + dictDtos.get(dictIndex).getStatusCd()).toString();
                if (StringUtil.isEmpty(oweFee)) {
                    oweFee = "0";
                }
                row.createCell(5 + dictIndex).setCellValue(oweFee);
            }
        }

    }

    private List<Map> computeOweReceivedFee(List<Map> datas) {
        if (datas == null || datas.size() < 1) {
            return new ArrayList<>();
        }

        List<Map> tmpDatas = new ArrayList<>();
        for (Map data : datas) {
            if (!hasInTmp(tmpDatas, data)) {
                tmpDatas.add(data);
            }
        }

        if (tmpDatas == null || tmpDatas.size() < 1) {
            return new ArrayList<>();
        }

        BigDecimal receivedFee = new BigDecimal(0.00);
        for (Map tmpData : tmpDatas) {
            for (Map data : datas) {
                if (!data.get("floorId").toString().equals(tmpData.get("floorId"))) {
                    continue;
                }

                receivedFee = receivedFee.add(new BigDecimal(data.get("oweFee").toString()));
                tmpData.put("oweFee" + data.get("feeTypeCd").toString(), data.get("oweFee"));
            }
            tmpData.put("oweFee", receivedFee.doubleValue());
        }

        return tmpDatas;
    }

    private boolean hasInTmp(List<Map> tmpDatas, Map data) {
        for (Map tmpData : tmpDatas) {
            if (tmpData.get("floorId").equals(data.get("floorId"))) {
                return true;
            }
        }
        return false;
    }
}
