package com.java110.front.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.assetExport.IExportReportFeeSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("exportReportFeeSMOImpl")
public class ExportReportFeeSMOImpl extends BaseComponentSMO implements IExportReportFeeSMO {
    private final static Logger logger = LoggerFactory.getLogger(ExportReportFeeSMOImpl.class);

    public static final String REPORT_FEE_SUMMARY = "reportFeeSummary";
    public static final String REPORT_FLOOR_UNIT_FEE_SUMMARY = "reportFloorUnitFeeSummary";
    public static final String REPORT_FEE_BREAKDOWN = "reportFeeBreakdown";
    public static final String REPORT_FEE_DETAIL = "reportFeeDetail";
    public static final String REPORT_OWE_FEE_DETAIL = "reportOweFeeDetail";
    public static final String REPORT_PAY_FEE_DETAIL = "reportPayFeeDetail";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception {

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "communityId", "请求中未包含小区");
        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "pagePath", "请求中未包含页面");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        String pagePath = reqJson.getString("pagePath");

        switch (pagePath) {
            case REPORT_FEE_SUMMARY:
                reportFeeSummary(pd, result, workbook);
                break;
            case REPORT_FLOOR_UNIT_FEE_SUMMARY:
                reportFloorUnitFeeSummary(pd, result, workbook);
                break;
            case REPORT_FEE_BREAKDOWN:
                reportFeeBreakdown(pd, result, workbook);
                break;
            case REPORT_FEE_DETAIL:
                reportFeeDetail(pd, result, workbook);
                break;

            case REPORT_OWE_FEE_DETAIL:
                reportOweFeeDetail(pd, result, workbook);
                break;
            case REPORT_PAY_FEE_DETAIL:
                reportPayFeeDetail(pd, result, workbook);
                break;

        }


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=" + pagePath + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
        headers.add("Pargam", "no-cache");
        headers.add("Cache-Control", "no-cache");
        //headers.add("Content-Disposition", "attachment; filename=" + outParam.getString("fileName"));
        headers.add("Accept-Ranges", "bytes");
        byte[] context = null;
        try {
            workbook.write(os);
            context = os.toByteArray();
            os.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 保存数据
            return new ResponseEntity<Object>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 保存数据
        return new ResponseEntity<Object>(context, headers, HttpStatus.OK);
    }

    private void reportPayFeeDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("缴费明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("费用项");
        row.createCell(3).setCellValue("支付方式");
        row.createCell(4).setCellValue("缴费开始时间");
        row.createCell(5).setCellValue("缴费结束时间");
        row.createCell(6).setCellValue("缴费时间");
        row.createCell(7).setCellValue("应收金额");
        row.createCell(8).setCellValue("实收金额");


        //查询楼栋信息
        JSONArray rooms = this.getReportPayFeeDetail(pd, result);

        if (rooms == null) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);

            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("feeName"));
            row.createCell(3).setCellValue(dataObj.getString("primeRate"));
            row.createCell(4).setCellValue(dataObj.getString("startTime"));
            row.createCell(5).setCellValue(dataObj.getString("endTime"));
            row.createCell(6).setCellValue(dataObj.getString("createTime"));
            row.createCell(7).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(8).setCellValue(dataObj.getString("receivedAmount"));

        }
    }

    private JSONArray getReportPayFeeDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/reportFeeMonthStatistics/queryPayFeeDetail" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("data");
    }

    private void reportOweFeeDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("欠费明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("费用项");
        row.createCell(3).setCellValue("费用开始时间");
        row.createCell(4).setCellValue("欠费时长（天）");
        row.createCell(5).setCellValue("欠费金额");


        //查询楼栋信息
        JSONArray rooms = this.getReportOweFeeDetail(pd, result);
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);

            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("feeName"));
            row.createCell(3).setCellValue(dataObj.getString("feeCreateTime"));
            row.createCell(4).setCellValue(dataObj.getString("oweDay"));
            row.createCell(5).setCellValue(dataObj.getString("oweAmount"));

        }
    }

    private void reportFeeDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("费用项");
        row.createCell(3).setCellValue("费用开始时间");
        row.createCell(4).setCellValue("费用结束时间");
        row.createCell(5).setCellValue("应收金额");
        row.createCell(6).setCellValue("实收金额");


        //查询楼栋信息
        JSONArray rooms = this.getReportFeeDetail(pd, result);
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);

            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("feeName"));
            row.createCell(3).setCellValue(dataObj.getString("feeCreateTime"));
            row.createCell(4).setCellValue(dataObj.getString("deadlineTime"));
            row.createCell(5).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(6).setCellValue(dataObj.getString("receivedAmount"));

        }
    }

    private JSONArray getReportOweFeeDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/reportFeeMonthStatistics/queryOweFeeDetail?communityId=" + result.getCommunityId() + "&page=1&row=10000";
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("data");
    }

    private JSONArray getReportFeeDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/reportFeeMonthStatistics/queryFeeDetail?communityId=" + result.getCommunityId() + "&page=1&row=10000";
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("data");
    }

    private void reportFeeBreakdown(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用分项表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("费用项");
        row.createCell(2).setCellValue("费用开始时间");
        row.createCell(3).setCellValue("应收金额");
        row.createCell(4).setCellValue("实收金额");
        row.createCell(5).setCellValue("欠费金额");


        //查询楼栋信息
        JSONArray rooms = this.getReportFeeBreakdown(pd, result);
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);

            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("feeName"));
            row.createCell(2).setCellValue(dataObj.getString("feeCreateTime"));
            row.createCell(3).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(4).setCellValue(dataObj.getString("receivedAmount"));
            row.createCell(5).setCellValue(dataObj.getString("oweAmount"));

        }
    }

    private JSONArray getReportFeeBreakdown(IPageData pd, ComponentValidateResult result) {

        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/reportFeeMonthStatistics/queryFeeBreakdown?communityId=" + result.getCommunityId() + "&page=1&row=10000";
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("data");
    }

    private void reportFloorUnitFeeSummary(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("楼栋费用表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("日期");
        row.createCell(1).setCellValue("楼栋");
        row.createCell(2).setCellValue("单元");
        row.createCell(3).setCellValue("应收金额");
        row.createCell(4).setCellValue("实收金额");
        row.createCell(5).setCellValue("欠费金额");


        //查询楼栋信息
        JSONArray rooms = this.getReportFloorUnitFeeSummary(pd, result);
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("feeYear") + "年" + dataObj.getString("feeMonth") + "月");
            row.createCell(1).setCellValue(dataObj.getString("floorNum") + "号楼");
            row.createCell(2).setCellValue(dataObj.getString("unitNum") + "单元");
            row.createCell(3).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(4).setCellValue(dataObj.getString("receivedAmount"));
            row.createCell(5).setCellValue(dataObj.getString("oweAmount"));

        }
    }

    private JSONArray getReportFloorUnitFeeSummary(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/reportFeeMonthStatistics/queryFloorUnitFeeSummary?communityId=" + result.getCommunityId() + "&page=1&row=10000";
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("data");

    }


    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getReportFeeSummaryFee(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/reportFeeMonthStatistics/queryReportFeeSummary?communityId=" + result.getCommunityId() + "&page=1&row=10000";
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("data");

    }

    /**
     * 获取 房屋信息
     *
     * @param componentValidateResult
     * @param workbook
     */
    private void reportFeeSummary(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用汇总表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("日期");
        row.createCell(1).setCellValue("应收金额");
        row.createCell(2).setCellValue("实收金额");
        row.createCell(3).setCellValue("欠费金额");


        //查询楼栋信息
        JSONArray rooms = this.getReportFeeSummaryFee(pd, componentValidateResult);
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);

            row.createCell(0).setCellValue(dataObj.getString("feeYear") + "年" + dataObj.getString("feeMonth") + "月");
            row.createCell(1).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(2).setCellValue(dataObj.getString("receivedAmount"));
            row.createCell(3).setCellValue(dataObj.getString("oweAmount"));

        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
