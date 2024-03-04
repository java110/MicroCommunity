package com.java110.dto.store;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 仓库数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class StorehouseDto extends PageDto implements Serializable {

    public static final String SH_TYPE_GROUP = "2806"; //集团仓库
    public static final String SH_TYPE_COMMUNITY = "2807"; //项目仓库

    public static final String SWITCH_ON = "ON";
    public static final String SWITCH_OFF = "OFF";

    private String shDesc;
    private String shType;
    private String shId;
    private String shName;
    private String storeId;
    private String isShow;

    private Date createTime;

    private String statusCd = "0";

    private String purchaseSwitch;
    private String purchaseRafId;
    private String purchaseFlowId;
    private String useSwitch;
    private String useRafId;
    private String useFlowId;
    private String allocationSwitch;
    private String allocationRafId;
    private String allocationFlowId;

    private String purchaseFlowName;
    private String useFlowName;
    private String allocationFlowName;

    private String communityId;

    private String allowPurchase;

    private String allowUse;

    public String getShDesc() {
        return shDesc;
    }

    public void setShDesc(String shDesc) {
        this.shDesc = shDesc;
    }

    public String getShType() {
        return shType;
    }

    public void setShType(String shType) {
        this.shType = shType;
    }


    public String getShId() {
        return shId;
    }

    public void setShId(String shId) {
        this.shId = shId;
    }

    public String getShName() {
        return shName;
    }

    public void setShName(String shName) {
        this.shName = shName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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


    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }


    public String getPurchaseSwitch() {
        return purchaseSwitch;
    }

    public void setPurchaseSwitch(String purchaseSwitch) {
        this.purchaseSwitch = purchaseSwitch;
    }

    public String getPurchaseRafId() {
        return purchaseRafId;
    }

    public void setPurchaseRafId(String purchaseRafId) {
        this.purchaseRafId = purchaseRafId;
    }

    public String getUseSwitch() {
        return useSwitch;
    }

    public void setUseSwitch(String useSwitch) {
        this.useSwitch = useSwitch;
    }

    public String getUseRafId() {
        return useRafId;
    }

    public void setUseRafId(String useRafId) {
        this.useRafId = useRafId;
    }

    public String getAllocationSwitch() {
        return allocationSwitch;
    }

    public void setAllocationSwitch(String allocationSwitch) {
        this.allocationSwitch = allocationSwitch;
    }

    public String getAllocationRafId() {
        return allocationRafId;
    }

    public void setAllocationRafId(String allocationRafId) {
        this.allocationRafId = allocationRafId;
    }

    public String getPurchaseFlowName() {
        return purchaseFlowName;
    }

    public void setPurchaseFlowName(String purchaseFlowName) {
        this.purchaseFlowName = purchaseFlowName;
    }

    public String getUseFlowName() {
        return useFlowName;
    }

    public void setUseFlowName(String useFlowName) {
        this.useFlowName = useFlowName;
    }

    public String getAllocationFlowName() {
        return allocationFlowName;
    }

    public void setAllocationFlowName(String allocationFlowName) {
        this.allocationFlowName = allocationFlowName;
    }

    public String getPurchaseFlowId() {
        return purchaseFlowId;
    }

    public void setPurchaseFlowId(String purchaseFlowId) {
        this.purchaseFlowId = purchaseFlowId;
    }

    public String getUseFlowId() {
        return useFlowId;
    }

    public void setUseFlowId(String useFlowId) {
        this.useFlowId = useFlowId;
    }

    public String getAllocationFlowId() {
        return allocationFlowId;
    }

    public void setAllocationFlowId(String allocationFlowId) {
        this.allocationFlowId = allocationFlowId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getAllowPurchase() {
        return allowPurchase;
    }

    public void setAllowPurchase(String allowPurchase) {
        this.allowPurchase = allowPurchase;
    }

    public String getAllowUse() {
        return allowUse;
    }

    public void setAllowUse(String allowUse) {
        this.allowUse = allowUse;
    }
}
