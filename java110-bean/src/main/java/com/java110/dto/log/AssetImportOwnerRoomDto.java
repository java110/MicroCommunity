package com.java110.dto.log;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 资产导入日志数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AssetImportOwnerRoomDto extends PageDto implements Serializable {

    public static final String STATE_WAIT_IMPORT = "W"; //W 待导入 C 导入成功 F 导入失败
    public static final String STATE_SUCCESS_IMPORT = "C"; //W 待导入 C 导入成功 F 导入失败
    public static final String STATE_FAIL_IMPORT = "F"; //W 待导入 C 导入成功 F 导入失败

    private String roomState;
    private String idCard;
    private String ownerTypeCd;
    private String detailId;
    private String unitNum;
    private String section;
    private String remark;
    private String floorNum;
    private String layer;
    private String roomNum;
    private String ownerName;
    private String logId;
    private String tel;
    private String state;
    private String communityId;
    private String roomRent;
    private String layerCount;
    private String sex;
    private String roomSubType;
    private String roomArea;
    private String builtUpArea;
    private String left;
    private String importUserId;
    private String age;


    private Date createTime;

    private String statusCd = "0";


    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOwnerTypeCd() {
        return ownerTypeCd;
    }

    public void setOwnerTypeCd(String ownerTypeCd) {
        this.ownerTypeCd = ownerTypeCd;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getRoomRent() {
        return roomRent;
    }

    public void setRoomRent(String roomRent) {
        this.roomRent = roomRent;
    }

    public String getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(String layerCount) {
        this.layerCount = layerCount;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRoomSubType() {
        return roomSubType;
    }

    public void setRoomSubType(String roomSubType) {
        this.roomSubType = roomSubType;
    }

    public String getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getImportUserId() {
        return importUserId;
    }

    public void setImportUserId(String importUserId) {
        this.importUserId = importUserId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
}
