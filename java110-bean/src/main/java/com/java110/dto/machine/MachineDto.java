package com.java110.dto.machine;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 设备数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MachineDto extends PageDto implements Serializable {

    public static final String MACHINE_TYPE_CAR = "9996";//自有道闸设备道闸
    public static final String MACHINE_TYPE_CAR_THIRD = "9995";//第三方道闸平台
    public static final String MACHINE_TYPE_ACCESS_CONTROL = "9999";
    public static final String MACHINE_TYPE_ATTENDANCE = "9997"; // 考勤机
    public static final String MACHINE_TYPE_MONITOR = "9998"; // 监控
    public static final String MACHINE_STATE_ON = "1200";
    public static final String MACHINE_STATE_OFF = "1300";

    public static final String DIRECTION_IN = "3306"; //进场
    public static final String DIRECTION_OUT = "3307"; //出场

    private String machineMac;
    private String machineId;
    private String machineCode;
    private String authCode;
    private String machineVersion;
    private String communityId;
    private String machineName;
    private String machineTypeCd;
    private String[] machineTypeCds;
    private String locationType;
    private String machineTypeCdName;
    private String machineIp;
    private String bId;
    private String locationTypeCd;
    private String locationTypeName;
    private String locationObjId;
    private String[] locationObjIds;
    private String state;
    private String stateName;
    private String floorId;
    private String floorNum;
    private String unitId;
    private String unitNum;
    private String roomId;
    private String roomNum;
    private String locationObjName;
    private String direction;//设备方向
    private String directionName;
    private String typeId;
    private String domain;

    private List<MachineAttrDto> machineAttrs;



    private String createTime;

    private String statusCd = "0";
    private String heartbeatTime;


    public String getMachineMac() {
        return machineMac;
    }

    public void setMachineMac(String machineMac) {
        this.machineMac = machineMac;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMachineVersion() {
        return machineVersion;
    }

    public void setMachineVersion(String machineVersion) {
        this.machineVersion = machineVersion;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineTypeCd() {
        return machineTypeCd;
    }

    public void setMachineTypeCd(String machineTypeCd) {
        this.machineTypeCd = machineTypeCd;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getMachineTypeCdName() {
        return machineTypeCdName;
    }

    public void setMachineTypeCdName(String machineTypeCdName) {
        this.machineTypeCdName = machineTypeCdName;
    }


    public String getLocationTypeCd() {
        return locationTypeCd;
    }

    public void setLocationTypeCd(String locationTypeCd) {
        this.locationTypeCd = locationTypeCd;
    }

    public String getLocationObjId() {
        return locationObjId;
    }

    public void setLocationObjId(String locationObjId) {
        this.locationObjId = locationObjId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getLocationObjName() {
        return locationObjName;
    }

    public void setLocationObjName(String locationObjName) {
        this.locationObjName = locationObjName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLocationTypeName() {
        return locationTypeName;
    }

    public void setLocationTypeName(String locationTypeName) {
        this.locationTypeName = locationTypeName;
    }

    public String[] getLocationObjIds() {
        return locationObjIds;
    }

    public void setLocationObjIds(String[] locationObjIds) {
        this.locationObjIds = locationObjIds;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public List<MachineAttrDto> getMachineAttrs() {
        return machineAttrs;
    }

    public void setMachineAttrs(List<MachineAttrDto> machineAttrs) {
        this.machineAttrs = machineAttrs;
    }

    public String getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(String heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String[] getMachineTypeCds() {
        return machineTypeCds;
    }

    public void setMachineTypeCds(String[] machineTypeCds) {
        this.machineTypeCds = machineTypeCds;
    }
}
