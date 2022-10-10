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
package com.java110.po.shoreShop;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-10-10 17:52:53 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class ShoreShopPo implements Serializable {

    private String shopDesc;
private String returnPerson;
private String shopLogo;
private String shopName;
private String statusCd = "0";
private String storeId;
private String mapY;
private String mapX;
private String sendAddress;
private String returnAddress;
private String openType;
private String areaCode;
private String returnLink;
private String shopId;
private String state;
private String shopType;
public String getShopDesc() {
        return shopDesc;
    }
public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }
public String getReturnPerson() {
        return returnPerson;
    }
public void setReturnPerson(String returnPerson) {
        this.returnPerson = returnPerson;
    }
public String getShopLogo() {
        return shopLogo;
    }
public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }
public String getShopName() {
        return shopName;
    }
public void setShopName(String shopName) {
        this.shopName = shopName;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getMapY() {
        return mapY;
    }
public void setMapY(String mapY) {
        this.mapY = mapY;
    }
public String getMapX() {
        return mapX;
    }
public void setMapX(String mapX) {
        this.mapX = mapX;
    }
public String getSendAddress() {
        return sendAddress;
    }
public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }
public String getReturnAddress() {
        return returnAddress;
    }
public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }
public String getOpenType() {
        return openType;
    }
public void setOpenType(String openType) {
        this.openType = openType;
    }
public String getAreaCode() {
        return areaCode;
    }
public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
public String getReturnLink() {
        return returnLink;
    }
public void setReturnLink(String returnLink) {
        this.returnLink = returnLink;
    }
public String getShopId() {
        return shopId;
    }
public void setShopId(String shopId) {
        this.shopId = shopId;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getShopType() {
        return shopType;
    }
public void setShopType(String shopType) {
        this.shopType = shopType;
    }



}
