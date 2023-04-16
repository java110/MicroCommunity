package com.java110.api.smo.assetImport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetImport.IAssetImportSMO;
import com.java110.core.context.IPageData;
import com.java110.core.smo.ISaveTransactionLogSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.assetImportLog.AssetImportLogDto;
import com.java110.dto.assetImportLog.AssetImportLogDetailDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.assetImport.*;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class AssetImportSMOImpl extends DefaultAbstractComponentSMO implements IAssetImportSMO {
    private final static Logger logger = LoggerFactory.getLogger(AssetImportSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISaveTransactionLogSMO saveTransactionLogSMOImpl;

    @Override
    public ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception {

        try {
            ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

            //InputStream is = uploadFile.getInputStream();

            Workbook workbook = null;  //工作簿
            //工作表
            String[] headers = null;   //表头信息
            List<ImportFloor> floors = new ArrayList<ImportFloor>();
            List<ImportOwner> owners = new ArrayList<ImportOwner>();
            List<ImportFee> fees = new ArrayList<>();
            List<ImportRoom> rooms = new ArrayList<ImportRoom>();
            List<ImportParkingSpace> parkingSpaces = new ArrayList<ImportParkingSpace>();
            workbook = ImportExcelUtils.createWorkbook(uploadFile);
            //获取楼信息
            getFloors(workbook, floors);
            //获取业主信息
            getOwners(workbook, owners);


            getFee(workbook, fees);

            //获取房屋信息
            getRooms(workbook, rooms, floors, owners);

            //获取车位信息
            getParkingSpaces(workbook, parkingSpaces, owners);

            //数据校验
            importExcelDataValidate(floors, owners, rooms, fees, parkingSpaces);

            // 保存数据
            return dealExcelData(pd, floors, owners, rooms, parkingSpaces, fees, result);
        } catch (Exception e) {
            logger.error("导入失败 ", e);
            return new ResponseEntity<String>("非常抱歉，您填写的模板数据有误：" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 处理ExcelData数据
     *
     * @param floors        楼栋单元信息
     * @param owners        业主信息
     * @param rooms         房屋信息
     * @param parkingSpaces 车位信息
     */
    private ResponseEntity<String> dealExcelData(IPageData pd,
                                                 List<ImportFloor> floors,
                                                 List<ImportOwner> owners,
                                                 List<ImportRoom> rooms,
                                                 List<ImportParkingSpace> parkingSpaces,
                                                 List<ImportFee> fees,
                                                 ComponentValidateResult result) {
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

        // 保存费用项
        responseEntity = savedFeeInfo(pd, fees, result);
        if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        //保存房屋
        responseEntity = savedRoomInfo(pd, rooms, fees, result);
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

    private ResponseEntity<String> savedFeeInfo(IPageData pd, List<ImportFee> fees, ComponentValidateResult result) {
        String apiUrl = "";
        JSONObject paramIn = null;
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        ImportOwner owner = null;

        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setSuccessCount(0L);
        assetImportLogDto.setErrorCount(0L);
        assetImportLogDto.setCommunityId(result.getCommunityId());
        assetImportLogDto.setLogType(AssetImportLogDto.LOG_TYPE_FEE_IMPORT);
        List<AssetImportLogDetailDto> assetImportLogDetailDtos = new ArrayList<>();
        assetImportLogDto.setAssetImportLogDetailDtos(assetImportLogDetailDtos);
        long successCount = 0L;
        long failCount = 0L;
        AssetImportLogDetailDto assetImportLogDetailDto = null;
        try {
            for (ImportFee fee : fees) {
                JSONObject savedFeeConfigInfo = getExistsFee(pd, result, fee);
                if (savedFeeConfigInfo != null) {
                    successCount += 1;
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                    continue;
                }
                //paramIn = new JSONObject();
                //保存 费用项

                apiUrl = "feeConfig.saveFeeConfig";

                paramIn = JSONObject.parseObject(JSONObject.toJSONString(fee));
                paramIn.put("communityId", result.getCommunityId());

                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    /***************************************导入日志记录****************************************************/
                    failCount += 1;
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(fee.getFeeName());
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(fee.getFeeName());
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                    } else {
                        successCount += 1;
                    }
                }
                assetImportLogDto.setSuccessCount(successCount);
                assetImportLogDto.setErrorCount(failCount);
            }
        } finally {
            saveTransactionLogSMOImpl.saveAssetImportLog(assetImportLogDto);
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
        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setSuccessCount(0L);
        assetImportLogDto.setErrorCount(0L);
        assetImportLogDto.setCommunityId(result.getCommunityId());
        assetImportLogDto.setLogType(AssetImportLogDto.LOG_TYPE_AREA_PARKING_IMPORT);
        List<AssetImportLogDetailDto> assetImportLogDetailDtos = new ArrayList<>();
        assetImportLogDto.setAssetImportLogDetailDtos(assetImportLogDetailDtos);
        long successCount = 0L;
        long failCount = 0L;
        AssetImportLogDetailDto assetImportLogDetailDto = null;
        try {
            for (ImportParkingSpace parkingSpace : parkingSpaces) {
                responseEntity = ResultVo.success();
                JSONObject savedParkingAreaInfo = getExistsParkingArea(pd, result, parkingSpace);
                paramIn = new JSONObject();
                // 如果不存在，才插入
                if (savedParkingAreaInfo == null) {
                    apiUrl = "parkingArea.saveParkingArea";
                    paramIn.put("communityId", result.getCommunityId());
                    paramIn.put("typeCd", parkingSpace.getTypeCd());
                    paramIn.put("num", parkingSpace.getPaNum());

                    responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                    savedParkingAreaInfo = getExistsParkingArea(pd, result, parkingSpace);
                }
                if (responseEntity != null && responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
                    failCount += 1;
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(parkingSpace.getPaNum());
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                    continue;
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(parkingSpace.getPaNum());
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                        continue;
                    } else {
                        successCount += 1;
                    }
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                }

                JSONObject savedParkingSpaceInfo = getExistsParkSpace(pd, result, parkingSpace);
                if (savedParkingSpaceInfo != null) {
                    continue;
                }

                apiUrl = "parkingSpace.saveParkingSpace";

                paramIn.put("paId", savedParkingAreaInfo.getString("paId"));
                paramIn.put("communityId", result.getCommunityId());
                paramIn.put("userId", result.getUserId());
                paramIn.put("num", parkingSpace.getPsNum());
                paramIn.put("area", parkingSpace.getArea());
                paramIn.put("typeCd", parkingSpace.getTypeCd());
                paramIn.put("parkingType", "1");

                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(parkingSpace.getPaNum());
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    failCount += 1;
                    successCount = successCount > 0 ? successCount - 1 : successCount;
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                    continue;
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(parkingSpace.getPaNum());
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                        failCount += 1;
                        successCount = successCount > 0 ? successCount - 1 : successCount;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                        continue;
                    }
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
                paramIn.put("startTime", parkingSpace.getStartTime());
                paramIn.put("endTime", parkingSpace.getEndTime());

                if ("H".equals(parkingSpace.getSellOrHire())) {
                    paramIn.put("cycles", "0");
                }

                apiUrl = "parkingSpace.sellParkingSpace";
                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);

                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    /***************************************导入日志记录****************************************************/
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(parkingSpace.getCarNum());
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    failCount += 1;
                    successCount = successCount > 0 ? successCount - 1 : successCount;
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(parkingSpace.getCarNum());
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                        successCount = successCount > 0 ? successCount - 1 : successCount;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("导入车位异常", e);
            saveTransactionLogSMOImpl.saveAssetImportLog(assetImportLogDto);
            throw e;
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
    private ResponseEntity<String> savedRoomInfo(IPageData pd, List<ImportRoom> rooms, List<ImportFee> fees, ComponentValidateResult result) {
        String apiUrl = "";
        JSONObject paramIn = null;
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        ImportOwner owner = null;
        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setSuccessCount(0L);
        assetImportLogDto.setErrorCount(0L);
        assetImportLogDto.setCommunityId(result.getCommunityId());
        assetImportLogDto.setLogType(AssetImportLogDto.LOG_TYPE_ROOM_IMPORT);
        List<AssetImportLogDetailDto> assetImportLogDetailDtos = new ArrayList<>();
        assetImportLogDto.setAssetImportLogDetailDtos(assetImportLogDetailDtos);
        long successCount = 0L;
        long failCount = 0L;
        AssetImportLogDetailDto assetImportLogDetailDto = null;
        try {
            for (ImportRoom room : rooms) {
                paramIn = new JSONObject();
                JSONObject savedRoomInfo = getExistsRoom(pd, result, room);
                if (savedRoomInfo != null) {
                    //如果空闲入住一下
                    if (RoomDto.STATE_FREE.equals(savedRoomInfo.getString("state")) && room.getImportOwner() != null) {
                        paramIn.clear();
                        apiUrl = "room.sellRoom";
                        paramIn.put("communityId", result.getCommunityId());
                        paramIn.put("ownerId", room.getImportOwner().getOwnerId());
                        paramIn.put("roomId", savedRoomInfo.getString("roomId"));
                        paramIn.put("state", "2001");
                        paramIn.put("storeId", result.getStoreId());
                        if (!StringUtil.isEmpty(room.getRoomFeeId()) && "0".equals(room.getRoomFeeId())) {
                            paramIn.put("feeEndDate", room.getFeeEndDate());
                        }
                        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                    }
                    continue;
                }
                //保存 房屋
                apiUrl = "room.saveRoom";

                paramIn.put("communityId", result.getCommunityId());
                paramIn.put("unitId", room.getFloor().getUnitId());
                paramIn.put("roomNum", room.getRoomNum());
                paramIn.put("layer", room.getLayer());
                paramIn.put("section", "1");
                paramIn.put("apartment", room.getSection());
                paramIn.put("state", "2002");
                paramIn.put("builtUpArea", room.getBuiltUpArea());
                paramIn.put("feeCoefficient", "1.00");
                paramIn.put("roomSubType", room.getRoomSubType());
                paramIn.put("roomArea", room.getRoomArea());
                paramIn.put("roomRent", room.getRoomRent());
                paramIn.put("roomType", "0".equals(room.getFloor().getUnitNum()) ? RoomDto.ROOM_TYPE_SHOPS : RoomDto.ROOM_TYPE_SHOPS);


                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    failCount += 1;
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(room.getRoomNum() + "室");
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                    continue;
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(room.getRoomNum() + "室");
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                        continue;
                    } else {
                        successCount += 1;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                    }
                }

                savedRoomInfo = getExistsRoom(pd, result, room);
                if (savedRoomInfo == null) {
                    continue;
                }
                if (room.getImportOwner() == null) {
                    continue;
                }
                paramIn.clear();
                apiUrl = "room.sellRoom";
                paramIn.put("communityId", result.getCommunityId());
                paramIn.put("ownerId", room.getImportOwner().getOwnerId());
                paramIn.put("roomId", savedRoomInfo.getString("roomId"));
                paramIn.put("state", "2001");
                paramIn.put("storeId", result.getStoreId());
                if (!StringUtil.isEmpty(room.getRoomFeeId()) && "0".equals(room.getRoomFeeId())) {
                    paramIn.put("feeEndDate", room.getFeeEndDate());
                }
                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(room.getRoomNum() + "室");
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    continue;
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(room.getRoomNum() + "室");
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        continue;
                    }
                }
                //创建费用
                if (StringUtil.isEmpty(room.getRoomFeeId()) || "0".equals(room.getRoomFeeId())) {
                    continue;
                }
                String[] feeIds = room.getRoomFeeId().split("#");

                for (int feeIndex = 0; feeIndex < feeIds.length; feeIndex++) {
                    String feeId = feeIds[feeIndex];
                    ImportFee tmpFee = null;
                    for (ImportFee fee : fees) {
                        if (feeId.equals(fee.getId())) {
                            tmpFee = fee;
                        }
                    }

                    if (tmpFee == null) {
                        continue;//没有费用项，可能写错了
                    }

                    JSONObject ttFee = getExistsFee(pd, result, tmpFee);

                    if (ttFee == null) {
                        continue;//没有费用项，可能写错了
                    }

                    apiUrl = "fee.saveRoomCreateFee";
                    paramIn.put("communityId", result.getCommunityId());
                    paramIn.put("locationTypeCd", "3000");
                    paramIn.put("locationObjId", savedRoomInfo.getString("roomId"));
                    paramIn.put("configId", ttFee.getString("configId"));
                    paramIn.put("storeId", result.getStoreId());
                    paramIn.put("feeEndDate", room.getFeeEndDate().split("#")[feeIndex]);
                    paramIn.put("startTime", paramIn.getString("feeEndDate"));

                    responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);

                    if (responseEntity.getStatusCode() != HttpStatus.OK) {
                        /***************************************导入日志记录****************************************************/
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        if (Assert.isJsonObject(responseEntity.getBody())) {
                            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                            assetImportLogDetailDto.setState(paramOut.getString("code"));
                            assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                        } else {
                            assetImportLogDetailDto.setState("F");
                            assetImportLogDetailDto.setMessage(responseEntity.getBody());
                        }
                        assetImportLogDetailDto.setObjName(room.getRoomNum() + "室");
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                        successCount = successCount > 0 ? successCount - 1 : successCount;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                    } else {
                        JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                        if (body.containsKey("code") && body.getIntValue("code") != 0) {
                            assetImportLogDetailDto = new AssetImportLogDetailDto();
                            assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                            assetImportLogDetailDto.setState(body.getString("code"));
                            assetImportLogDetailDto.setMessage(body.getString("msg"));
                            assetImportLogDetailDto.setObjName(room.getRoomNum() + "室");
                            assetImportLogDetailDtos.add(assetImportLogDetailDto);
                            failCount += 1;
                            successCount = successCount > 0 ? successCount - 1 : successCount;
                            assetImportLogDto.setSuccessCount(successCount);
                            assetImportLogDto.setErrorCount(failCount);
                        }
                    }
                }

            }
        } finally {
            saveTransactionLogSMOImpl.saveAssetImportLog(assetImportLogDto);
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
        apiUrl = "parkingSpace.queryParkingSpaces?page=1&row=1&communityId=" + result.getCommunityId()
                + "&num=" + parkingSpace.getPsNum() + "&areaNum=" + parkingSpace.getPaNum();
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
     * @param fee
     * @return
     */
    private JSONObject getExistsFee(IPageData pd, ComponentValidateResult result, ImportFee fee) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "feeConfig.listFeeConfigs?page=1&row=1&communityId=" + result.getCommunityId()
                + "&feeName=" + fee.getFeeName();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedRoomInfoResults.containsKey("feeConfigs") || savedRoomInfoResults.getJSONArray("feeConfigs").size() != 1) {
            return null;
        }


        JSONObject savedFeeConfigInfo = savedRoomInfoResults.getJSONArray("feeConfigs").getJSONObject(0);

        return savedFeeConfigInfo;
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
        apiUrl = "room.queryRooms?page=1&row=1&communityId=" + result.getCommunityId()
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
        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setSuccessCount(0L);
        assetImportLogDto.setErrorCount(0L);
        assetImportLogDto.setCommunityId(result.getCommunityId());
        assetImportLogDto.setLogType(AssetImportLogDto.LOG_TYPE_OWENR_IMPORT);
        List<AssetImportLogDetailDto> assetImportLogDetailDtos = new ArrayList<>();
        assetImportLogDto.setAssetImportLogDetailDtos(assetImportLogDetailDtos);
        long successCount = 0L;
        long failCount = 0L;
        AssetImportLogDetailDto assetImportLogDetailDto = null;
        try {
            for (ImportOwner owner : owners) {
                JSONObject savedOwnerInfo = getExistsOwner(pd, result, owner);

                if (savedOwnerInfo != null) {
                    owner.setOwnerId(savedOwnerInfo.getString("ownerId"));
                    continue;
                }
                paramIn = new JSONObject();

                apiUrl = "owner.saveOwner";

                paramIn.put("communityId", result.getCommunityId());
                paramIn.put("userId", result.getUserId());
                paramIn.put("name", owner.getOwnerName());
                paramIn.put("age", owner.getAge());
                paramIn.put("link", owner.getTel());
                paramIn.put("sex", owner.getSex());
                paramIn.put("ownerTypeCd", owner.getOwnerTypeCd());
                paramIn.put("idCard", owner.getIdCard());
                paramIn.put("source", "BatchImport");
                if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(owner.getOwnerTypeCd())) {
                    //查询业主ID
                    paramIn.put("ownerId", getOwnerId(owners, owner));
                }
                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);

                /***************************************导入日志记录****************************************************/
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(owner.getOwnerName());
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                        throw new IllegalArgumentException(body.getString("msg"));
                    }
                    savedOwnerInfo = getExistsOwner(pd, result, owner);
                    owner.setOwnerId(savedOwnerInfo.getString("ownerId"));
                    successCount += 1;
                } else {
                    failCount += 1;
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(owner.getOwnerName());
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                }
                assetImportLogDto.setSuccessCount(successCount);
                assetImportLogDto.setErrorCount(failCount);
            }
        } finally {
            saveTransactionLogSMOImpl.saveAssetImportLog(assetImportLogDto);
        }

        return responseEntity;
    }

    private String getOwnerId(List<ImportOwner> owners, ImportOwner owner) {
        for (ImportOwner owner1 : owners) {
            if (owner1.getOwnerNum().equals(owner.getParentOwnerId())) {
                return owner1.getOwnerId();
            }
        }
        throw new IllegalArgumentException("请将业主成员放到业主后面,或者业主成员未找到对应的业主");
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

        AssetImportLogDto assetImportLogDto = new AssetImportLogDto();
        assetImportLogDto.setSuccessCount(0L);
        assetImportLogDto.setErrorCount(0L);
        assetImportLogDto.setCommunityId(result.getCommunityId());
        assetImportLogDto.setLogType(AssetImportLogDto.LOG_TYPE_FLOOR_UNIT_IMPORT);
        List<AssetImportLogDetailDto> assetImportLogDetailDtos = new ArrayList<>();
        assetImportLogDto.setAssetImportLogDetailDtos(assetImportLogDetailDtos);
        long successCount = 0L;
        long failCount = 0L;
        AssetImportLogDetailDto assetImportLogDetailDto = null;
        try {
            for (int floorIndex = 0; floorIndex < floors.size(); floorIndex++) {
                ImportFloor importFloor = floors.get(floorIndex);
                paramIn = new JSONObject();
                //先保存 楼栋信息
                JSONObject savedFloorInfo = getExistsFloor(pd, result, importFloor);
                // 如果不存在，才插入
                if (savedFloorInfo == null) {
                    apiUrl = "floor.saveFloor";
                    paramIn.put("communityId", result.getCommunityId());
                    paramIn.put("floorNum", importFloor.getFloorNum());
                    paramIn.put("userId", result.getUserId());
                    paramIn.put("name", importFloor.getFloorNum() + "栋");
                    paramIn.put("floorArea", 1.00);
                    paramIn.put("seq",floorIndex+1);

                    responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                    savedFloorInfo = getExistsFloor(pd, result, importFloor);
                }

                /***************************************导入日志记录****************************************************/
                if (responseEntity != null && responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
                    failCount += 1;
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(importFloor.getFloorNum() + "栋");
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                    continue;
                } else {
                    successCount += 1;
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                }


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

                apiUrl = "unit.saveUnit";

                paramIn.put("communityId", result.getCommunityId());
                paramIn.put("floorId", savedFloorInfo.getString("floorId"));
                paramIn.put("unitNum", importFloor.getUnitNum());
                paramIn.put("layerCount", importFloor.getLayerCount());
                paramIn.put("lift", importFloor.getLift());
                paramIn.put("unitArea", 1.00);
                responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(), apiUrl, HttpMethod.POST);
                /****************************** 开始记录失败日志 *******************************/
                if (responseEntity != null && responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
                    assetImportLogDetailDto = new AssetImportLogDetailDto();
                    assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                    if (Assert.isJsonObject(responseEntity.getBody())) {
                        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
                        assetImportLogDetailDto.setState(paramOut.getString("code"));
                        assetImportLogDetailDto.setMessage(paramOut.getString("msg"));
                    } else {
                        assetImportLogDetailDto.setState("F");
                        assetImportLogDetailDto.setMessage(responseEntity.getBody());
                    }
                    assetImportLogDetailDto.setObjName(importFloor.getFloorNum() + "栋" + importFloor.getUnitNum() + "单元");
                    assetImportLogDetailDtos.add(assetImportLogDetailDto);
                    failCount += 1;
                    successCount = successCount > 0 ? successCount - 1 : successCount;
                    assetImportLogDto.setSuccessCount(successCount);
                    assetImportLogDto.setErrorCount(failCount);
                } else {
                    JSONObject body = JSONObject.parseObject(responseEntity.getBody());
                    if (body.containsKey("code") && body.getIntValue("code") != 0) {
                        assetImportLogDetailDto = new AssetImportLogDetailDto();
                        assetImportLogDetailDto.setCommunityId(assetImportLogDto.getCommunityId());
                        assetImportLogDetailDto.setState(body.getString("code"));
                        assetImportLogDetailDto.setMessage(body.getString("msg"));
                        assetImportLogDetailDto.setObjName(importFloor.getFloorNum() + "栋" + importFloor.getUnitNum() + "单元");
                        assetImportLogDetailDtos.add(assetImportLogDetailDto);
                        failCount += 1;
                        successCount = successCount > 0 ? successCount - 1 : successCount;
                        assetImportLogDto.setSuccessCount(successCount);
                        assetImportLogDto.setErrorCount(failCount);
                    }
                }
                //将unitId 刷入ImportFloor对象
                savedUnitInfo = getExistsUnit(pd, result, importFloor);
                importFloor.setUnitId(savedUnitInfo.getString("unitId"));

            }
        } finally {
            saveTransactionLogSMOImpl.saveAssetImportLog(assetImportLogDto);
        }
        return responseEntity;
    }

    private JSONObject getExistsUnit(IPageData pd, ComponentValidateResult result, ImportFloor importFloor) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "unit.queryUnits?communityId=" + result.getCommunityId()
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
        apiUrl = "floor.queryFloors?page=1&row=1&communityId=" + result.getCommunityId() + "&floorNum=" + importFloor.getFloorNum();
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
        apiUrl = "owner.queryOwners?page=1&row=1&communityId=" + result.getCommunityId()
                + "&ownerTypeCd=" + importOwner.getOwnerTypeCd() + "&name=" + importOwner.getOwnerName() + "&link=" + importOwner.getTel();
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
     * 查询存在的停车场
     *
     * @param pd
     * @param result
     * @param parkingSpace
     * @return
     */
    private JSONObject getExistsParkingArea(IPageData pd, ComponentValidateResult result, ImportParkingSpace parkingSpace) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "parkingArea.listParkingAreas?page=1&row=1&communityId=" + result.getCommunityId()
                + "&num=" + parkingSpace.getPaNum();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedParkingAreaInfoResult = JSONObject.parseObject(responseEntity.getBody());

        if (!savedParkingAreaInfoResult.containsKey("parkingAreas") || savedParkingAreaInfoResult.getJSONArray("parkingAreas").size() != 1) {
            return null;
        }

        JSONObject savedParkingAreaInfo = savedParkingAreaInfoResult.getJSONArray("parkingAreas").getJSONObject(0);

        return savedParkingAreaInfo;
    }

    /**
     * 数据校验处理
     *
     * @param floors
     * @param owners
     * @param rooms
     * @param parkingSpaces
     */
    private void importExcelDataValidate(List<ImportFloor> floors, List<ImportOwner> owners, List<ImportRoom> rooms, List<ImportFee> fees, List<ImportParkingSpace> parkingSpaces) {

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
            Assert.hasValue(os[0], "车位信息选项中" + (osIndex + 1) + "行停车场编号为空");
            Assert.hasValue(os[1], "车位信息选项中" + (osIndex + 1) + "行车位编码为空");
            Assert.hasValue(os[2], "车位信息选项中" + (osIndex + 1) + "行停车场类型为空");
            Assert.hasValue(os[3], "车位信息选项中" + (osIndex + 1) + "行面积为空，没有请填写规定值 如10");
            importParkingSpace = new ImportParkingSpace();
            importParkingSpace.setPaNum(os[0].toString());
            importParkingSpace.setPsNum(os[1].toString());
            importParkingSpace.setTypeCd(os[2].toString());
            importParkingSpace.setArea(Double.parseDouble(os[3].toString()));
            if (os.length < 5 || StringUtil.isNullOrNone(os[4])) {
                parkingSpaces.add(importParkingSpace);
                continue;
            }
            ImportOwner importOwner = getImportOwner(owners, os[4].toString());
            importParkingSpace.setImportOwner(importOwner);
            if (importOwner != null) {
                importParkingSpace.setCarNum(os[5].toString());
                importParkingSpace.setCarBrand(os[6].toString());
                importParkingSpace.setCarType(os[7].toString());
                importParkingSpace.setCarColor(os[8].toString());
                importParkingSpace.setSellOrHire(os[9].toString());

                String startTime = excelDoubleToDate(os[10].toString());
                String endTime = excelDoubleToDate(os[11].toString());
                Assert.isDate(startTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行开始时间格式错误 请填写YYYY-MM-DD 文本格式");
                Assert.isDate(endTime, DateUtil.DATE_FORMATE_STRING_B, (osIndex + 1) + "行结束时间格式错误 请填写YYYY-MM-DD 文本格式");
                importParkingSpace.setStartTime(startTime);
                importParkingSpace.setEndTime(endTime);
            }

            parkingSpaces.add(importParkingSpace);
        }
    }

    //解析Excel日期格式
    public static String excelDoubleToDate(String strDate) {
        if (strDate.length() == 5) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date tDate = DoubleToDate(Double.parseDouble(strDate));
                return sdf.format(tDate);
            } catch (Exception e) {
                e.printStackTrace();
                return strDate;
            }
        }
        return strDate;
    }


    //解析Excel日期格式
    public static Date DoubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));

        return tDate;
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
            try {
                Object[] os = oList.get(osIndex);
                if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                    continue;
                }
                if (StringUtil.isNullOrNone(os[0])) {
                    continue;
                }
                Assert.hasValue(os[1], "房屋信息选项中" + (osIndex + 1) + "行楼栋编号为空");
                Assert.hasValue(os[2], "房屋信息选项中" + (osIndex + 1) + "行单元编号为空");
                Assert.hasValue(os[3], "房屋信息选项中" + (osIndex + 1) + "行房屋楼层为空");
                Assert.hasValue(os[4], "房屋信息选项中" + (osIndex + 1) + "行房屋户型为空");
                Assert.hasValue(os[5], "房屋信息选项中" + (osIndex + 1) + "行建筑面积为空");
                if (os.length > 6 && !StringUtil.isNullOrNone(os[6])) {
                    Assert.hasValue(os[7], "房屋信息选项中" + (osIndex + 1) + "行房屋费用为空");
                    Assert.hasValue(os[8], "房屋信息选项中" + (osIndex + 1) + "行费用到期时间为空");
                }
                Assert.hasValue(os[9], "房屋信息选项中" + (osIndex + 1) + "行房屋类型为空");
                Assert.hasValue(os[10], "房屋信息选项中" + (osIndex + 1) + "行室内面积为空");
                Assert.hasValue(os[11], "房屋信息选项中" + (osIndex + 1) + "行租金为空");
                importRoom = new ImportRoom();
                importRoom.setRoomNum(os[0].toString());
                importRoom.setFloor(getImportFloor(floors, os[1].toString(), os[2].toString()));
                importRoom.setLayer(Integer.parseInt(os[3].toString()));
                importRoom.setSection(os[4].toString());
                importRoom.setBuiltUpArea(Double.parseDouble(os[5].toString()));
                importRoom.setRoomSubType(os[9].toString());
                importRoom.setRoomArea(os[10].toString());
                importRoom.setRoomRent(os[11].toString());


                if (os.length > 6 && !StringUtil.isNullOrNone(os[6])) {
                    importRoom.setRoomFeeId(os[7].toString());
                    String feeEndDate = excelDoubleToDate(os[8].toString());
                    importRoom.setFeeEndDate(feeEndDate);
                }
                if (os.length < 7 || StringUtil.isNullOrNone(os[6])) {
                    rooms.add(importRoom);
                    continue;
                }
                importRoom.setImportOwner(getImportOwner(owners, os[6].toString()));
                rooms.add(importRoom);
            } catch (Exception e) {
                logger.error("房屋数据校验失败", e);
                throw new IllegalArgumentException("房屋信息sheet中第" + (osIndex + 1) + "行数据错误，请检查" + e.getLocalizedMessage());
            }
        }
    }

    /**
     * 获取 房屋信息
     *
     * @param workbook
     * @param importFees
     */
    private void getFee(Workbook workbook, List<ImportFee> importFees) {
        Sheet sheet = null;
        sheet = ImportExcelUtils.getSheet(workbook, "费用设置");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportFee importFee = null;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {
            Object[] os = oList.get(osIndex);
            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (StringUtil.isNullOrNone(os[0])) {
                continue;
            }
            Assert.hasValue(os[0], "费用设置选项中" + (osIndex + 1) + "行费用编号为空");
            Assert.hasValue(os[1], "费用设置选项中" + (osIndex + 1) + "行费用类型为空");
            Assert.hasValue(os[2], "费用设置选项中" + (osIndex + 1) + "行收费项目为空");
            Assert.hasValue(os[3], "费用设置选项中" + (osIndex + 1) + "行费用标识为空");
            Assert.hasValue(os[4], "费用设置选项中" + (osIndex + 1) + "行费用类型为空");
            Assert.hasValue(os[5], "费用设置选项中" + (osIndex + 1) + "行缴费周期为空");
            Assert.isInteger(os[5].toString(), "费用设置选项中" + (osIndex + 1) + "行缴费周期不是正整数");
            Assert.hasValue(os[6], "费用设置选项中" + (osIndex + 1) + "行出账类型为空");
            Assert.isDate(os[7].toString(), DateUtil.DATE_FORMATE_STRING_B, "费用设置选项中" + (osIndex + 1) + "行计费起始时间不是有效时间格式 请输入类似2020-06-01");
            Assert.isDate(os[8].toString(), DateUtil.DATE_FORMATE_STRING_B, "费用设置选项中" + (osIndex + 1) + "行计费终止时间不是有效时间格式 请输入类似2037-01-01");
            Assert.hasValue(os[9], "费用设置选项中" + (osIndex + 1) + "行计算公式为空");
            if (!"1001".equals(os[9].toString()) && !"2002".equals(os[9].toString())) {
                throw new IllegalArgumentException("费用设置选项中" + (osIndex + 1) + "行计算公式错误 请填写1001 或者2002");
            }
            Assert.hasValue(os[10], "费用设置选项中" + (osIndex + 1) + "行计费单价为空");
            Assert.hasValue(os[11], "费用设置选项中" + (osIndex + 1) + "行固定费用为空");
            importFee = new ImportFee();
            importFee.setId(os[0].toString());
            importFee.setFeeTypeCd("物业费".equals(os[1]) ? "888800010001" : "888800010002");
            importFee.setFeeName(os[2].toString());
            importFee.setFeeFlag("周期性费用".equals(os[3]) ? "1003006" : "2006012");
            importFee.setPaymentCd("预付费".equals(os[4]) ? "1200" : "2100");
            String billType = "";
            if ("每年1月1日".equals(os[6])) {
                billType = "001";
            } else if ("每月1日".equals(os[6])) {
                billType = "002";
            } else if ("每日".equals(os[6])) {
                billType = "003";
            } else {
                billType = "004";
            }
            importFee.setBillType(billType);
            importFee.setPaymentCycle(os[5].toString());
            importFee.setStartTime(os[7].toString());
            importFee.setEndTime(os[8].toString());
            importFee.setComputingFormula(os[9].toString());
            importFee.setSquarePrice(os[10].toString());
            importFee.setAdditionalAmount(os[11].toString());
            importFees.add(importFee);
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
            try {
                Object[] os = oList.get(osIndex);
                if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                    continue;
                }
                if (StringUtil.isNullOrNone(os[0])) {
                    continue;
                }
                Assert.hasValue(os[0], "业主信息选项中" + (osIndex + 1) + "行业主编号为空");
                Assert.hasValue(os[1], "业主信息选项中" + (osIndex + 1) + "行业主名称为空");
                Assert.hasValue(os[2], "业主信息选项中" + (osIndex + 1) + "行业主性别为空");
                String tel = StringUtil.isNullOrNone(os[4]) ? "19999999999" : os[4].toString();
                String idCard = StringUtil.isNullOrNone(os[5]) ? "10000000000000000001" : os[5].toString();
                String ownerTypeCd = StringUtil.isNullOrNone(os[6]) ? "1001" : os[6].toString();
                String parentOwnerId = StringUtil.isNullOrNone(os[7]) ? os[0].toString() : os[7].toString();

                if (os[4].toString().length() > 11) {
                    throw new IllegalArgumentException(os[1].toString() + " 的手机号超过11位,请核实");
                }
                if (os[5].toString().length() > 18) {
                    throw new IllegalArgumentException(os[1].toString() + " 的身份证超过18位,请核实");
                }

                String age = StringUtil.isNullOrNone(os[3]) ? CommonUtil.getAgeByCertId(idCard) : os[3].toString();
                importOwner = new ImportOwner();
                importOwner.setOwnerNum(os[0].toString());
                importOwner.setOwnerName(os[1].toString());
                importOwner.setSex("男".equals(os[2].toString()) ? "0" : "1");
                importOwner.setAge(Integer.parseInt(age));
                importOwner.setTel(tel);
                importOwner.setIdCard(idCard);
                importOwner.setOwnerTypeCd(ownerTypeCd);
                importOwner.setParentOwnerId(parentOwnerId);
                owners.add(importOwner);
            } catch (Exception e) {
                logger.error("第" + (osIndex + 1) + "行数据出现问题", e);
                throw new IllegalArgumentException("第" + (osIndex + 1) + "行数据出现问题" + e.getLocalizedMessage(), e);
            }
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

            Assert.hasValue(os[0], "楼栋单元选项中" + (osIndex + 1) + "行楼栋号为空");
            Assert.hasValue(os[1], "楼栋单元选项中" + (osIndex + 1) + "行单元编号为空");
            Assert.hasValue(os[2], "楼栋单元选项中" + (osIndex + 1) + "行总楼层为空");
            Assert.hasValue(os[3], "楼栋单元选项中" + (osIndex + 1) + "行是否有电梯为空");
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
