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
package com.java110.po.wechatSubscribe;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-02-22 16:17:23 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class WechatSubscribePo implements Serializable {

    private String subId;
private String openId;
private String appId;
private String nickname;
private String headimgurl;
private String statusCd = "0";
private String userId;
private String openType;
public String getSubId() {
        return subId;
    }
public void setSubId(String subId) {
        this.subId = subId;
    }
public String getOpenId() {
        return openId;
    }
public void setOpenId(String openId) {
        this.openId = openId;
    }
public String getAppId() {
        return appId;
    }
public void setAppId(String appId) {
        this.appId = appId;
    }
public String getNickname() {
        return nickname;
    }
public void setNickname(String nickname) {
        this.nickname = nickname;
    }
public String getHeadimgurl() {
        return headimgurl;
    }
public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getOpenType() {
        return openType;
    }
public void setOpenType(String openType) {
        this.openType = openType;
    }



}
