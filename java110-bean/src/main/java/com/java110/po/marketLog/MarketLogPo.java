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
package com.java110.po.marketLog;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-10-26 23:06:53 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class MarketLogPo implements Serializable {

    private String personName;
private String sendContent;
private String sendWay;
private String openId;
private String logId;
private String communityName;
private String statusCd = "0";
private String remark;
private String ruleId;
private String communityId;
private String personTel;
private String businessType;
public String getPersonName() {
        return personName;
    }
public void setPersonName(String personName) {
        this.personName = personName;
    }
public String getSendContent() {
        return sendContent;
    }
public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }
public String getSendWay() {
        return sendWay;
    }
public void setSendWay(String sendWay) {
        this.sendWay = sendWay;
    }
public String getOpenId() {
        return openId;
    }
public void setOpenId(String openId) {
        this.openId = openId;
    }
public String getLogId() {
        return logId;
    }
public void setLogId(String logId) {
        this.logId = logId;
    }
public String getCommunityName() {
        return communityName;
    }
public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getPersonTel() {
        return personTel;
    }
public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }
public String getBusinessType() {
        return businessType;
    }
public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }



}
