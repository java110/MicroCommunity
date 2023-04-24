package com.java110.dto.propertyRightRegistration;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 房屋产权证件详情数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PropertyRightRegistrationDetailDto extends PageDto implements Serializable {

    public static final String ID_CARD = "001"; // 001身份证   002购房合同   003维修基金   004契税
    public static final String HOUSE_PURCHASE = "002"; // 001身份证   002购房合同   003维修基金   004契税
    public static final String REPAIR_MONEY = "003"; // 001身份证   002购房合同   003维修基金   004契税
    public static final String DEED_TAX = "004"; // 001身份证   002购房合同   003维修基金   004契税

    public static final String STATE_NO = "0";
    public static final String STATE_ADOPT = "1";
    public static final String STATE_FAIL = "2";

    private String prrId;
    private String prrdId;
    private String createUser;
    private String state;
    private String securities;
    private String securitiesName;
    private String isTrue;
    //身份证图片地址
    private String idCardUrl;
    //购房合同图片地址
    private String housePurchaseUrl;
    //维修基金图片地址
    private String repairUrl;
    //契税图片地址
    private String deedTaxUrl;

    private Date createTime;

    private String statusCd = "0";

    public String getPrrId() {
        return prrId;
    }

    public void setPrrId(String prrId) {
        this.prrId = prrId;
    }

    public String getPrrdId() {
        return prrdId;
    }

    public void setPrrdId(String prrdId) {
        this.prrdId = prrdId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSecurities() {
        return securities;
    }

    public void setSecurities(String securities) {
        this.securities = securities;
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

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    public String getSecuritiesName() {
        return securitiesName;
    }

    public void setSecuritiesName(String securitiesName) {
        this.securitiesName = securitiesName;
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
}
