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
package com.java110.po.log;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2023-06-19 00:08:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class AssetImportOwnerRoomPo implements Serializable {

    private String roomState;
private String idCard;
private String ownerTypeCd;
private String detailId;
private String unitNum;
private String section;
private String remark;
private String floorNum;
private String layer;
private String roomNum;
private String ownerName;
private String logId;
private String tel;
private String state;
private String communityId;
private String roomRent;
private String layerCount;
private String sex;
private String roomSubType;
private String roomArea;
private String statusCd = "0";
private String builtUpArea;
private String left;
private String importUserId;
private String age;
public String getRoomState() {
        return roomState;
    }
public void setRoomState(String roomState) {
        this.roomState = roomState;
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
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
public String getUnitNum() {
        return unitNum;
    }
public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }
public String getSection() {
        return section;
    }
public void setSection(String section) {
        this.section = section;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getFloorNum() {
        return floorNum;
    }
public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }
public String getLayer() {
        return layer;
    }
public void setLayer(String layer) {
        this.layer = layer;
    }
public String getRoomNum() {
        return roomNum;
    }
public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }
public String getOwnerName() {
        return ownerName;
    }
public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
public String getLogId() {
        return logId;
    }
public void setLogId(String logId) {
        this.logId = logId;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
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
public String getRoomRent() {
        return roomRent;
    }
public void setRoomRent(String roomRent) {
        this.roomRent = roomRent;
    }
public String getLayerCount() {
        return layerCount;
    }
public void setLayerCount(String layerCount) {
        this.layerCount = layerCount;
    }
public String getSex() {
        return sex;
    }
public void setSex(String sex) {
        this.sex = sex;
    }
public String getRoomSubType() {
        return roomSubType;
    }
public void setRoomSubType(String roomSubType) {
        this.roomSubType = roomSubType;
    }
public String getRoomArea() {
        return roomArea;
    }
public void setRoomArea(String roomArea) {
        this.roomArea = roomArea;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getBuiltUpArea() {
        return builtUpArea;
    }
public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }
public String getLeft() {
        return left;
    }
public void setLeft(String left) {
        this.left = left;
    }
public String getImportUserId() {
        return importUserId;
    }
public void setImportUserId(String importUserId) {
        this.importUserId = importUserId;
    }
public String getAge() {
        return age;
    }
public void setAge(String age) {
        this.age = age;
    }



}
