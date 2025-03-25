package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.ListUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 费用导入
 */
@Service("exportFeeImportTemplate")
public class ExportFeeImportTemplateAdapt implements IExportDataAdapt {

    public static final String TYPE_ROOM = "1001";
    public static final String TYPE_PARKSPACE = "2002";
    public static final String TYPE_CONTRACT = "3003"; //合同

    public static final int DEFAULT_ROW = 500;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        JSONObject reqJson = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();

        //查询资产和费用项
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reqJson.getString("objType"))) {
            //获取楼信息
            getRooms(reqJson, workbook);
        } else {
            getCars(reqJson, workbook);
        }

        return workbook;
    }

    private void getRooms(JSONObject paramIn, SXSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("房屋费用信息");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("房屋编号: 楼栋-单元-房号 ；\n费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n开始时间: " +
                "收费开始时间，格式为YYYY-MM-DD；\n结束时间: 费用结束时间，格式为YYYY-MM-DD； \n收费金额: 本次收取金额 单位元； " +
                "\n注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("房屋编码");
        row.createCell(1).setCellValue("费用名称");
        row.createCell(2).setCellValue("开始时间");
        row.createCell(3).setCellValue("结束时间");
        row.createCell(4).setCellValue("收费金额");

        RoomDto roomDto = new RoomDto();
        roomDto.setPage(1);
        roomDto.setRow(10000);
        roomDto.setCommunityId(paramIn.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        //查询楼栋信息
        if (ListUtil.isNull(roomDtos)) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
            sheet.addMergedRegion(region);
            return;
        }
        String roomName = "";
        for (int roomIndex = 0; roomIndex < roomDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 2);
            roomName = roomDtos.get(roomIndex).getFloorNum()
                    + "-" + roomDtos.get(roomIndex).getUnitNum()
                    + "-" + roomDtos.get(roomIndex).getRoomNum();
            row.createCell(0).setCellValue(roomName);
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
        }

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
        sheet.addMergedRegion(region);
    }

    private void getCars(JSONObject paramIn, SXSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("车位费用信息");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如停车费等 ；\n开始时间: " +
                "收费开始时间，格式为YYYY-MM-DD；\n结束时间: 费用结束时间，格式为YYYY-MM-DD； \n收费金额: 本次收取金额 单位元； " +
                "\n注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("车牌号");
        row.createCell(1).setCellValue("费用名称");
        row.createCell(2).setCellValue("开始时间");
        row.createCell(3).setCellValue("结束时间");
        row.createCell(4).setCellValue("收费金额");

        //查询楼栋信息
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setPage(1);
        ownerCarDto.setRow(10000);
        ownerCarDto.setCommunityId(paramIn.getString("communityId"));
       List<OwnerCarDto> cars = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ListUtil.isNull(cars)) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
            sheet.addMergedRegion(region);
            return;
        }
        for (int carIndex = 0; carIndex < cars.size(); carIndex++) {
            row = sheet.createRow(carIndex + 2);
            row.createCell(0).setCellValue(cars.get(carIndex).getCarNum());
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
        }

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(region);
    }

}
