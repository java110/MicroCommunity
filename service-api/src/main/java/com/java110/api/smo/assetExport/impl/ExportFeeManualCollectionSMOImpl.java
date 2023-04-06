package com.java110.api.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetExport.IExportFeeManualCollectionSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.Money2ChineseUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("exportFeeManualCollectionSMOImpl")
public class ExportFeeManualCollectionSMOImpl extends DefaultAbstractComponentSMO implements IExportFeeManualCollectionSMO {
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

        } catch (IOException e) {
            e.printStackTrace();
            // 保存数据
            return new ResponseEntity<Object>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        Sheet sheet = workbook.createSheet("催缴单");
        Drawing patriarch = sheet.createDrawingPatriarch();
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        apiUrl = "/feeApi/listAllRoomOweFees" + mapToUrlParam(reqJson);
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

        //查询催缴单二维码
        JSONObject feePrint = null;
        apiUrl = "/feePrintSpec/queryFeePrintSpec?page=1&row=1&specCd=1010&communityId=" + result.getCommunityId();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject feePrintResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
            if (feePrintResults.containsKey("data")) {
                JSONArray feePrints = feePrintResults.getJSONArray("data");
                if (feePrints != null && feePrints.size() > 0) {
                    feePrint = feePrints.getJSONObject(0);
                }
            }

        }


        int line = 0;
        double totalPageHeight = 0;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            Map<String, Object> info = generatorRoomOweFee(sheet, workbook, rooms.getJSONObject(roomIndex), line, totalPageHeight, patriarch, feePrint);
            line = Integer.parseInt(info.get("line").toString()) + 1;
            totalPageHeight = Double.parseDouble(info.get("totalPageHeight").toString());
        }
    }

    private Map<String, Object> generatorRoomOweFee(Sheet sheet, Workbook workbook, JSONObject room, int line, double totalPageHeight, Drawing patriarch, JSONObject feePrint) {
        JSONArray fees = room.getJSONArray("fees");
        String[] feePrintRemarks = null;
        if (feePrint != null) {
            feePrintRemarks = feePrint.getString("content").toLowerCase().replace("</br>", "").split("\n");
        } else {
            feePrintRemarks = new String[]{""};
        }
        int defaultRowHeight = 280;
        //计算当前单子的高度
        int titleHeight = defaultRowHeight * 3;
        int subTitleHeight = defaultRowHeight * 5;
        int totalHeight = titleHeight + subTitleHeight + defaultRowHeight * 3 + fees.size() * defaultRowHeight;
        //备注，加上打印配置内容
        if (feePrintRemarks != null && feePrintRemarks.length > 0) {
            totalHeight += (feePrintRemarks.length * defaultRowHeight);
        }
        double A4_lengthways_pageSize = defaultRowHeight * 57;//15960

        //当前页 已经占用的高度
        double curPageHeight = totalPageHeight % A4_lengthways_pageSize;
        //当前页空闲高度
        double freePageHeight = A4_lengthways_pageSize - curPageHeight;
        if (freePageHeight < totalHeight && curPageHeight != 0) {
            line += Math.ceil(freePageHeight / defaultRowHeight);
            totalPageHeight += freePageHeight;
        }

        totalPageHeight += totalHeight;


        sheet.setColumnWidth(0, 8 * 256 * 2);
        sheet.setColumnWidth(1, 8 * 256 * 1);
        sheet.setColumnWidth(2, 8 * 256 * 1);
        sheet.setColumnWidth(3, 8 * 256 * 3);
        sheet.setColumnWidth(4, 8 * 256 * 1);
        sheet.setColumnWidth(5, 8 * 256 * 1);
        sheet.setColumnWidth(6, 8 * 256 * 1);

        //通用样式
        CellStyle cellStyle = workbook.createCellStyle();
//设置样式对象，这里仅设置了边框属性
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        // 标题
        Row row = sheet.createRow(0 + line);
        Cell cell0 = row.createCell(0);
        //cell0.setCellValue("缴费通知单totalHeight:" + totalHeight + "-totalPageHeight:" + totalPageHeight + "-curPageHeight:" + curPageHeight + "-freePageHeight:" + freePageHeight + "-line:" + line);
        if (feePrint != null) {
            cell0.setCellValue(feePrint.getString("printName") + "缴费通知单");
        } else {
            cell0.setCellValue("缴费通知单");
        }
        //标题设置字体
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 26);
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(font);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell0.setCellStyle(titleCellStyle);
        row.setHeight((short) (titleHeight));

        //合并标题
        CellRangeAddress region = new CellRangeAddress(0 + line, 0 + line, 0, 6);
        sheet.addMergedRegion(region);


        //子标题
        if (feePrint != null) {
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 0, 1 + line, (short) 1, 1 + line + 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);//设置图片随单元移动调整大小
            try {
                String qrImg = feePrint.getString("qrImg").replace("data:image/webp;base64,", "")
                        .replace("data:image/png;base64,", "")
                        .replace("data:image/jpeg;base64,", "");
                patriarch.createPicture(anchor, workbook.addPicture(Base64Convert.base64ToByte(qrImg), XSSFWorkbook.PICTURE_TYPE_JPEG));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CellStyle subTitleCellStyle = workbook.createCellStyle();
        subTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        subTitleCellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        row = sheet.createRow(1 + line);
        Cell cell1 = row.createCell(1);
        if (fees != null && fees.size() > 0) {
            cell1.setCellValue("业主：" + fees.getJSONObject(0).getString("ownerName"));
        } else {
            cell1.setCellValue("业主：无");
        }
        cell1.setCellStyle(subTitleCellStyle);

        Cell cell2 = row.createCell(2);
        cell2.setCellValue("房号：" + room.getString("floorNum")
                + "-" + room.getString("unitNum")
                + "-" + room.getString("roomNum"));
        cell2.setCellStyle(subTitleCellStyle);

        row.createCell(3).setCellValue("");
        row.createCell(4).setCellValue("");
        Cell cell5 = row.createCell(5);
        cell5.setCellValue(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        cell5.setCellStyle(subTitleCellStyle);
        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        row.setRowStyle(rowCellStyle);

        //设置表头之上
        region = new CellRangeAddress(1 + line, 1 + line, 2, 3);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(1 + line, 1 + line, 5, 6);
        sheet.addMergedRegion(region);
        //子标题高度
        row.setHeight((short) (subTitleHeight));


        row = sheet.createRow(2 + line);
        cell0 = row.createCell(0);
        cell0.setCellValue("收费名称");
        cell0.setCellStyle(cellStyle);
        cell1 = row.createCell(1);
        cell1.setCellValue("收费标准");
        cell1.setCellStyle(cellStyle);
        cell2 = row.createCell(2);
        cell2.setCellValue("数量/面积");
        cell2.setCellStyle(cellStyle);
        Cell cell3 = row.createCell(3);
        cell3.setCellValue("欠费时间");
        cell3.setCellStyle(cellStyle);
        Cell cell4 = row.createCell(4);
        cell4.setCellValue("应缴金额（元）");
        cell4.setCellStyle(cellStyle);
        cell5 = row.createCell(5);
        cell5.setCellValue("违约金（元）");
        cell5.setCellStyle(cellStyle);
        Cell cell6 = row.createCell(6);
        cell6.setCellValue("备注");
        cell6.setCellStyle(cellStyle);
        row.setHeight((short) (defaultRowHeight));


        BigDecimal totalPrice = new BigDecimal(0);
        String startTime = "";
        String endTime = "";
        String squarePrice = "";
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            JSONObject feeObj = fees.getJSONObject(feeIndex);
            row = sheet.createRow(line + feeIndex + 3);
            startTime = feeObj.getString("endTime").length() > 10 ? feeObj.getString("endTime").substring(0, 10) : feeObj.getString("endTime");
            endTime = feeObj.getString("deadlineTime").length() > 10 ? feeObj.getString("deadlineTime").substring(0, 10) : feeObj.getString("deadlineTime");
            //如果费用是周期性费用 则 结束时间减一天
            try {
                if (feeObj.containsKey("feeFlag") && (FeeDto.FEE_FLAG_CYCLE.equals(feeObj.getString("feeFlag")) || FeeDto.FEE_FLAG_CYCLE_ONCE.equals(feeObj.getString("feeFlag")))) {
                    endTime = DateUtil.getFormatTimeString(DateUtil.stepDay(DateUtil.getDateFromString(endTime, DateUtil.DATE_FORMATE_STRING_B), -1),
                            DateUtil.DATE_FORMATE_STRING_B);
                }
            } catch (ParseException e) {
                logger.error("处理结束时间失败", e);
            }
            squarePrice = feeObj.getString("squarePrice");

            //动态费用单价就去动态单价
            if (feeObj.containsKey("computingFormula") && "9009".equals(feeObj.getString("computingFormula"))) {
                squarePrice = feeObj.getString("mwPrice");
            }

            cell0 = row.createCell(0);
            cell0.setCellValue(feeObj.getString("feeName"));
            cell0.setCellStyle(cellStyle);
            cell1 = row.createCell(1);
            cell1.setCellValue(squarePrice);
            cell1.setCellStyle(cellStyle);
            cell2 = row.createCell(2);
            if (feeObj.containsKey("curDegrees")) {
                double totalDegrees = feeObj.getDouble("curDegrees") - feeObj.getDouble("preDegrees");
                BigDecimal degreesDec = new BigDecimal(totalDegrees).setScale(2, BigDecimal.ROUND_HALF_UP);
                cell2.setCellValue(degreesDec.doubleValue());
            } else {
                cell2.setCellValue(room.getString("builtUpArea"));
            }
            cell2.setCellStyle(cellStyle);
            cell3 = row.createCell(3);
            if (feeObj.containsKey("curDegrees")) {
                cell3.setCellValue(startTime + "至" + endTime + " " + feeObj.getString("preDegrees") + "至" + feeObj.getString("curDegrees"));
            } else {
                cell3.setCellValue(startTime + "至" + endTime);
            }
            cell3.setCellStyle(cellStyle);
            cell4 = row.createCell(4);
            cell4.setCellValue(feeObj.getString("feeTotalPrice"));
            cell4.setCellStyle(cellStyle);
            cell5 = row.createCell(5);
            cell5.setCellValue("0");
            cell5.setCellStyle(cellStyle);
            cell6 = row.createCell(6);
            cell6.setCellValue("");
            cell6.setCellStyle(cellStyle);
            row.setHeight((short) (defaultRowHeight));
            totalPrice = totalPrice.add(new BigDecimal(feeObj.getString("feeTotalPrice")));
        }

        row = sheet.createRow(line + fees.size() + 3);

        cell0 = row.createCell(0);
        cell0.setCellValue("合计（大写）");
        cell0.setCellStyle(cellStyle);
        cell1 = row.createCell(1);
        cell1.setCellValue(Money2ChineseUtil.toChineseChar(totalPrice.doubleValue()));
        cell1.setCellStyle(cellStyle);
        cell2 = row.createCell(2);
        cell2.setCellValue("");
        cell2.setCellStyle(cellStyle);
        cell3 = row.createCell(3);
        cell3.setCellValue("");
        cell3.setCellStyle(cellStyle);
        cell4 = row.createCell(4);
        cell4.setCellValue(totalPrice.doubleValue());
        cell4.setCellStyle(cellStyle);
        cell5 = row.createCell(5);
        cell5.setCellValue("");
        cell5.setCellStyle(cellStyle);
        cell6 = row.createCell(6);
        cell6.setCellValue("");
        cell6.setCellStyle(cellStyle);
        row.setHeight((short) (defaultRowHeight));


        //合计 合并
        region = new CellRangeAddress(line + fees.size() + 3, line + fees.size() + 3, 1, 3);
        sheet.addMergedRegion(region);

        if (feePrintRemarks != null && feePrintRemarks.length > 0) {
            for (int remarkIndex = 0; remarkIndex < feePrintRemarks.length; remarkIndex++) {
                row = sheet.createRow(line + fees.size() + 4 + remarkIndex);
                row.createCell(0).setCellValue(feePrintRemarks[remarkIndex]);
                row.setHeight((short) (defaultRowHeight));
            }
        }
        row = sheet.createRow(line + fees.size() + 4 + feePrintRemarks.length);
        row.createCell(0).setCellValue("");
        row.setHeight((short) (defaultRowHeight));
        Map info = new HashMap();
        info.put("line", line + fees.size() + 4 + feePrintRemarks.length);
        info.put("totalPageHeight", totalPageHeight);
        return info;
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
        apiUrl = "/feeManualCollection/queryExportCollections?communityId=" + result.getCommunityId();
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
