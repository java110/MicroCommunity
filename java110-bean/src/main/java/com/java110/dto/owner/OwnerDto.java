package com.java110.dto.owner;

import com.java110.dto.PageDto;
import com.java110.dto.RoomDto;

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
    public static final String OWNER_TYPE_CD_TEMP = "1004"; //临时人员
    public static final String OWNER_TYPE_CD_OTHER = "1005"; //其他


    public static final String OWNER_FLAG_TRUE = "T";//业主标识 T是真实业主 F 是虚拟业主
    public static final String OWNER_FLAG_FALSE = "F";//业主标识 T是真实业主 F 是虚拟业主

    // 1000 表示待审核，2000 审核完成  3000 拒绝审核
    public static final String STATE_FINISH = "2000";

    public static final String IS_BIND_Y="Y";
    public static final String IS_BIND_N="N";


    private String communityId;
    private String communityName;
    private String sCommunityTel;

    private String communityQrCode;

    private String roomId;
    private String[] roomIds;
    private String sex;
    private String name;
    private String nameLike;
    private String link;
    private String address;
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
    private String roomName;
    private String psId;
    private String num;
    private String idCard;
    private String floorId;
    private String floorNum;
    private String unitId;
    private String unitNum;
    private String state;
    private String startTime;
    private String endTime;
    private String appUserName;


    private String bId;

    private String userName;
    private String ownerFlag;
    private String operate;

    private String startDate;
    private String endDate;

    private List<OwnerAttrDto> ownerAttrDtos;

    private List<RoomDto> rooms;

    private Date createTime;

    private String statusCd = "0";

    private String isBind;

    private String msg;

    private List<String> urls;

    private String url;

    private String roomCount;
    private String memberCount;
    private String carCount;
    private String complaintCount;
    private String repairCount;

    private String oweFee;
    private String contractCount;

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

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOwnerFlag() {
        return ownerFlag;
    }

    public void setOwnerFlag(String ownerFlag) {
        this.ownerFlag = ownerFlag;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getsCommunityTel() {
        return sCommunityTel;
    }

    public void setsCommunityTel(String sCommunityTel) {
        this.sCommunityTel = sCommunityTel;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommunityQrCode() {
        return communityQrCode;
    }

    public void setCommunityQrCode(String communityQrCode) {
        this.communityQrCode = communityQrCode;
    }


    public String getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(String roomCount) {
        this.roomCount = roomCount;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getCarCount() {
        return carCount;
    }

    public void setCarCount(String carCount) {
        this.carCount = carCount;
    }

    public String getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(String complaintCount) {
        this.complaintCount = complaintCount;
    }

    public String getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(String repairCount) {
        this.repairCount = repairCount;
    }

    public String getOweFee() {
        return oweFee;
    }

    public void setOweFee(String oweFee) {
        this.oweFee = oweFee;
    }

    public String getContractCount() {
        return contractCount;
    }

    public void setContractCount(String contractCount) {
        this.contractCount = contractCount;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
