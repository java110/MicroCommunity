package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.meter.ImportExportMeterWaterDto;
import com.java110.dto.meter.MeterWaterDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 水电费导出
 */
@Service("exportMeterWater2")
public class ExportMeterWater2Adapt implements IExportDataAdapt {

    private static final int MAX_ROW = 200;

    @Autowired
    private IMeterWaterInnerServiceSMO meterWaterInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        JSONObject reqJson = exportDataDto.getReqJson();

        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区");
        Assert.hasKeyAndValue(reqJson, "meterType", "请求中未包含费用项目");

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("房屋费用信息");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("上期度数: 请填写上期表读数 ；\n上期读表时间: " +
                "格式为YYYY-MM-DD； \n本期度数: 本次表读数；\n本期读表时间: 格式为YYYY-MM-DD； " +
                "\n注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("楼栋编号");
        row.createCell(1).setCellValue("单元编号");
        row.createCell(2).setCellValue("房屋编码");
        row.createCell(3).setCellValue("费用类型");
        row.createCell(4).setCellValue("单价");
        row.createCell(5).setCellValue("上期度数");
        row.createCell(6).setCellValue("上期读表时间");
        row.createCell(7).setCellValue("本期度数");
        row.createCell(8).setCellValue("本期读表时间");
        row.createCell(9).setCellValue("备注");


        List<ImportExportMeterWaterDto> importExportMeterWaterDtos
                = queryExportRoomAndMeterWater(reqJson.getString("communityId"), reqJson.getString("meterType"));


        if (importExportMeterWaterDtos == null) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
            sheet.addMergedRegion(region);
            return workbook;
        }

        for (int roomIndex = 0; roomIndex < importExportMeterWaterDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 2);
            row.createCell(0).setCellValue(importExportMeterWaterDtos.get(roomIndex).getFloorNum());
            row.createCell(1).setCellValue(importExportMeterWaterDtos.get(roomIndex).getUnitNum());
            row.createCell(2).setCellValue(importExportMeterWaterDtos.get(roomIndex).getRoomNum());
            row.createCell(3).setCellValue(reqJson.getString("feeName"));
            row.createCell(4).setCellValue(importExportMeterWaterDtos.get(roomIndex).getPrice());
            row.createCell(5).setCellValue(importExportMeterWaterDtos.get(roomIndex).getPreDegrees());
            row.createCell(6).setCellValue(importExportMeterWaterDtos.get(roomIndex).getPreReadingTime());
            row.createCell(7).setCellValue("");
            row.createCell(8).setCellValue("");
            row.createCell(9).setCellValue("");
        }

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
        sheet.addMergedRegion(region);
        return workbook;
    }

    private void appendData(List<OwnerCarDto> ownerCarDtoList, Sheet sheet, int step) {
        Row row = null;
        OwnerCarDto dataObj = null;
        for (int roomIndex = 0; roomIndex < ownerCarDtoList.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = ownerCarDtoList.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.getCarNum());
            row.createCell(1).setCellValue(dataObj.getRoomName());
            row.createCell(2).setCellValue(dataObj.getCarTypeName());
            row.createCell(3).setCellValue(dataObj.getCarColor());
            row.createCell(4).setCellValue(dataObj.getOwnerName());
            row.createCell(5).setCellValue(dataObj.getLink());
            row.createCell(6).setCellValue(dataObj.getAreaNum() + "-" + dataObj.getNum());
            row.createCell(7).setCellValue(DateUtil.getFormatTimeStringA(dataObj.getStartTime()));
            row.createCell(8).setCellValue(DateUtil.getFormatTimeStringA(dataObj.getEndTime()));
        }

    }

    public List<ImportExportMeterWaterDto> queryExportRoomAndMeterWater(String communityId, String meterType) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        MeterWaterDto meterWaterDto = null;
        List<ImportExportMeterWaterDto> importExportMeterWaterDtos = new ArrayList<>();
        ImportExportMeterWaterDto importExportMeterWaterDto = null;

        for (RoomDto tmpRoomDto : roomDtos) {
            meterWaterDto = new MeterWaterDto();
            meterWaterDto.setMeterType(meterType);
            meterWaterDto.setObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
            meterWaterDto.setObjId(tmpRoomDto.getRoomId());
            meterWaterDto.setCommunityId(communityId);
            List<MeterWaterDto> meterWaterDtos = meterWaterInnerServiceSMOImpl.queryMeterWaters(meterWaterDto);
            importExportMeterWaterDto = BeanConvertUtil.covertBean(tmpRoomDto, ImportExportMeterWaterDto.class);
            String preDegree = "0";
            String preReadTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B);
            double price = 0;
            if (meterWaterDtos != null && meterWaterDtos.size() > 0) {
                preDegree = meterWaterDtos.get(0).getCurDegrees();
                preReadTime = DateUtil.dateTimeToDate(meterWaterDtos.get(0).getCurReadingTime());
                price = meterWaterDtos.get(0).getPrice();
            }
            importExportMeterWaterDto.setPreDegrees(preDegree);
            importExportMeterWaterDto.setPreReadingTime(preReadTime);
            importExportMeterWaterDto.setPrice(price);
            importExportMeterWaterDtos.add(importExportMeterWaterDto);
        }
        return importExportMeterWaterDtos;
    }
}
