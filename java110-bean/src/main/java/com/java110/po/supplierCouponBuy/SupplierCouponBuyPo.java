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
package com.java110.po.supplierCouponBuy;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-11-17 21:39:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class SupplierCouponBuyPo implements Serializable {

    private String createUserId;
private String supplierId;
private String quantity;
private String createUserName;
private String statusCd = "0";
private String remark;
private String objName;
private String couponId;
private String storeId;
private String valuePrice;
private String createUserTel;
private String name;
private String objId;
private String buyId;
private String storeName;
public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getSupplierId() {
        return supplierId;
    }
public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
public String getQuantity() {
        return quantity;
    }
public void setQuantity(String quantity) {
        this.quantity = quantity;
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
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getObjName() {
        return objName;
    }
public void setObjName(String objName) {
        this.objName = objName;
    }
public String getCouponId() {
        return couponId;
    }
public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getValuePrice() {
        return valuePrice;
    }
public void setValuePrice(String valuePrice) {
        this.valuePrice = valuePrice;
    }
public String getCreateUserTel() {
        return createUserTel;
    }
public void setCreateUserTel(String createUserTel) {
        this.createUserTel = createUserTel;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getObjId() {
        return objId;
    }
public void setObjId(String objId) {
        this.objId = objId;
    }
public String getBuyId() {
        return buyId;
    }
public void setBuyId(String buyId) {
        this.buyId = buyId;
    }
public String getStoreName() {
        return storeName;
    }
public void setStoreName(String storeName) {
        this.storeName = storeName;
    }



}
