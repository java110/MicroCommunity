package com.java110.web.smo.assetImport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.FeeTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.common.util.ImportExcelUtils;
import com.java110.common.util.StringUtil;
import com.java110.core.context.IPageData;
import com.java110.dto.FeeConfigDto;
import com.java110.dto.ParkingSpaceDto;
import com.java110.entity.assetImport.ImportFloor;
import com.java110.entity.assetImport.ImportOwner;
import com.java110.entity.assetImport.ImportParkingSpace;
import com.java110.entity.assetImport.ImportRoom;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.assetImport.IAssetImportSMO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
@Service("assetImportSMOImpl")
public class AssetImportSMOImpl extends BaseComponentSMO implements IAssetImportSMO {
    private final static Logger logger = LoggerFactory.getLogger(AssetImportSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        //InputStream is = uploadFile.getInputStream();

        Workbook workbook = null;  //工作簿
        //工作表
        String[] headers = null;   //表头信息
        List<ImportFloor> floors = new ArrayList<ImportFloor>();
        List<ImportOwner> owners = new ArrayList<ImportOwner>();
        List<ImportRoom> rooms = new ArrayList<ImportRoom>();
        List<ImportParkingSpace> parkingSpaces = new ArrayList<ImportParkingSpace>();
        workbook = ImportExcelUtils.createWorkbook(uploadFile);
        //获取楼信息
        getFloors(workbook, floors);
        //获取业主信息
        getOwners(workbook, owners);

        //获取房屋信息
        getRooms(workbook, rooms, floors, owners);

        //获取车位信息
        getParkingSpaces(workbook, parkingSpaces, owners);

        //数据校验
        importExcelDataValidate(floors, owners, rooms, parkingSpaces);

        // 保存数据
        return dealExcelData(pd, floors, owners, rooms, parkingSpaces, result);
    }

    /**
     * 处理ExcelData数据
     *
     * @param floors        楼栋单元信息
     * @param owners        业主信息
     * @param rooms         房屋信息
     * @param parkingSpaces 车位信息
     */
    private ResponseEntity<String> dealExcelData(IPageData pd, List<ImportFloor> floors, List<ImportOwner> owners, List<ImportRoom> rooms, List<ImportParkingSpace> parkingSpaces, ComponentValidateResult result) {
        ResponseEntity<String> responseEntity = null;
        //保存单元信息 和 楼栋信息
        responseEntity = savedFloorAndUnitInfo(pd, floors, result);

        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        // 保存业主信息
        responseEntity = savedOwnerInfo(pd, owners, result);
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        //保存房屋
        responseEntity = savedRoomInfo(pd, rooms, result);
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        //保存车位
        responseEntity = savedParkingSpaceInfo(pd, parkingSpaces, result);

        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        return responseEntity;
    }

    /**
     * 保存车位信息
     *
     * @param pd
     * @param parkingSpaces
     * @param result
     * @return
     */
    private ResponseEntity<String> savedParkingSpaceInfo(IPageData pd, List<ImportParkingSpace> parkingSpaces, ComponentValidateResult result) {
        String apiUrl = "";
        JSONObject paramIn = null;
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        ImportOwner owner = null;
        for (ImportParkingSpace parkingSpace : parkingSpaces) {
            JSONObject savedParkingSpaceInfo = getExistsParkSpace(pd, result, parkingSpace);
            if (savedParkingSpaceInfo != null) {
                continue;
            }

            paramIn = new JSONObject();

            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/parkingSpace.saveParkingSpace";

            paramIn.put("communityId", result.getCommunityId());
            paramIn.put("userId", result.getUserId());
            paramIn.put("num", parkingSpace.getPsNum());
            paramIn.put("area", parkingSpace.getArea());
            paramIn.put("typeCd", parkingSpace.getTypeCd());

            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                continue;
            }

            savedParkingSpaceInfo = getExistsParkSpace(pd, result, parkingSpace);
            if (savedParkingSpaceInfo == null) {
                continue;
            }

            //是否有业主信息
            if (parkingSpace.getImportOwner() == null) {
                continue;
            }

            paramIn.clear();

            paramIn.put("communityId", result.getCommunityId());
            paramIn.put("ownerId", parkingSpace.getImportOwner().getOwnerId());
            paramIn.put("userId", result.getUserId());
            paramIn.put("carNum", parkingSpace.getCarNum());
            paramIn.put("carBrand", parkingSpace.getCarBrand());
            paramIn.put("carType", parkingSpace.getCarType());
            paramIn.put("carColor", parkingSpace.getCarColor());
            paramIn.put("psId", savedParkingSpaceInfo.getString("psId"));
            paramIn.put("storeId", result.getStoreId());
            paramIn.put("sellOrHire", parkingSpace.getSellOrHire());

            if("H".equals(parkingSpace.getSellOrHire())){
                paramIn.put("cycles", "0");
            }

            String feeTypeCd = "1001".equals(parkingSpace.getTypeCd())
                    ? FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE : FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE;
            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/fee.queryFeeConfig?communityId=" + result.getCommunityId() + "&feeTypeCd=" + feeTypeCd;
            responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                continue;
            }

            JSONObject configInfo = JSONArray.parseArray(responseEntity.getBody()).getJSONObject(0);
            if (!configInfo.containsKey("additionalAmount")) {
                continue;
            }

            paramIn.put("receivedAmount", configInfo.getString("additionalAmount"));

            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/parkingSpace.sellParkingSpace";
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
        }

        return responseEntity;
    }


    /**
     * 保存房屋信息
     *
     * @param pd
     * @param rooms
     * @param result
     * @return
     */
    private ResponseEntity<String> savedRoomInfo(IPageData pd, List<ImportRoom> rooms, ComponentValidateResult result) {
        String apiUrl = "";
        JSONObject paramIn = null;
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        ImportOwner owner = null;
        for (ImportRoom room : rooms) {
            JSONObject savedRoomInfo = getExistsRoom(pd, result, room);
            if (savedRoomInfo != null) {
                continue;
            }

            paramIn = new JSONObject();


            //保存 房屋

            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/room.saveRoom";

            paramIn.put("communityId", result.getCommunityId());
            paramIn.put("unitId", room.getFloor().getUnitId());
            paramIn.put("roomNum", room.getRoomNum());
            paramIn.put("layer", room.getLayer());
            paramIn.put("section", "1");
            paramIn.put("apartment", room.getSection());
            paramIn.put("state", "2002");
            paramIn.put("builtUpArea", room.getBuiltUpArea());
            paramIn.put("unitPrice", "1000.00");

            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                continue;
            }

            savedRoomInfo = getExistsRoom(pd, result, room);
            if (savedRoomInfo == null) {
                continue;
            }

            if (room.getImportOwner() == null) {
                continue;
            }
            paramIn.clear();
            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/room.sellRoom";
            paramIn.put("communityId", result.getCommunityId());
            paramIn.put("ownerId", room.getImportOwner().getOwnerId());
            paramIn.put("roomId", savedRoomInfo.getString("roomId"));
            paramIn.put("state", "2001");
            paramIn.put("storeId", result.getStoreId());
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);

        }

        return responseEntity;
    }

    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @param parkingSpace
     * @return
     */
    private JSONObject getExistsParkSpace(IPageData pd, ComponentValidateResult result, ImportParkingSpace parkingSpace) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/parkingSpace.queryParkingSpaces?page=1&row=1&communityId=" + result.getCommunityId()
                + "&num=" + parkingSpace.getPsNum();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedParkingSpaceInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedParkingSpaceInfoResults.containsKey("parkingSpaces") || savedParkingSpaceInfoResults.getJSONArray("parkingSpaces").size() != 1) {
            return null;
        }


        JSONObject savedParkingSpaceInfo = savedParkingSpaceInfoResults.getJSONArray("parkingSpaces").getJSONObject(0);

        return savedParkingSpaceInfo;
    }


    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @param room
     * @return
     */
    private JSONObject getExistsRoom(IPageData pd, ComponentValidateResult result, ImportRoom room) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/room.queryRooms?page=1&row=1&communityId=" + result.getCommunityId()
                + "&floorId=" + room.getFloor().getFloorId() + "&unitId=" + room.getFloor().getUnitId() + "&roomNum=" + room.getRoomNum();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedRoomInfoResults.containsKey("rooms") || savedRoomInfoResults.getJSONArray("rooms").size() != 1) {
            return null;
        }


        JSONObject savedRoomInfo = savedRoomInfoResults.getJSONArray("rooms").getJSONObject(0);

        return savedRoomInfo;
    }

    /**
     * 保存业主信息
     *
     * @param pd
     * @param owners
     * @param result
     * @return
     */
    private ResponseEntity<String> savedOwnerInfo(IPageData pd, List<ImportOwner> owners, ComponentValidateResult result) {
        String apiUrl = "";
        JSONObject paramIn = null;
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);

        for (ImportOwner owner : owners) {
            JSONObject savedOwnerInfo = getExistsOwner(pd, result, owner);

            if (savedOwnerInfo != null) {
                owner.setOwnerId(savedOwnerInfo.getString("ownerId"));
                continue;
            }
            paramIn = new JSONObject();

            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/owner.saveOwner";

            paramIn.put("communityId", result.getCommunityId());
            paramIn.put("userId", result.getUserId());
            paramIn.put("name", owner.getOwnerName());
            paramIn.put("age", owner.getAge());
            paramIn.put("link", owner.getTel());
            paramIn.put("sex", owner.getSex());
            paramIn.put("ownerTypeCd", "1001");
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                savedOwnerInfo = getExistsOwner(pd, result, owner);
                owner.setOwnerId(savedOwnerInfo.getString("ownerId"));
            }
        }

        return responseEntity;
    }

    /**
     * 保存 楼栋和 单元信息
     *
     * @param pd
     * @param floors
     * @param result
     * @return
     */
    private ResponseEntity<String> savedFloorAndUnitInfo(IPageData pd, List<ImportFloor> floors, ComponentValidateResult result) {
        String apiUrl = "";
        JSONObject paramIn = null;
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        for (ImportFloor importFloor : floors) {
            paramIn = new JSONObject();
            //先保存 楼栋信息
            JSONObject savedFloorInfo = getExistsFloor(pd, result, importFloor);
            // 如果不存在，才插入
            if (savedFloorInfo == null) {
                apiUrl = ServiceConstant.SERVICE_API_URL + "/api/floor.saveFloor";
                paramIn.put("communityId", result.getCommunityId());
                paramIn.put("floorNum", importFloor.getFloorNum());
                paramIn.put("userId", result.getUserId());
                paramIn.put("name", importFloor.getFloorNum() + "号楼");
                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
            }
            if (responseEntity != null && responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
                continue;
            }

            savedFloorInfo = getExistsFloor(pd, result, importFloor);

            if (savedFloorInfo == null) {
                continue;
            }
            importFloor.setFloorId(savedFloorInfo.getString("floorId"));
            paramIn.clear();
            //判断单元信息是否已经存在，如果存在则不保存数据unit.queryUnits
            JSONObject savedUnitInfo = getExistsUnit(pd, result, importFloor);
            if (savedUnitInfo != null) {
                importFloor.setUnitId(savedUnitInfo.getString("unitId"));
                continue;
            }

            apiUrl = ServiceConstant.SERVICE_API_URL + "/api/unit.saveUnit";

            paramIn.put("communityId", result.getCommunityId());
            paramIn.put("floorId", savedFloorInfo.getString("floorId"));
            paramIn.put("unitNum", importFloor.getUnitNum());
            paramIn.put("layerCount", importFloor.getLayerCount());
            paramIn.put("lift", importFloor.getLift());
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);

            //将unitId 刷入ImportFloor对象
            savedUnitInfo = getExistsUnit(pd, result, importFloor);
            importFloor.setUnitId(savedUnitInfo.getString("unitId"));

        }
        return responseEntity;
    }

    private JSONObject getExistsUnit(IPageData pd, ComponentValidateResult result, ImportFloor importFloor) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/unit.queryUnits?communityId=" + result.getCommunityId()
                + "&floorId=" + importFloor.getFloorId() + "&unitNum=" + importFloor.getUnitNum();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONArray savedFloorInfoResults = JSONArray.parseArray(responseEntity.getBody());

        if (savedFloorInfoResults == null || savedFloorInfoResults.size() != 1) {
            return null;
        }

        JSONObject savedUnitInfo = savedFloorInfoResults.getJSONObject(0);

        return savedUnitInfo;
    }

    private JSONObject getExistsFloor(IPageData pd, ComponentValidateResult result, ImportFloor importFloor) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/floor.queryFloors?page=1&row=1&communityId=" + result.getCommunityId() + "&floorNum=" + importFloor.getFloorNum();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedFloorInfoResult = JSONObject.parseObject(responseEntity.getBody());

        if (!savedFloorInfoResult.containsKey("apiFloorDataVoList") || savedFloorInfoResult.getJSONArray("apiFloorDataVoList").size() != 1) {
            return null;
        }

        JSONObject savedFloorInfo = savedFloorInfoResult.getJSONArray("apiFloorDataVoList").getJSONObject(0);

        return savedFloorInfo;
    }

    /**
     * 查询存在的业主
     *
     * @param pd
     * @param result
     * @param importOwner
     * @return
     */
    private JSONObject getExistsOwner(IPageData pd, ComponentValidateResult result, ImportOwner importOwner) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = ServiceConstant.SERVICE_API_URL + "/api/owner.queryOwners?page=1&row=1&communityId=" + result.getCommunityId()
                + "&ownerTypeCd=1001&name=" + importOwner.getOwnerName() + "&link=" + importOwner.getTel();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedOwnerInfoResult = JSONObject.parseObject(responseEntity.getBody());

        if (!savedOwnerInfoResult.containsKey("owners") || savedOwnerInfoResult.getJSONArray("owners").size() != 1) {
            return null;
        }

        JSONObject savedOwnerInfo = savedOwnerInfoResult.getJSONArray("owners").getJSONObject(0);

        return savedOwnerInfo;
    }

    /**
     * 数据校验处理
     *
     * @param floors
     * @param owners
     * @param rooms
     * @param parkingSpaces
     */
    private void importExcelDataValidate(List<ImportFloor> floors, List<ImportOwner> owners, List<ImportRoom> rooms, List<ImportParkingSpace> parkingSpaces) {
    }

    /**
     * 获取车位信息
     *
     * @param workbook
     * @param parkingSpaces
     */
    private void getParkingSpaces(Workbook workbook, List<ImportParkingSpace> parkingSpaces, List<ImportOwner> owners) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "车位信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportParkingSpace importParkingSpace = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            importParkingSpace = new ImportParkingSpace();
            importParkingSpace.setPsNum(os[0].toString());
            importParkingSpace.setTypeCd(os[1].toString());
            importParkingSpace.setArea(Double.parseDouble(os[2].toString()));
            if (StringUtil.isNullOrNone(os[3])) {
                parkingSpaces.add(importParkingSpace);
                continue;
            }
            ImportOwner importOwner = getImportOwner(owners, os[3].toString());
            importParkingSpace.setImportOwner(importOwner);
            if (importOwner != null) {
                importParkingSpace.setCarNum(os[4].toString());
                importParkingSpace.setCarBrand(os[5].toString());
                importParkingSpace.setCarType(os[6].toString());
                importParkingSpace.setCarColor(os[7].toString());
                importParkingSpace.setSellOrHire(os[8].toString());
            }

            parkingSpaces.add(importParkingSpace);
        }
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param rooms
     */
    private void getRooms(Workbook workbook, List<ImportRoom> rooms,
                          List<ImportFloor> floors, List<ImportOwner> owners) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "房屋信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportRoom importRoom = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            importRoom = new ImportRoom();
            importRoom.setRoomNum(os[0].toString());
            importRoom.setFloor(getImportFloor(floors, os[1].toString(), os[2].toString()));
            importRoom.setLayer(Integer.parseInt(os[3].toString()));
            importRoom.setSection(os[4].toString());
            importRoom.setBuiltUpArea(Double.parseDouble(os[5].toString()));
            if (StringUtil.isNullOrNone(os[6])) {
                rooms.add(importRoom);
                continue;
            }
            importRoom.setImportOwner(getImportOwner(owners, os[6].toString()));
            rooms.add(importRoom);
        }
    }

    /**
     * 从导入的业主信息中获取业主，如果没有返回 null
     *
     * @param owners
     * @param ownerNum
     * @return
     */
    private ImportOwner getImportOwner(List<ImportOwner> owners, String ownerNum) {
        for (ImportOwner importOwner : owners) {
            if (ownerNum.equals(importOwner.getOwnerNum())) {
                return importOwner;
            }
        }

        return null;

    }

    /**
     * get 导入楼栋信息
     *
     * @param floors
     */
    private ImportFloor getImportFloor(List<ImportFloor> floors, String floorNum, String unitNum) {
        for (ImportFloor importFloor : floors) {
            if (floorNum.equals(importFloor.getFloorNum())
                    && unitNum.equals(importFloor.getUnitNum())) {
                return importFloor;
            }
        }

        throw new IllegalArgumentException("在楼栋单元sheet中未找到楼栋编号[" + floorNum + "],单元编号[" + unitNum + "]数据");
    }

    /**
     * 获取业主信息
     *
     * @param workbook
     * @param owners
     */
    private void getOwners(Workbook workbook, List<ImportOwner> owners) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "业主信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportOwner importOwner = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            importOwner = new ImportOwner();
            importOwner.setOwnerNum(os[0].toString());
            importOwner.setOwnerName(os[1].toString());
            importOwner.setSex("男".equals(os[2].toString()) ? "0" : "1");
            importOwner.setAge(Integer.parseInt(os[3].toString()));
            importOwner.setTel(os[4].toString());
            owners.add(importOwner);
        }
    }

    /**
     * 获取小区
     *
     * @param workbook
     * @param floors
     */
    private void getFloors(Workbook workbook, List<ImportFloor> floors) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "楼栋单元");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportFloor importFloor = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }

            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            importFloor = new ImportFloor();
            importFloor.setFloorNum(os[0].toString());
            importFloor.setUnitNum(os[1].toString());
            importFloor.setLayerCount(os[2].toString());
            importFloor.setLift("有".equals(os[3].toString()) ? "1010" : "2020");
            floors.add(importFloor);
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
