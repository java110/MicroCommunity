package com.java110.dto.machine;

import com.java110.dto.PageDto;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 设备同步数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MachineTranslateDto extends PageDto implements Serializable {

    /**
     * 301	添加设备
     * 302	编辑设备
     * 303	删除设备
     * 401	添加小区
     * 402	编辑小区
     * 403	删除小区
     * 501	添加业主人脸
     * 502	编辑业主人脸
     * 503	删除业主人脸
     */
    public static final String CMD_ADD_MACHINE = "301";
    public static final String CMD_UPDATE_MACHINE = "302";
    public static final String CMD_DELETE_MACHINE = "303";
    public static final String CMD_ADD_COMMUNITY = "401";
    public static final String CMD_UPDATE_COMMUNITY = "402";
    public static final String CMD_DELETE_COMMUNITY = "403";
    public static final String CMD_ADD_OWNER_FACE = "501";
    public static final String CMD_UPDATE_OWNER_FACE = "502";
    public static final String CMD_DELETE_OWNER_FACE = "503";
    public static final String CMD_ADD_PARKING_AREA = "601";
    public static final String CMD_UPDATE_PARKING_AREA = "602";
    public static final String CMD_DELETE_PARKING_AREA = "603";
    public static final String CMD_ADD_PARKING_AREA_TEXT = "604";
    public static final String CMD_ADD_OWNER_CAR = "701";
    public static final String CMD_UPDATE_OWNER_CAR = "702";
    public static final String CMD_DELETE_OWNER_CAR = "703";
    public static final String CMD_ADD_CAR_BLACK_WHITE = "801";
    public static final String CMD_UPDATE_CAR_BLACK_WHITE = "802";
    public static final String CMD_DELETE_CAR_BLACK_WHITE = "803";
    public static final String CMD_ADD_TEAM_CAR_FEE_CONFIG = "901";
    public static final String CMD_UPDATE_TEAM_CAR_FEE_CONFIG = "902";
    public static final String CMD_DELETE_TEAM_CAR_FEE_CONFIG = "903";

    public static final String CMD_ADD_ATTENDANCE_CLASSES = "911";
    public static final String CMD_UPDATE_ATTENDANCE_CLASSES = "912";
    public static final String CMD_DELETE_ATTENDANCE_CLASSES = "913";

    public static final String CMD_ADD_PARKING_COUPON_CAR = "921";
    public static final String CMD_UPDATE_PARKING_COUPON_CAR = "922";
    public static final String CMD_DELETE_PARKING_COUPON_CAR = "923";

    public static final String CMD_ADD_VISIT = "921";

    public static final String CMD_OPEN_DOOR = "5";
    public static final String CMD_CLOSE_DOOR = "51";

    //小区信息
    public static final String TYPE_COMMUNITY = "9988";
    public static final String TYPE_MACHINE = "3344";
    public static final String TYPE_OWNER = "8899";
    public static final String TYPE_PARKING_AREA = "2233";
    public static final String TYPE_OWNER_CAR = "4455";
    public static final String TYPE_TEAM_CAR_FEE_CONFIG = "1122";
    public static final String TYPE_PARK_COUPON_CAR = "1133";
    public static final String TYPE_ATTENDANCE = "1111";


    //同步状态
    public static final String STATE_SUCCESS = "20000";
    public static final String STATE_DOING = "30000";

    //同步失败
    public static final String STATE_ERROR = "60000";


    private String machineId;
    private String machineCode;
    private String machineName;
    private String typeCd;
    private String typeCdName;
    private String machineTranslateId;
    private String objId;

    private String[] objIds;
    private String objName;

    private String state;
    private String stateName;
    private String communityId;
    private String updateTime;

    private String bId;

    private String machineCmd;
    private String machineCmdName;

    private String objBId;

    private String remark;

    private String isNow;


    private Date createTime;

    private String statusCd = "0";


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

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getMachineTranslateId() {
        return machineTranslateId;
    }

    public void setMachineTranslateId(String machineTranslateId) {
        this.machineTranslateId = machineTranslateId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getTypeCdName() {
        return typeCdName;
    }

    public void setTypeCdName(String typeCdName) {
        this.typeCdName = typeCdName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getMachineCmd() {
        return machineCmd;
    }

    public void setMachineCmd(String machineCmd) {
        this.machineCmd = machineCmd;
    }

    public String getObjBId() {
        return objBId;
    }

    public void setObjBId(String objBId) {
        this.objBId = objBId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (!StringUtils.isEmpty(remark) && remark.length() > 190) {
            remark = remark.substring(0, 190);
        }
        this.remark = remark;
    }

    public String getMachineCmdName() {
        return machineCmdName;
    }

    public void setMachineCmdName(String machineCmdName) {
        this.machineCmdName = machineCmdName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getIsNow() {
        return isNow;
    }

    public void setIsNow(String isNow) {
        this.isNow = isNow;
    }

    public String[] getObjIds() {
        return objIds;
    }

    public void setObjIds(String[] objIds) {
        this.objIds = objIds;
    }
}
