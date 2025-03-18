package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.ListUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("exportCreateFeeTemplate")
public class ExportCreateFeeTemplateAdapt implements IExportDataAdapt {

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
        if (TYPE_ROOM.equals(reqJson.getString("type"))) {
            getRoomAndConfigs(reqJson, workbook);
        } else if (TYPE_PARKSPACE.equals(reqJson.getString("type"))) {
            getParkspaceAndConfigs(reqJson, workbook);
        }

        return workbook;
    }

    private void getRoomAndConfigs(JSONObject paramIn, SXSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("创建费用");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n计费起始时间: " +
                "计费起始时间，格式为YYYY-MM-DD；\n计费结束时间，格式为YYYY-MM-DD；\n" +
                "建账时间: 建账时间，格式为YYYY-MM-DD； \n 类型：表明是合同 房屋 还是车辆 房屋 1001 车辆 2002 合同 3003" +
                "\n注意：所有单元格式为文本，计费结束时间只有一次性费用和间接性费用时需要填写");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("房号");
        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("费用项ID");
        row.createCell(3).setCellValue("收费项目");
        row.createCell(4).setCellValue("建账时间");
        row.createCell(5).setCellValue("计费起始时间");
        row.createCell(6).setCellValue("计费结束时间");
        row.createCell(7).setCellValue("房屋状态");


        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(paramIn.getString("communityId"));
        roomDto.setFloorIds(paramIn.getString("floorIds").split(","));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (ListUtil.isNull(roomDtos)) {
            return;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(paramIn.getString("configIds").split(","));
        feeConfigDto.setCommunityId(paramIn.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (ListUtil.isNull(feeConfigDtos)) {
            return;
        }

        int roomIndex = 2;
        for (RoomDto tmpRoomDto : roomDtos) {
            for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
                row = sheet.createRow(roomIndex);
                row.createCell(0).setCellValue(tmpRoomDto.getFloorNum() + "-" + tmpRoomDto.getUnitNum() + "-" + tmpRoomDto.getRoomNum());
                row.createCell(1).setCellValue("1001");
                row.createCell(2).setCellValue(tmpFeeConfigDto.getConfigId());
                row.createCell(3).setCellValue(tmpFeeConfigDto.getFeeName());
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
                row.createCell(7).setCellValue(tmpRoomDto.getStateName());
                roomIndex += 1;
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(region);
    }

    private void getParkspaceAndConfigs(JSONObject paramIn, SXSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("创建费用");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n计费起始时间: " +
                "计费起始时间，格式为YYYY-MM-DD；\n计费结束时间，格式为YYYY-MM-DD；\n" +
                "建账时间: 建账时间，格式为YYYY-MM-DD； \n 类型：表明是合同 房屋 还是车辆 房屋 1001 车辆 2002 合同 3003" +
                "\n注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("车牌号");
        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("费用项ID");
        row.createCell(3).setCellValue("收费项目");
        row.createCell(4).setCellValue("建账时间");
        row.createCell(5).setCellValue("计费起始时间");
        row.createCell(6).setCellValue("计费结束时间");

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramIn.getString("communityId"));
        parkingSpaceDto.setPaIds(paramIn.getString("paIds").split(","));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (ListUtil.isNull(parkingSpaceDtos)) {
            return;
        }

        //查询车牌号
        List<OwnerCarDto> ownerCarDtos = getOwnerCars(parkingSpaceDtos);
        if (ListUtil.isNull(ownerCarDtos) ) {
            return;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(paramIn.getString("configIds").split(","));
        feeConfigDto.setCommunityId(paramIn.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (ListUtil.isNull(feeConfigDtos) ) {
            return;
        }

        int roomIndex = 2;
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
                row = sheet.createRow(roomIndex);
                row.createCell(0).setCellValue(tmpOwnerCarDto.getCarNum());
                row.createCell(1).setCellValue("2002");
                row.createCell(2).setCellValue(tmpFeeConfigDto.getConfigId());
                row.createCell(3).setCellValue(tmpFeeConfigDto.getFeeName());
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                roomIndex += 1;
            }
        }

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(region);
    }

    private List<OwnerCarDto> getOwnerCars(List<ParkingSpaceDto> parkingSpaceDtos) {
        List<String> psIds = new ArrayList<>();
        List<OwnerCarDto> tmpOwnerCarDtos = new ArrayList<>();
        for (int roomIndex = 0; roomIndex < parkingSpaceDtos.size(); roomIndex++) {
            psIds.add(parkingSpaceDtos.get(roomIndex).getPsId());
            if (roomIndex % DEFAULT_ROW == 0 && roomIndex != 0) {
                // 处理房屋费用
                OwnerCarDto ownerCarDto = new OwnerCarDto();
                ownerCarDto.setPsIds(psIds.toArray(new String[psIds.size()]));
                ownerCarDto.setCommunityId(parkingSpaceDtos.get(roomIndex).getCommunityId());
                tmpOwnerCarDtos.addAll(ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto));

                psIds = new ArrayList<>();
            }
        }
        if (!ListUtil.isNull(psIds)) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setPsIds(psIds.toArray(new String[psIds.size()]));
            ownerCarDto.setCommunityId(parkingSpaceDtos.get(0).getCommunityId());
            tmpOwnerCarDtos.addAll(ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto));
        }
        return tmpOwnerCarDtos;

    }
}
