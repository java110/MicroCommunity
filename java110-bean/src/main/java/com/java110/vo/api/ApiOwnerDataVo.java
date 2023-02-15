package com.java110.vo.api;

import com.java110.dto.owner.OwnerAttrDto;
import com.java110.vo.Vo;

import java.util.List;

/**
 * @ClassName ApiFloorDataVo
 * @Description TODO
 * @Author wuxw
 * @Date 2019/4/24 11:18
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ApiOwnerDataVo extends Vo {
    /**
     * floorId
     */
    private String ownerId;

    private String memberId;

    private String name;

    private String sex;

    private String age;

    private String link;

    private String address;

    private String remark;

    private String userName;

    private String createTime;

    private String ownerTypeCd;
    private String ownerTypeName;

    private String roomName;

    private List<OwnerAttrDto> ownerAttrDtos;

    private String idCard;

    private List<String> urls;

    private String url;


    private long roomCount;
    private long memberCount;
    private long carCount;
    private long complaintCount;
    private long repairCount;

    private double oweFee;

    private String contractCount;
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOwnerTypeCd() {
        return ownerTypeCd;
    }

    public void setOwnerTypeCd(String ownerTypeCd) {
        this.ownerTypeCd = ownerTypeCd;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOwnerTypeName() {
        return ownerTypeName;
    }

    public void setOwnerTypeName(String ownerTypeName) {
        this.ownerTypeName = ownerTypeName;
    }

    public List<OwnerAttrDto> getOwnerAttrDtos() {
        return ownerAttrDtos;
    }

    public void setOwnerAttrDtos(List<OwnerAttrDto> ownerAttrDtos) {
        this.ownerAttrDtos = ownerAttrDtos;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public long getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(long roomCount) {
        this.roomCount = roomCount;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(long memberCount) {
        this.memberCount = memberCount;
    }

    public long getCarCount() {
        return carCount;
    }

    public void setCarCount(long carCount) {
        this.carCount = carCount;
    }

    public long getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(long complaintCount) {
        this.complaintCount = complaintCount;
    }

    public long getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(long repairCount) {
        this.repairCount = repairCount;
    }

    public double getOweFee() {
        return oweFee;
    }

    public void setOweFee(double oweFee) {
        this.oweFee = oweFee;
    }

    public String getContractCount() {
        return contractCount;
    }

    public void setContractCount(String contractCount) {
        this.contractCount = contractCount;
    }
}
