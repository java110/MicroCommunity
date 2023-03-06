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
package com.java110.po.ownerCommittee;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2023-03-06 11:57:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class OwnerCommitteePo implements Serializable {

    private String address;
private String appointTime;
private String idCard;
private String postDesc;
private String sex;
private String link;
private String remark;
private String statusCd = "0";
private String curTime;
private String post;
private String name;
private String position;
private String state;
private String ocId;
private String communityId;
public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getAppointTime() {
        return appointTime;
    }
public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }
public String getIdCard() {
        return idCard;
    }
public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
public String getPostDesc() {
        return postDesc;
    }
public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
public String getSex() {
        return sex;
    }
public void setSex(String sex) {
        this.sex = sex;
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
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getCurTime() {
        return curTime;
    }
public void setCurTime(String curTime) {
        this.curTime = curTime;
    }
public String getPost() {
        return post;
    }
public void setPost(String post) {
        this.post = post;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getPosition() {
        return position;
    }
public void setPosition(String position) {
        this.position = position;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getOcId() {
        return ocId;
    }
public void setOcId(String ocId) {
        this.ocId = ocId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }



}
