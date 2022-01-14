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
package com.java110.po.parkingSpaceApply;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2021-10-18 13:00:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class ParkingSpaceApplyPo implements Serializable {

    private String carBrand;
private String applyPersonName;
private String carNum;
private String psId;
private String remark;
private String statusCd = "0";
private String applyId;
private String carColor;
private String carType;
private String configId;
private String applyPersonLink;
private String startTime;
private String applyPersonId;
private String endTime;
private String state;
private String feeId;
private String communityId;
public String getCarBrand() {
        return carBrand;
    }
public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
public String getApplyPersonName() {
        return applyPersonName;
    }
public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }
public String getCarNum() {
        return carNum;
    }
public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
public String getPsId() {
        return psId;
    }
public void setPsId(String psId) {
        this.psId = psId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getApplyId() {
        return applyId;
    }
public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
public String getCarColor() {
        return carColor;
    }
public void setCarColor(String carColor) {
        this.carColor = carColor;
    }
public String getCarType() {
        return carType;
    }
public void setCarType(String carType) {
        this.carType = carType;
    }
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
    }
public String getApplyPersonLink() {
        return applyPersonLink;
    }
public void setApplyPersonLink(String applyPersonLink) {
        this.applyPersonLink = applyPersonLink;
    }
public String getStartTime() {
        return startTime;
    }
public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
public String getApplyPersonId() {
        return applyPersonId;
    }
public void setApplyPersonId(String applyPersonId) {
        this.applyPersonId = applyPersonId;
    }
public String getEndTime() {
        return endTime;
    }
public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
}
