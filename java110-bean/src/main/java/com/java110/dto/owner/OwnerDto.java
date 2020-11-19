package com.java110.dto.owner;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 业主数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerDto extends PageDto implements Serializable {

    // 业主本人 1002 家庭成员
    public static final String OWNER_TYPE_CD_OWNER = "1001"; //业主
    public static final String OWNER_TYPE_CD_MEMBER = "1002"; //家庭成员
    public static final String OWNER_TYPE_CD_RENTING = "1003"; //租客


    private String communityId;

    private String roomId;
    private String[] roomIds;
    private String sex;
    private String name;
    private String link;
    private String remark;
    private String ownerId;
    private String[] ownerIds;
    private String userId;
    private String age;
    private String memberId;
    private String ownerTypeCd;
    private String[] ownerTypeCds;
    private String ownerTypeName;
    private String roomNum;
    private String psId;
    private String num;
    private String idCard;
    private String floorId;
    private String unitId;
    private String state;

    private String bId;

    private String userName;

    private List<OwnerAttrDto> ownerAttrDtos;


    private Date createTime;

    private String statusCd = "0";


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOwnerTypeCd() {
        return ownerTypeCd;
    }

    public void setOwnerTypeCd(String ownerTypeCd) {
        this.ownerTypeCd = ownerTypeCd;
    }


    public String[] getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(String[] roomIds) {
        this.roomIds = roomIds;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwnerTypeName() {
        return ownerTypeName;
    }

    public void setOwnerTypeName(String ownerTypeName) {
        this.ownerTypeName = ownerTypeName;
    }

    public String[] getOwnerTypeCds() {
        return ownerTypeCds;
    }

    public void setOwnerTypeCds(String[] ownerTypeCds) {
        this.ownerTypeCds = ownerTypeCds;
    }

    public List<OwnerAttrDto> getOwnerAttrDtos() {
        return ownerAttrDtos;
    }

    public void setOwnerAttrDtos(List<OwnerAttrDto> ownerAttrDtos) {
        this.ownerAttrDtos = ownerAttrDtos;
    }

    public String[] getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(String[] ownerIds) {
        this.ownerIds = ownerIds;
    }
}
