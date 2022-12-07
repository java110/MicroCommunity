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
package com.java110.po.reserveGoods;

import java.io.Serializable;
import java.util.Date;
/**
 * 类表述： Po 数据模型实体对象 基本保持与数据库模型一直 用于 增加修改删除 等时的数据载体
 * add by 吴学文 at 2022-12-05 18:25:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
public class ReserveGoodsPo implements Serializable {

    private String endDate;
private String goodsId;
private String statusCd = "0";
private String sort;
private String type;
private String imgUrl;
private String catalogId;
private String paramsId;
private String price;
private String state;
private String communityId;
private String goodsName;
private String startDate;
private String goodsDesc;
public String getEndDate() {
        return endDate;
    }
public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
public String getGoodsId() {
        return goodsId;
    }
public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
public String getStatusCd() {
        return statusCd;
    }
public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
public String getSort() {
        return sort;
    }
public void setSort(String sort) {
        this.sort = sort;
    }
public String getType() {
        return type;
    }
public void setType(String type) {
        this.type = type;
    }
public String getImgUrl() {
        return imgUrl;
    }
public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
public String getCatalogId() {
        return catalogId;
    }
public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }
public String getParamsId() {
        return paramsId;
    }
public void setParamsId(String paramsId) {
        this.paramsId = paramsId;
    }
public String getPrice() {
        return price;
    }
public void setPrice(String price) {
        this.price = price;
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
public String getGoodsName() {
        return goodsName;
    }
public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
public String getStartDate() {
        return startDate;
    }
public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
public String getGoodsDesc() {
        return goodsDesc;
    }
public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }



}
