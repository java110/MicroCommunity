package com.java110.entity.assetImport;


/**
 * 业主导入 实体
 *
 * add by wuxw 2019-09-24
 */
public class ImportOwner {

    private String ownerId;

    private String ownerNum;

    private String ownerName;

    private String sex;

    private int age;

    private String tel;

    private String idCard;

    private String ownerTypeCd;

    private String parentOwnerId;

    public String getOwnerNum() {
        return ownerNum;
    }

    public void setOwnerNum(String ownerNum) {
        this.ownerNum = ownerNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getParentOwnerId() {
        return parentOwnerId;
    }

    public void setParentOwnerId(String parentOwnerId) {
        this.parentOwnerId = parentOwnerId;
    }
}
