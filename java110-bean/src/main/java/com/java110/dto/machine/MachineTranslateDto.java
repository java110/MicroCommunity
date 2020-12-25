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

    //小区信息
    public static final String TYPE_COMMUNITY = "9988";
    public static final String TYPE_MACHINE = "3344";
    public static final String TYPE_OWNER = "8899";

    //同步状态
    public static final String STATE_SUCCESS = "20000";

    //同步失败
    public static final String STATE_ERROR = "60000";


    private String machineId;
    private String machineCode;
    private String machineName;
    private String typeCd;
    private String typeCdName;
    private String machineTranslateId;
    private String objId;
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
}
