package com.java110.api.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.assetExport.IAssetExportSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
@Service("assetExportSMOImpl")
public class AssetExportSMOImpl extends DefaultAbstractComponentSMO implements IAssetExportSMO {
    private final static Logger logger = LoggerFactory.getLogger(AssetExportSMOImpl.class);

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
        getFloors(pd, result, workbook);
        //获取业主信息
        getOwners(pd, result, workbook);
        //获取房屋信息
        getRooms(pd, result, workbook);
        //获取车位信息
        getParkingSpaces(pd, result, workbook);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsParkSpace(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "parkingSpace.queryParkingSpaces?page=1&row=10000&communityId=" + result.getCommunityId();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedParkingSpaceInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedParkingSpaceInfoResults.containsKey("parkingSpaces") ) {
            return null;
        }


        return savedParkingSpaceInfoResults.getJSONArray("parkingSpaces");

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


        if (!savedRoomInfoResults.containsKey("rooms") ) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("rooms");

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

    private JSONArray getExistsFloor(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "floor.queryFloors?page=1&row=100&communityId=" + result.getCommunityId();

        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedFloorInfoResult = JSONObject.parseObject(responseEntity.getBody());

        if (!savedFloorInfoResult.containsKey("apiFloorDataVoList") ) {
            return null;
        }

        return savedFloorInfoResult.getJSONArray("apiFloorDataVoList");

    }

    /**
     * 查询存在的业主
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsOwner(IPageData pd, ComponentValidateResult result) {
        return getExistsOwner(pd, result, "");
    }

    /**
     * 查询存在的业主
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsOwner(IPageData pd, ComponentValidateResult result, String roomId) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "owner.queryOwners?page=1&row=10000&communityId=" + result.getCommunityId()
                + "&ownerTypeCd=1001";
        if (!StringUtil.isEmpty(roomId)) {
            apiUrl += ("&roomId=" + roomId);
        }
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedOwnerInfoResult = JSONObject.parseObject(responseEntity.getBody());

        if (!savedOwnerInfoResult.containsKey("owners") ) {
            return null;
        }

        return savedOwnerInfoResult.getJSONArray("owners");

    }

    private JSONArray getExistsParkingSpaceCar(IPageData pd, ComponentValidateResult result, String psId) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "parkingSpace.queryParkingSpaceCars?page=1&row=10000&communityId=" + result.getCommunityId();
        if (!StringUtil.isEmpty(psId)) {
            apiUrl += ("&psId=" + psId);
        }
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedOwnerInfoResult = JSONObject.parseObject(responseEntity.getBody());

        if (!savedOwnerInfoResult.containsKey("ownerCars") ) {
            return null;
        }

        return savedOwnerInfoResult.getJSONArray("ownerCars");

    }

    /**
     * 获取车位信息
     *
     * @param workbook
     */
    private void getParkingSpaces(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("车位信息");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("车位编码");
        row.createCell(1).setCellValue("车位类型");
        row.createCell(2).setCellValue("面积");
        row.createCell(3).setCellValue("业主编号");
        row.createCell(4).setCellValue("车牌号");
        row.createCell(5).setCellValue("车品牌");
        row.createCell(6).setCellValue("车类型");
        row.createCell(7).setCellValue("颜色");
        row.createCell(8).setCellValue("出租还是出售（H出租S出售）");

        //查询楼栋信息
        JSONArray parkSpaces = this.getExistsParkSpace(pd, componentValidateResult);

        if(parkSpaces == null){
            return;
        }


        for (int psIndex = 0; psIndex < parkSpaces.size(); psIndex++) {
            row = sheet.createRow(psIndex + 1);
            row.createCell(0).setCellValue(parkSpaces.getJSONObject(psIndex).getString("num"));
            row.createCell(1).setCellValue(parkSpaces.getJSONObject(psIndex).getString("typeCd"));
            row.createCell(2).setCellValue(parkSpaces.getJSONObject(psIndex).getString("area"));
            if (!"H".equals(parkSpaces.getJSONObject(psIndex).getString("state"))) { //已出售 时查询业主信息
                JSONArray ownerCars = this.getExistsParkingSpaceCar(pd, componentValidateResult, parkSpaces.getJSONObject(psIndex).getString("psId"));
                if (ownerCars == null || ownerCars.size() == 0) {
                    row.createCell(3).setCellValue("");
                    row.createCell(4).setCellValue("");
                    row.createCell(5).setCellValue("");
                    row.createCell(6).setCellValue("");
                    row.createCell(7).setCellValue("");
                    continue;
                }
                row.createCell(3).setCellValue(ownerCars.getJSONObject(0).getString("ownerId"));
                row.createCell(4).setCellValue(ownerCars.getJSONObject(0).getString("carNum"));
                row.createCell(5).setCellValue(ownerCars.getJSONObject(0).getString("carBrand"));
                row.createCell(6).setCellValue(ownerCars.getJSONObject(0).getString("carType"));
                row.createCell(7).setCellValue(ownerCars.getJSONObject(0).getString("carColor"));
            }
            row.createCell(8).setCellValue(parkSpaces.getJSONObject(psIndex).getString("state"));

        }
    }

    /**
     * 获取 房屋信息
     *
     * @param componentValidateResult
     * @param workbook
     */
    private void getRooms(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("房屋信息");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("房屋编号");
        row.createCell(1).setCellValue("楼栋编号");
        row.createCell(2).setCellValue("单元编号");
        row.createCell(3).setCellValue("房屋楼层");
        row.createCell(4).setCellValue("房屋户型");
        row.createCell(5).setCellValue("建筑面积");
        row.createCell(6).setCellValue("业主编号");

        //查询楼栋信息
        JSONArray rooms = this.getExistsRoom(pd, componentValidateResult);
        if(rooms == null){
            return;
        }
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            row.createCell(0).setCellValue(rooms.getJSONObject(roomIndex).getString("roomNum"));
            row.createCell(1).setCellValue(rooms.getJSONObject(roomIndex).getString("floorNum"));
            row.createCell(2).setCellValue(rooms.getJSONObject(roomIndex).getString("unitNum"));
            row.createCell(3).setCellValue(rooms.getJSONObject(roomIndex).getString("layer"));
            row.createCell(4).setCellValue(rooms.getJSONObject(roomIndex).getString("section"));
            row.createCell(5).setCellValue(rooms.getJSONObject(roomIndex).getString("builtUpArea"));
            if ("2001".equals(rooms.getJSONObject(roomIndex).getString("state"))) { //已出售 时查询业主信息
                JSONArray ownerDtos = this.getExistsOwner(pd, componentValidateResult, rooms.getJSONObject(roomIndex).getString("roomId"));
                if (ownerDtos == null || ownerDtos.size() == 0) {
                    row.createCell(6).setCellValue("");
                    continue;
                }
                row.createCell(6).setCellValue(ownerDtos.getJSONObject(0).getString("ownerId"));
            }
        }
    }

    /**
     * 获取业主信息
     *
     * @param workbook
     */
    private void getOwners(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("业主信息");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("业主编号");
        row.createCell(1).setCellValue("业主名称");
        row.createCell(2).setCellValue("性别");
        row.createCell(3).setCellValue("年龄");
        row.createCell(4).setCellValue("手机号");

        //查询楼栋信息
        JSONArray owners = this.getExistsOwner(pd, componentValidateResult);
        if(owners == null){
            return;
        }
        for (int owenrIndex = 0; owenrIndex < owners.size(); owenrIndex++) {
            row = sheet.createRow(owenrIndex + 1);
            row.createCell(0).setCellValue(owners.getJSONObject(owenrIndex).getString("ownerId"));
            row.createCell(1).setCellValue(owners.getJSONObject(owenrIndex).getString("name"));
            row.createCell(2).setCellValue("0".equals(owners.getJSONObject(owenrIndex).getString("sex")) ? "男" : "女");
            row.createCell(3).setCellValue(owners.getJSONObject(owenrIndex).getString("age"));
            row.createCell(4).setCellValue(owners.getJSONObject(owenrIndex).getString("link"));
        }
    }

    /**
     * 获取小区
     *
     * @param workbook
     */
    private void getFloors(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("楼栋单元");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("楼栋号");
        row.createCell(1).setCellValue("单元编号");
        row.createCell(2).setCellValue("总层数");
        row.createCell(3).setCellValue("是否有电梯");

        //查询楼栋信息
        JSONArray floors = this.getExistsFloor(pd, componentValidateResult);

        if(floors == null){
            return;
        }
        for (int floorIndex = 0; floorIndex < floors.size(); floorIndex++) {
            JSONArray units = this.getExistsUnit(pd, componentValidateResult, floors.getJSONObject(floorIndex).getString("floorId"));
            for (int unitIndex = 0; unitIndex < units.size(); unitIndex++) {
                row = sheet.createRow(floorIndex + 1);
                row.createCell(0).setCellValue(floors.getJSONObject(floorIndex).getString("floorNum"));
                row.createCell(1).setCellValue(units.getJSONObject(unitIndex).getString("unitNum"));
                row.createCell(2).setCellValue(units.getJSONObject(unitIndex).getString("layerCount"));
                row.createCell(3).setCellValue("1010".equals(units.getJSONObject(unitIndex).getString("lift")) ? "有" : "无");
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
