package com.java110.front.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.assetExport.IExportFeeManualCollectionSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.Money2ChineseUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.math.BigDecimal;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("exportFeeManualCollectionSMOImpl")
public class ExportFeeManualCollectionSMOImpl extends BaseComponentSMO implements IExportFeeManualCollectionSMO {
    private final static Logger logger = LoggerFactory.getLogger(ExportFeeManualCollectionSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception {

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "communityId", "请求中未包含小区");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();
        //获取楼信息
        getRoomFees(pd, result, workbook);


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=feeManualCollection_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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

    @Override
    public ResponseEntity<Object> downloadCollectionLetterOrder(IPageData pd) {
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "communityId", "请求中未包含小区");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();
        //获取楼信息
        getRoomOweFees(pd, result, workbook);


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=downloadCollectionLetterOrder_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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

    /**
     * 查询房屋欠费
     *
     * @param pd
     * @param result
     * @param workbook
     */
    private void getRoomOweFees(IPageData pd, ComponentValidateResult result, Workbook workbook) {

        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/feeApi/listAllRoomOweFees?communityId=" + result.getCommunityId();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);


        if (!savedRoomInfoResults.containsKey("data")) {
            return;
        }


        JSONArray rooms = savedRoomInfoResults.getJSONArray("data");

        if (rooms == null || rooms.size() < 1) {
            return;
        }

        Sheet sheet = workbook.createSheet("催缴单");
        int line = 0;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            line = generatorRoomOweFee(sheet, workbook, rooms.getJSONObject(roomIndex), line + 1);
        }
    }

    private int generatorRoomOweFee(Sheet sheet, Workbook workbook, JSONObject room, int line) {
        Row row = sheet.createRow(0 + line);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("缴费通知单");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        //第一行
        row = sheet.createRow(1 + line);
        row.createCell(0).setCellValue("收费二维码");
        row.createCell(1).setCellValue("房号：" + room.getString("floorNum")
                + "-" + room.getString("unitNum")
                + "-" + room.getString("roomNum"));
        row.createCell(2).setCellValue("业主：" + room.getString("ownerName"));
        row.createCell(3).setCellValue(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));

        row = sheet.createRow(2 + line);
        row.createCell(0).setCellValue("收费名称");
        row.createCell(1).setCellValue("收费标准");
        row.createCell(2).setCellValue("数量/面积");
        row.createCell(3).setCellValue("欠费时间");
        row.createCell(4).setCellValue("应缴金额（元）");
        row.createCell(5).setCellValue("违约金（元）");
        row.createCell(6).setCellValue("备注");

        JSONArray fees = room.getJSONArray("fees");
        BigDecimal totalPrice = new BigDecimal(0);
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            JSONObject feeObj = fees.getJSONObject(feeIndex);
            row = sheet.createRow(line + feeIndex + 3);
            row.createCell(0).setCellValue(feeObj.getString("feeName"));
            row.createCell(1).setCellValue(feeObj.getString("squarePrice"));
            row.createCell(2).setCellValue(room.getString("builtUpArea"));
            row.createCell(3).setCellValue(feeObj.getString("endTime") + "至" + feeObj.getString("deadlineTime"));
            row.createCell(4).setCellValue(room.getString("feePrice"));
            row.createCell(5).setCellValue("0");
            row.createCell(6).setCellValue("");
            totalPrice.add(new BigDecimal(feeObj.getString("squarePrice")));
        }

        row = sheet.createRow(line + fees.size() + 3);
        row.createCell(0).setCellValue("合计（大写）：");
        row.createCell(1).setCellValue(Money2ChineseUtil.toChineseChar(totalPrice.doubleValue()));
        row.createCell(2).setCellValue("");
        row.createCell(3).setCellValue("");
        row.createCell(4).setCellValue(totalPrice.doubleValue());
        row.createCell(5).setCellValue("");
        row.createCell(6).setCellValue("");

        row = sheet.createRow(line + fees.size() + 4);
        row.createCell(0).setCellValue("1、请收到通知单5日内到物业处或微信支付");
        row = sheet.createRow(fees.size() + 5);
        row.createCell(0).setCellValue("2、逾期未缴，将按规定收取违约金，会给您照成不必要的损失");

        return line + fees.size() + 4;
    }


    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsRoomFee(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/feeManualCollection/queryExportCollections?communityId=" + result.getCommunityId();
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
    private void getRoomFees(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("人工托收");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("小区名称：         ；办公电话：        ；具体交费地点：        ；\n" +
                "对公信息：开户名：              ；对公账号：               ；开户行：              ；税号：               ；\n" +
                "对接人员姓名(包括项目上的和公司财务对接人员)：          ；对接人员电话：          ；\n" +
                "可否上门收：        ；有无业委会：     ；是否属前期物业：       ；物业上班时间：             ；" +
                "物业入驻小区开始服务时间:如：2014年1月1号服务至今   ；有无涨价：     ；有无涨价公告：    ；" +
                "有无提前收取其他费用（如:水电周转金      、装修押金       、土头清运费      ）");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("序号");


        //查询楼栋信息
        JSONArray rooms = this.getExistsRoomFee(pd, componentValidateResult);
        if (rooms == null || rooms.size() < 1) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 1);
            sheet.addMergedRegion(region);
            return;
        }
        JSONObject dataObj = rooms.getJSONObject(0);
        int dataKeyIndex = 1;
        for (String key : dataObj.keySet()) {
            if (key.contains("_")) {
                row.createCell(dataKeyIndex).setCellValue(key.substring(key.indexOf("_") + 1));
            } else {
                row.createCell(dataKeyIndex).setCellValue(key);
            }
            dataKeyIndex++;
        }
        row.createCell(dataKeyIndex).setCellValue("备注");


        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 2);
            dataObj = rooms.getJSONObject(roomIndex);
            dataKeyIndex = 1;
            row.createCell(0).setCellValue(roomIndex + 1);
            for (String key : dataObj.keySet()) {
                row.createCell(dataKeyIndex).setCellValue(dataObj.getString(key));
                dataKeyIndex++;
            }
            row.createCell(dataKeyIndex + 2).setCellValue("");
        }

        row = sheet.createRow(rooms.size() + 2);
        cell0 = row.createCell(0);
        cell0.setCellValue("注：此《欠费统计表》交由厦门维度智临科技有限公司进行催收");

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, dataKeyIndex);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(rooms.size() + 2, rooms.size() + 2, 0, dataKeyIndex);
        sheet.addMergedRegion(region);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
