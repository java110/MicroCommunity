package com.java110.api.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetExport.IExportRoomSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("exportRoomSMOImpl")
public class ExportRoomSMOImpl extends DefaultAbstractComponentSMO implements IExportRoomSMO {
    private final static Logger logger = LoggerFactory.getLogger(ExportRoomSMOImpl.class);

    public static final String TYPE_ROOM = "1001";
    public static final String TYPE_PARKSPACE = "2002";
    public static final String TYPE_CONTRACT = "3003"; //合同

    public static final int DEFAULT_ROW = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception {

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "communityId", "请求中未包含小区");
        Assert.hasKeyAndValue(paramIn, "objType", "请求中未包含费用对象");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();

        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(paramIn.getString("objType"))) {
            //获取楼信息
            getRooms(pd, result, workbook);
        } else {
            getCars(pd, result, workbook);
        }


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=feeImport_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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
     * 导出房产信息
     *
     * @param pd 前台数据封装
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<Object> exportRoomExcelData(IPageData pd) throws Exception {
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("storeId",result.getStoreId());
        Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");
        //Assert.hasKeyAndValue(paramIn, "floorIds", "请求中未包含楼栋");
        Assert.hasKeyAndValue(paramIn, "configIds", "请求中未包含费用项");
        Assert.hasKeyAndValue(paramIn, "type", "请求中未包含类型");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();

        //查询资产和费用项
        if (TYPE_ROOM.equals(paramIn.getString("type"))) {
            getRoomAndConfigs(paramIn, workbook);
        } else if (TYPE_PARKSPACE.equals(paramIn.getString("type"))) {
            getParkspaceAndConfigs(paramIn, workbook);
        }


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=customImport_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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


    private void getParkspaceAndConfigs(JSONObject paramIn, Workbook workbook) {
        Sheet sheet = workbook.createSheet("创建费用");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n计费起始时间: " +
                "计费起始时间时间，格式为YYYY-MM-DD；\n建账时间: 建账时间，格式为YYYY-MM-DD； \n 类型：表明是合同 房屋 还是车辆 房屋 1001 车辆 2002 合同 3003" +
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

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(paramIn.getString("communityId"));
        parkingSpaceDto.setPaIds(paramIn.getString("paIds").split(","));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
            return;
        }

        //查询车牌号
        List<OwnerCarDto> ownerCarDtos = getOwnerCars(parkingSpaceDtos);
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(paramIn.getString("configIds").split(","));
        feeConfigDto.setCommunityId(paramIn.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
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
        if (psIds != null && psIds.size() > 0) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setPsIds(psIds.toArray(new String[psIds.size()]));
            ownerCarDto.setCommunityId(parkingSpaceDtos.get(0).getCommunityId());
            tmpOwnerCarDtos.addAll(ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto));
        }
        return tmpOwnerCarDtos;

    }

    private void getRoomAndConfigs(JSONObject paramIn, Workbook workbook) {
        Sheet sheet = workbook.createSheet("创建费用");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n计费起始时间: " +
                "计费起始时间时间，格式为YYYY-MM-DD；\n建账时间: 建账时间，格式为YYYY-MM-DD； \n 类型：表明是合同 房屋 还是车辆 房屋 1001 车辆 2002 合同 3003" +
                "\n注意：所有单元格式为文本");
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
        row.createCell(6).setCellValue("房屋状态");


        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(paramIn.getString("communityId"));
        roomDto.setFloorIds(paramIn.getString("floorIds").split(","));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            return;
        }
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigIds(paramIn.getString("configIds").split(","));
        feeConfigDto.setCommunityId(paramIn.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = payFeeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
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
                row.createCell(6).setCellValue(tmpRoomDto.getStateName());
                roomIndex += 1;
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(region);
    }


    /**
     * 查询车辆
     *
     * @param pd
     * @param result
     * @param workbook
     */
    private void getCars(IPageData pd, ComponentValidateResult result, Workbook workbook) {
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
        JSONArray cars = this.getExistsCars(pd, result);
        if (cars == null) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
            sheet.addMergedRegion(region);
            return;
        }
        for (int carIndex = 0; carIndex < cars.size(); carIndex++) {
            row = sheet.createRow(carIndex + 2);
            row.createCell(0).setCellValue(cars.getJSONObject(carIndex).getString("carNum"));
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
        }

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(region);
    }


    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsRoom(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "room.queryRooms?page=1&row=10000&communityId=" + result.getCommunityId();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedRoomInfoResults.containsKey("rooms")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("rooms");

    }

    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsCars(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "owner.queryOwnerCars?page=1&row=10000&communityId=" + result.getCommunityId();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedCarInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedCarInfoResults.containsKey("data")) {
            return null;
        }


        return savedCarInfoResults.getJSONArray("data");

    }


    private JSONArray getExistsUnit(IPageData pd, ComponentValidateResult result, String floorId) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "unit.queryUnits?communityId=" + result.getCommunityId() + "&floorId=" + floorId;
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONArray savedFloorInfoResults = JSONArray.parseArray(responseEntity.getBody());


        return savedFloorInfoResults;
    }


    /**
     * 获取 房屋信息
     *
     * @param componentValidateResult
     * @param workbook
     */
    private void getRooms(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("房屋费用信息");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("费用名称: 请填写系统中费用类型，如物业费，押金等 ；\n开始时间: " +
                "收费开始时间，格式为YYYY-MM-DD；\n结束时间: 费用结束时间，格式为YYYY-MM-DD； \n收费金额: 本次收取金额 单位元； " +
                "\n注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 10));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("楼栋编号");
        row.createCell(1).setCellValue("单元编号");
        row.createCell(2).setCellValue("房屋编码");
        row.createCell(3).setCellValue("费用名称");
        row.createCell(4).setCellValue("开始时间");
        row.createCell(5).setCellValue("结束时间");
        row.createCell(6).setCellValue("收费金额");

        //查询楼栋信息
        JSONArray rooms = this.getExistsRoom(pd, componentValidateResult);
        if (rooms == null) {
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
            sheet.addMergedRegion(region);
            return;
        }
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 2);
            row.createCell(0).setCellValue(rooms.getJSONObject(roomIndex).getString("floorNum"));
            row.createCell(1).setCellValue(rooms.getJSONObject(roomIndex).getString("unitNum"));
            row.createCell(2).setCellValue(rooms.getJSONObject(roomIndex).getString("roomNum"));
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
            row.createCell(5).setCellValue("");
            row.createCell(6).setCellValue("");
        }

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
        sheet.addMergedRegion(region);
    }


    /**
     * 导出报表
     *
     * @param pd 前台数据封装
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<Object> exportCustomReportTableData(IPageData pd) throws Exception {
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");
        //Assert.hasKeyAndValue(paramIn, "floorIds", "请求中未包含楼栋");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();

        //查询资产和费用项
        getCustomReportTableData(paramIn, workbook, pd);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=customReportTableImport_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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


    private void getCustomReportTableData(JSONObject paramIn, Workbook workbook, IPageData pd) {
        Sheet sheet = workbook.createSheet("报表数据");
        String apiUrl = "reportCustomComponent.listReportCustomComponentData" + super.mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        if (paramOut.getIntValue("code") != 0) {
            return;
        }
        JSONArray th = paramOut.getJSONObject("data").getJSONArray("th");

        if (th == null || th.size() < 1) {
            return;
        }

        Row row = sheet.createRow(0);
        for (int thIndex = 0; thIndex < th.size(); thIndex++) {
            row.createCell(thIndex).setCellValue(th.getString(thIndex));
        }

        JSONArray td = paramOut.getJSONObject("data").getJSONArray("td");

        if (td == null || td.size() < 1) {
            return;
        }
        JSONObject tdObj = null;
        for (int tdIndex = 0; tdIndex < td.size(); tdIndex++) {
            row = sheet.createRow(tdIndex + 1);
            tdObj = td.getJSONObject(tdIndex);
            for (int thIndex = 0; thIndex < th.size(); thIndex++) {
                row.createCell(thIndex).setCellValue(tdObj.getString(th.getString(thIndex)));
            }
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
