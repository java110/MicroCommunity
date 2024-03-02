package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 未收费房屋导出
 *
 * @author fqz
 * @date 2024-01-29 16:19
 */
@Service("reportNoFeeRoom")
public class ReportNoFeeRoomAdapt implements IExportDataAdapt {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("未收费房屋");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("楼栋");
        row.createCell(2).setCellValue("单元");
        row.createCell(3).setCellValue("房屋");
        row.createCell(4).setCellValue("业主名称");
        row.createCell(5).setCellValue("业主电话");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getReportNoFeeRoom(sheet, reqJson);
        return workbook;
    }

    private void getReportNoFeeRoom(Sheet sheet, JSONObject reqJson) {
        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        roomDto.setPage(1);
        roomDto.setRow(MAX_ROW);
        List<RoomDto> roomList = reportFeeMonthStatisticsInnerServiceSMOImpl.queryNoFeeRooms(roomDto);
        appendData(roomList, sheet);
    }

    private void appendData(List<RoomDto> roomList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < roomList.size(); index++) {
            row = sheet.createRow(index + 1);
            RoomDto roomDto = roomList.get(index);
            row.createCell(0).setCellValue(index + 1);
            row.createCell(1).setCellValue(roomDto.getFloorNum());
            row.createCell(2).setCellValue(roomDto.getUnitNum());
            row.createCell(3).setCellValue(roomDto.getRoomNum());
            row.createCell(4).setCellValue(roomDto.getOwnerName());
            row.createCell(5).setCellValue(roomDto.getLink());
        }
    }
}
