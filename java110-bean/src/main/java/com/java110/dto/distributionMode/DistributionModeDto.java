package com.java110.dto.distributionMode;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 派送方式数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class DistributionModeDto extends PageDto implements Serializable {

    public static final String DOMAIN_AMLL = "MALL";
    public static final String FEE_MODE_TYPE = "FEE_MODE_TYPE";
    public static final String FEE_MODE_TYPE_T = "T";
    public static final String FEE_MODE_TYPE_M = "M";
    public static final String MODE_TYPE_USER = "12001" ; // 到店自提
    public static final String MODE_TYPE_STORE = "12002" ; //商家配送

    private String modeName;
    private String modeId;
    private String[] modeIds;
    private String startDeliveryOut;
    private String startDeliveryIn;
    private String maximumDistribution;
    private String fixedFee;
    private String shopId;
    private String[] shopIds;
    private String modeType;



    private Date createTime;

    private String statusCd = "0";


    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }

    public String getStartDeliveryOut() {
        return startDeliveryOut;
    }

    public void setStartDeliveryOut(String startDeliveryOut) {
        this.startDeliveryOut = startDeliveryOut;
    }

    public String getStartDeliveryIn() {
        return startDeliveryIn;
    }

    public void setStartDeliveryIn(String startDeliveryIn) {
        this.startDeliveryIn = startDeliveryIn;
    }

    public String getMaximumDistribution() {
        return maximumDistribution;
    }

    public void setMaximumDistribution(String maximumDistribution) {
        this.maximumDistribution = maximumDistribution;
    }

    public String getFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(String fixedFee) {
        this.fixedFee = fixedFee;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String[] getModeIds() {
        return modeIds;
    }

    public void setModeIds(String[] modeIds) {
        this.modeIds = modeIds;
    }

    public String[] getShopIds() {
        return shopIds;
    }

    public void setShopIds(String[] shopIds) {
        this.shopIds = shopIds;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }
}
