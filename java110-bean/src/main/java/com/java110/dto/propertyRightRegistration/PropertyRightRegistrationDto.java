package com.java110.dto.propertyRightRegistration;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 房屋产权数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PropertyRightRegistrationDto extends PageDto implements Serializable {

    private String address;
    private String prrId;
    private String idCard;
    private String name;
    private String link;
    private String createUser;
    private String roomId;
    private String floorId;
    private String unitId;
    private String roomNum;
    private String unitNum;
    private String floorNum;
    //文件真实名称
    private String fileRealName;
    //文件存储名称
    private String fileSaveName;
    //文件类型
    private String relTypeCd;
    //身份证图片地址
    private String idCardUrl;
    //购房合同图片地址
    private String housePurchaseUrl;
    //维修基金图片地址
    private String repairUrl;
    //契税图片地址
    private String deedTaxUrl;

    private Date createTime;

    private String state; //审核状态 0 未审核  1 审核通过  2 审核拒绝
    private String stateName;

    private String remark;
    private String communityId;

    private String statusCd = "0";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrrId() {
        return prrId;
    }

    public void setPrrId(String prrId) {
        this.prrId = prrId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }

    public String getRelTypeCd() {
        return relTypeCd;
    }

    public void setRelTypeCd(String relTypeCd) {
        this.relTypeCd = relTypeCd;
    }

    public String getIdCardUrl() {
        return idCardUrl;
    }

    public void setIdCardUrl(String idCardUrl) {
        this.idCardUrl = idCardUrl;
    }

    public String getHousePurchaseUrl() {
        return housePurchaseUrl;
    }

    public void setHousePurchaseUrl(String housePurchaseUrl) {
        this.housePurchaseUrl = housePurchaseUrl;
    }

    public String getRepairUrl() {
        return repairUrl;
    }

    public void setRepairUrl(String repairUrl) {
        this.repairUrl = repairUrl;
    }

    public String getDeedTaxUrl() {
        return deedTaxUrl;
    }

    public void setDeedTaxUrl(String deedTaxUrl) {
        this.deedTaxUrl = deedTaxUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
