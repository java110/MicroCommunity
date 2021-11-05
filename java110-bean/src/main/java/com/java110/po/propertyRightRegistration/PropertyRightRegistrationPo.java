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
package com.java110.po.propertyRightRegistration;

import java.io.Serializable;
import java.util.List;

/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2021-10-09 10:34:14 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class PropertyRightRegistrationPo implements Serializable {

    private String address;
    private String prrId;
    private String idCard;
    private String name;
    private String link;
    private String createUser;
    private String createTime;
    private String statusCd = "0";
    private String roomId;
    private List<String> idCardPhotos;
    private List<String> housePurchasePhotos;
    private List<String> repairPhotos;
    private List<String> deedTaxPhotos;
    private String state; //审核状态 0 未审核  1 审核通过  2 审核拒绝
    private String stateName;
    private String remark;
    private String communityId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrrId() {
        return prrId;
    }

    public void setPrrId(String prrId) {
        this.prrId = prrId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<String> getIdCardPhotos() {
        return idCardPhotos;
    }

    public void setIdCardPhotos(List<String> idCardPhotos) {
        this.idCardPhotos = idCardPhotos;
    }

    public List<String> getHousePurchasePhotos() {
        return housePurchasePhotos;
    }

    public void setHousePurchasePhotos(List<String> housePurchasePhotos) {
        this.housePurchasePhotos = housePurchasePhotos;
    }

    public List<String> getRepairPhotos() {
        return repairPhotos;
    }

    public void setRepairPhotos(List<String> repairPhotos) {
        this.repairPhotos = repairPhotos;
    }

    public List<String> getDeedTaxPhotos() {
        return deedTaxPhotos;
    }

    public void setDeedTaxPhotos(List<String> deedTaxPhotos) {
        this.deedTaxPhotos = deedTaxPhotos;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
