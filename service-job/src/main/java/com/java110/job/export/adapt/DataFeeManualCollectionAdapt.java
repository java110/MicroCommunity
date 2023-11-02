package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeePrintSpecDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.*;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 催缴单 导出处理类
 */
@Service("dataFeeManualCollection")
public class DataFeeManualCollectionAdapt implements IExportDataAdapt {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IFeePrintSpecInnerServiceSMO feePrintSpecInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    private static final int MAX_ROW = 100;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        JSONObject reqJson = exportDataDto.getReqJson();
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "configIds", "未包含费用");
        Assert.hasKeyAndValue(reqJson, "roomIds", "未包含房屋");

        String configIds = reqJson.getString("configIds");

        String roomIds = reqJson.getString("roomIds");


        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("催缴单");
        Drawing patriarch = sheet.createDrawingPatriarch();

        FeePrintSpecDto feePrintSpecDto = new FeePrintSpecDto();
        feePrintSpecDto.setCommunityId(reqJson.getString("communityId"));
        feePrintSpecDto.setSpecCd("1010");
        feePrintSpecDto.setPage(1);
        feePrintSpecDto.setRow(1);
        List<FeePrintSpecDto> feePrintSpecDtos = feePrintSpecInnerServiceSMOImpl.queryFeePrintSpecs(feePrintSpecDto);

        if (feePrintSpecDtos == null || feePrintSpecDtos.isEmpty()) {
            feePrintSpecDto = null;
        } else {
            feePrintSpecDto = feePrintSpecDtos.get(0);
        }
        int line = 0;
        double totalPageHeight = 0;

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.split(","));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.isEmpty()) {
            throw new IllegalArgumentException("未包含房屋");
        }

        for (RoomDto roomDto1 : roomDtos) {
            getTmpRoomDtos(roomDto1, configIds, reqJson);
        }
        for (int roomIndex = 0; roomIndex < roomDtos.size(); roomIndex++) {
            //todo 有可能房屋下没有欠费
            if (roomDtos.get(roomIndex).getFees() == null) {
                continue;
            }
            Map<String, Object> info = generatorRoomOweFee(sheet, workbook, roomDtos.get(roomIndex), line, totalPageHeight, patriarch, feePrintSpecDto);
            line = Integer.parseInt(info.get("line").toString()) + 1;
            totalPageHeight = Double.parseDouble(info.get("totalPageHeight").toString());
        }


        return workbook;
    }

    private Map<String, Object> generatorRoomOweFee(Sheet sheet, SXSSFWorkbook workbook, RoomDto roomDto, int line,
                                                    double totalPageHeight, Drawing patriarch, FeePrintSpecDto feePrintSpecDto
    ) {
        List<FeeDto> fees = roomDto.getFees();
        String[] feePrintRemarks = null;
        if (feePrintSpecDto != null) {
            feePrintRemarks = feePrintSpecDto.getContent().toLowerCase().replace("</br>", "").split("\n");
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
        if (feePrintSpecDto != null) {
            cell0.setCellValue(feePrintSpecDto.getPrintName() + "缴费通知单");
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
        if (feePrintSpecDto != null) {
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 0, 1 + line, (short) 1, 1 + line + 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);//设置图片随单元移动调整大小
            try {
                String qrImg = feePrintSpecDto.getQrImg().replace("data:image/webp;base64,", "")
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
            cell1.setCellValue("业主：" + fees.get(0).getOwnerName());
        } else {
            cell1.setCellValue("业主：无");
        }
        cell1.setCellStyle(subTitleCellStyle);

        Cell cell2 = row.createCell(2);
        cell2.setCellValue("房号：" + roomDto.getFloorNum()
                + "-" + roomDto.getUnitNum()
                + "-" + roomDto.getRoomNum());
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
            JSONObject feeObj = BeanConvertUtil.beanCovertJson(fees.get(feeIndex));
            row = sheet.createRow(line + feeIndex + 3);
            startTime = DateUtil.getFormatTimeStringB(feeObj.getDate("endTime"));
            endTime = DateUtil.getFormatTimeStringB(feeObj.getDate("deadlineTime"));
            //todo 如果费用是周期性费用 则 结束时间减一天
            if (feeObj.containsKey("feeFlag") && (FeeDto.FEE_FLAG_CYCLE.equals(feeObj.getString("feeFlag")) || FeeDto.FEE_FLAG_CYCLE_ONCE.equals(feeObj.getString("feeFlag")))) {
                endTime = DateUtil.getFormatTimeStringB(DateUtil.stepDay(DateUtil.getDateFromStringB(endTime), -1));
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
            cell2.setCellValue(roomDto.getBuiltUpArea());
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

    private RoomDto getTmpRoomDtos(RoomDto tmpRoomDto, String configIds, JSONObject reqJson) {
        FeeDto tmpFeeDto = null;
        tmpFeeDto = new FeeDto();
        tmpFeeDto.setArrearsEndTime(DateUtil.getCurrentDate());
        tmpFeeDto.setState(FeeDto.STATE_DOING);
        tmpFeeDto.setPayerObjId(tmpRoomDto.getRoomId());
        tmpFeeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        tmpFeeDto.setConfigIds(configIds.split(","));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.querySimpleFees(tmpFeeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return null;
        }

        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        for (FeeDto tempFeeDto : feeDtos) {

            computeFeeSMOImpl.computeEveryOweFee(tempFeeDto, tmpRoomDto);//计算欠费金额
            //如果金额为0 就排除
            //if (tempFeeDto.getFeePrice() > 0 && tempFeeDto.getEndTime().getTime() <= DateUtil.getCurrentDate().getTime()) {
            // todo  校验 时间范围
            if (!hasInTime(tempFeeDto, reqJson)) {
                continue;
            }
            if (tempFeeDto.getFeePrice() != 0) {
                tmpFeeDtos.add(tempFeeDto);
            }
        }

        if (tmpFeeDtos.size() < 1) {
            return null;
        }
        tmpRoomDto.setFees(tmpFeeDtos);
        return tmpRoomDto;
    }

    private boolean hasInTime(FeeDto tempFeeDto, JSONObject reqJson) {
        if (!reqJson.containsKey("startTime") || !reqJson.containsKey("endTime")) {
            return true;
        }

        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");

        if (StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime)) {
            return true;
        }
        if (tempFeeDto.getDeadlineTime() == null) {
            return true;
        }

        if (tempFeeDto.getEndTime().before(DateUtil.getDateFromStringB(startTime))
                && tempFeeDto.getDeadlineTime().after(DateUtil.getDateFromStringB(endTime))
        ) {
            return true;
        }

        return false;
    }
}
