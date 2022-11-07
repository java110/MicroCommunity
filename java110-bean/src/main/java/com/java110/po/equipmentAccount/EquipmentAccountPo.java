/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.po.equipmentAccount;

import java.io.Serializable;

/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-09-10 20:37:25 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class EquipmentAccountPo implements Serializable {

    private String useOrgName;
    private String locationTypeCd;
    private String remark;
    private String purchasePrice;
    private String chargeOrgName;
    private String machineName;
    private String chargeOrgId;
    private String locationDetail;
    private String firstEnableTime;
    private String warrantyDeadline;
    private String model;
    private String state;
    private String stateName;
    private String communityId;
    private String brand;
    private String machineTypeCd;
    private String machineCode;
    private String useUserName;
    private String importanceLevel;
    private String useOrgId;
    private String useUseTel;
    private String machineVersion;
    private String statusCd = "0";
    private String chargeUseName;
    private String usefulLife;
    private String machineId;
    private String chargeUseId;
    private String netWorth;
    private String chargeOrgTel;
    private String typeId;
    private String locationObjId;
    private String locationObjName;
    private String useUserId;

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

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
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

    public String getLocationObjName() {
        return locationObjName;
    }

    public void setLocationObjName(String locationObjName) {
        this.locationObjName = locationObjName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
