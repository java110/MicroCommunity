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
package com.java110.po.communityPublicity;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2023-04-06 18:45:50 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class CommunityPublicityPo implements Serializable {

    private String createUserId;
private String pubId;
private String context;
private String collectCount;
private String pubType;
private String likeCount;
private String createUserName;
private String statusCd = "0";
private String title;
private String communityId;
private String readCount;
private String headerImg;
public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getPubId() {
        return pubId;
    }
public void setPubId(String pubId) {
        this.pubId = pubId;
    }
public String getContext() {
        return context;
    }
public void setContext(String context) {
        this.context = context;
    }
public String getCollectCount() {
        return collectCount;
    }
public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }
public String getPubType() {
        return pubType;
    }
public void setPubType(String pubType) {
        this.pubType = pubType;
    }
public String getLikeCount() {
        return likeCount;
    }
public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }
public String getCreateUserName() {
        return createUserName;
    }
public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getTitle() {
        return title;
    }
public void setTitle(String title) {
        this.title = title;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getReadCount() {
        return readCount;
    }
public void setReadCount(String readCount) {
        this.readCount = readCount;
    }
public String getHeaderImg() {
        return headerImg;
    }
public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }



}
