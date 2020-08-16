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


    private String remark;

    private String userName;

    private String createTime;

    private String ownerTypeCd;
    private String ownerTypeName;

    private List<OwnerAttrDto> ownerAttrDtos;

    private String idCard;

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
}
