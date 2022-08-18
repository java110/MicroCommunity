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
package com.java110.po.systemInfo;

import java.io.Serializable;

/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-08-16 23:57:44 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class SystemInfoPo implements Serializable {

    private String imgUrl;
    private String systemId;
    private String ownerTitle;
    private String defaultCommunityId;
    private String systemTitle;

    private String systemSimpleTitle;
    private String qqMapKey;
    private String subSystemTitle;
    private String companyName;
    private String mallUrl;
    private String statusCd = "0";
    private String logoUrl;
    private String propertyTitle;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getOwnerTitle() {
        return ownerTitle;
    }

    public void setOwnerTitle(String ownerTitle) {
        this.ownerTitle = ownerTitle;
    }

    public String getDefaultCommunityId() {
        return defaultCommunityId;
    }

    public void setDefaultCommunityId(String defaultCommunityId) {
        this.defaultCommunityId = defaultCommunityId;
    }

    public String getSystemTitle() {
        return systemTitle;
    }

    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }

    public String getQqMapKey() {
        return qqMapKey;
    }

    public void setQqMapKey(String qqMapKey) {
        this.qqMapKey = qqMapKey;
    }

    public String getSubSystemTitle() {
        return subSystemTitle;
    }

    public void setSubSystemTitle(String subSystemTitle) {
        this.subSystemTitle = subSystemTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMallUrl() {
        return mallUrl;
    }

    public void setMallUrl(String mallUrl) {
        this.mallUrl = mallUrl;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }


    public String getSystemSimpleTitle() {
        return systemSimpleTitle;
    }

    public void setSystemSimpleTitle(String systemSimpleTitle) {
        this.systemSimpleTitle = systemSimpleTitle;
    }
}
