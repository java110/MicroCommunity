package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业主车辆导出
 *
 * @date 2023-09-08
 */
@Service("exportOwnerCar")
public class ExportOwnerCarAdapt implements IExportDataAdapt {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("业主车辆");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("车牌号");
        row.createCell(1).setCellValue("成员车辆");
        row.createCell(2).setCellValue("房屋号");
        row.createCell(3).setCellValue("车辆品牌");
        row.createCell(4).setCellValue("车辆类型");
        row.createCell(5).setCellValue("颜色");
        row.createCell(6).setCellValue("业主");
        row.createCell(7).setCellValue("车位");
        row.createCell(8).setCellValue("有效期");
        row.createCell(9).setCellValue("状态");
        row.createCell(10).setCellValue("备注");
        JSONObject reqJson = exportDataDto.getReqJson();
        if (reqJson.containsKey("num") && !StringUtil.isEmpty(reqJson.getString("num"))) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setAreaNum(reqJson.getString("areaNum"));
            parkingSpaceDto.setNum(reqJson.getString("num"));
            parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos != null && parkingSpaceDtos.size() > 0) {
                reqJson.put("psId", parkingSpaceDtos.get(0).getPsId());
            }
        }
        OwnerCarDto ownerCarDto = BeanConvertUtil.covertBean(reqJson, OwnerCarDto.class);
        if (reqJson.containsKey("carTypeCds")) {
            ownerCarDto.setCarTypeCd("");
            ownerCarDto.setCarTypeCds(reqJson.getString("carTypeCds").split(","));
        }
        //查询总记录数
        int total = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        ownerCarDto.setRow(MAX_ROW);
        int maxPage = (int) Math.ceil((double) total / (double) MAX_ROW);
        List<OwnerCarDto> ownerCarDtoList = null;
        for (int page = 1; page <= maxPage; page++) {
            ownerCarDto.setPage(page);
            ownerCarDto.setRow(MAX_ROW);
            ownerCarDtoList = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            freshPs(ownerCarDtoList);
            freshRoomInfo(ownerCarDtoList);
            appendData(ownerCarDtoList, sheet, (page - 1) * MAX_ROW);
        }
        return workbook;
    }

    private void appendData(List<OwnerCarDto> ownerCarDtoList, Sheet sheet, int step) {
        Row row = null;
        OwnerCarDto dataObj = null;
        for (int roomIndex = 0; roomIndex < ownerCarDtoList.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = ownerCarDtoList.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.getCarNum());
            if (!StringUtil.isEmpty(dataObj.getMemberCarCount())) {
                row.createCell(1).setCellValue(dataObj.getMemberCarCount());
            } else {
                row.createCell(1).setCellValue("0");
            }
            row.createCell(2).setCellValue(dataObj.getRoomName());
            row.createCell(3).setCellValue(dataObj.getCarBrand());
            row.createCell(4).setCellValue(dataObj.getCarTypeName());
            row.createCell(5).setCellValue(dataObj.getCarColor());
            row.createCell(6).setCellValue(dataObj.getOwnerName() + "(" + dataObj.getLink() + ")");
            if (!StringUtil.isEmpty(dataObj.getAreaNum()) && dataObj.getState().equals("1001")) {
                row.createCell(7).setCellValue(dataObj.getAreaNum() + "-" + dataObj.getNum());
            } else {
                row.createCell(7).setCellValue("车位已释放");
            }
            if (!StringUtil.isEmpty(dataObj.getLeaseType()) && dataObj.getLeaseType().equals("H")) { //H 月租车；S出售车；I 内部车；NM 免费车；R 预约车
                row.createCell(8).setCellValue(DateUtil.getFormatTimeStringA(dataObj.getStartTime()) + "~" + DateUtil.getFormatTimeStringA(dataObj.getEndTime()));
            } else {
                row.createCell(8).setCellValue("--");
            }
            //结束时间
            Date endTime = dataObj.getEndTime();
            Date date = new Date();
            if (!StringUtil.isEmpty(dataObj.getState()) && dataObj.getState().equals("3003")) { //1001 正常；2002 欠费；3003 车位释放
                row.createCell(9).setCellValue("到期");
            } else if (endTime.getTime() > date.getTime()) {
                row.createCell(9).setCellValue("正常");
            } else {
                row.createCell(9).setCellValue("到期");
            }
            row.createCell(10).setCellValue(dataObj.getRemark());
        }
    }

    private void freshPs(List<OwnerCarDto> ownerCarDtoList) {
        if (ownerCarDtoList == null || ownerCarDtoList.size() < 1) {
            return;
        }
        List<String> psIds = new ArrayList<>();
        for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
            if (StringUtil.isEmpty(ownerCarDto.getPsId())) {
                continue;
            }
            psIds.add(ownerCarDto.getPsId());
        }
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(ownerCarDtoList.get(0).getCommunityId());
        parkingSpaceDto.setPsIds(psIds.toArray(new String[psIds.size()]));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
            for (OwnerCarDto ownerCarDto : ownerCarDtoList) {
                if (tmpParkingSpaceDto.getPsId().equals(ownerCarDto.getPsId())) {
                    ownerCarDto.setAreaNum(tmpParkingSpaceDto.getAreaNum());
                    ownerCarDto.setNum(tmpParkingSpaceDto.getNum());
                    ownerCarDto.setParkingType(tmpParkingSpaceDto.getParkingType());
                }
            }
        }
    }

    /**
     * 刷入房屋信息
     *
     * @param ownerCarDtos
     */
    private void freshRoomInfo(List<OwnerCarDto> ownerCarDtos) {
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            doFreshRoomInfo(ownerCarDto);
        }
    }

    /**
     * 车位信息刷入房屋信息
     *
     * @param ownerCarDto
     */
    private void doFreshRoomInfo(OwnerCarDto ownerCarDto) {
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerCarDto.getOwnerId());
        ownerRoomRelDto.setPage(1);
        ownerRoomRelDto.setRow(3); //只展示3个房屋以内 不然页面太乱
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            ownerCarDto.setRoomName("-");
            return;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tOwnerRoomRelDto.getRoomId());
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(ownerCarDto.getCommunityId());
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        String roomName = "";
        for (RoomDto tRoomDto : roomDtos) {
            roomName += (tRoomDto.getFloorNum() + "栋" + tRoomDto.getUnitNum() + "单元" + tRoomDto.getRoomNum() + "室" + "/");
        }
        roomName = roomName.endsWith("/") ? roomName.substring(0, roomName.length() - 1) : roomName;
        ownerCarDto.setRoomName(roomName);
    }
}
