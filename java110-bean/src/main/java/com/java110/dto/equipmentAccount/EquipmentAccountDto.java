package com.java110.dto.equipmentAccount;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 设备台账数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class EquipmentAccountDto extends PageDto implements Serializable {

    private String useOrgName;
    private String locationTypeCd;
    private String remark;
    private String purchasePrice;
    private String chargeOrgName;
    private String machineName;
    private String machineNameLike;
    private String chargeOrgId;
    private String locationDetail;
    private String firstEnableTime;
    private String warrantyDeadline;
    private String model;
    private String state;
    private String communityId;
    private String brand;
    private String machineTypeCd;
    private String machineCode;
    private String useUserName;
    private String importanceLevel;
    private String useOrgId;
    private String useUseTel;
    private String machineVersion;
    private String chargeUseName;
    private String usefulLife;
    private String machineId;
    private String chargeUseId;
    private String netWorth;
    private String chargeOrgTel;
    private String typeId;
    private String machineTypeName;
    private String locationObjId;
    private String locationObjName;
    private String useUserId;
    private String stateName;
    private String levelName;
    private String []machineIds;
    private Date createTime;

    private String statusCd = "0";

    private String url;


    public String getUseOrgName() {
        return useOrgName;
    }

    public void setUseOrgName(String useOrgName) {
        this.useOrgName = useOrgName;
    }

    public String getLocationTypeCd() {
        return locationTypeCd;
    }

    public void setLocationTypeCd(String locationTypeCd) {
        this.locationTypeCd = locationTypeCd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getChargeOrgName() {
        return chargeOrgName;
    }

    public void setChargeOrgName(String chargeOrgName) {
        this.chargeOrgName = chargeOrgName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getChargeOrgId() {
        return chargeOrgId;
    }

    public void setChargeOrgId(String chargeOrgId) {
        this.chargeOrgId = chargeOrgId;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public String getFirstEnableTime() {
        return firstEnableTime;
    }

    public void setFirstEnableTime(String firstEnableTime) {
        this.firstEnableTime = firstEnableTime;
    }

    public String getWarrantyDeadline() {
        return warrantyDeadline;
    }

    public void setWarrantyDeadline(String warrantyDeadline) {
        this.warrantyDeadline = warrantyDeadline;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMachineTypeCd() {
        return machineTypeCd;
    }

    public void setMachineTypeCd(String machineTypeCd) {
        this.machineTypeCd = machineTypeCd;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getUseUserName() {
        return useUserName;
    }

    public void setUseUserName(String useUserName) {
        this.useUserName = useUserName;
    }

    public String getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(String importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    public String getUseOrgId() {
        return useOrgId;
    }

    public void setUseOrgId(String useOrgId) {
        this.useOrgId = useOrgId;
    }

    public String getUseUseTel() {
        return useUseTel;
    }

    public void setUseUseTel(String useUseTel) {
        this.useUseTel = useUseTel;
    }

    public String getMachineVersion() {
        return machineVersion;
    }

    public void setMachineVersion(String machineVersion) {
        this.machineVersion = machineVersion;
    }

    public String getChargeUseName() {
        return chargeUseName;
    }

    public void setChargeUseName(String chargeUseName) {
        this.chargeUseName = chargeUseName;
    }

    public String getUsefulLife() {
        return usefulLife;
    }

    public void setUsefulLife(String usefulLife) {
        this.usefulLife = usefulLife;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getChargeUseId() {
        return chargeUseId;
    }

    public void setChargeUseId(String chargeUseId) {
        this.chargeUseId = chargeUseId;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public String getChargeOrgTel() {
        return chargeOrgTel;
    }

    public void setChargeOrgTel(String chargeOrgTel) {
        this.chargeOrgTel = chargeOrgTel;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getLocationObjId() {
        return locationObjId;
    }

    public void setLocationObjId(String locationObjId) {
        this.locationObjId = locationObjId;
    }

    public String getUseUserId() {
        return useUserId;
    }

    public void setUseUserId(String useUserId) {
        this.useUserId = useUserId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
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

    public String getLocationObjName() {
        return locationObjName;
    }

    public void setLocationObjName(String locationObjName) {
        this.locationObjName = locationObjName;
    }

    public String[] getMachineIds() {
        return machineIds;
    }

    public void setMachineIds(String[] machineIds) {
        this.machineIds = machineIds;
    }

    public String getMachineTypeName() {
        return machineTypeName;
    }

    public void setMachineTypeName(String machineTypeName) {
        this.machineTypeName = machineTypeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMachineNameLike() {
        return machineNameLike;
    }

    public void setMachineNameLike(String machineNameLike) {
        this.machineNameLike = machineNameLike;
    }
}
