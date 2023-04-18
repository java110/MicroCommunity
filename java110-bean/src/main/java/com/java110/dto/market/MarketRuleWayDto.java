package com.java110.dto.market;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销规则方式数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketRuleWayDto extends PageDto implements Serializable {

    public static final String WAY_TYPE_TEXT = "1001";
    public static final String WAY_TYPE_PIC = "2002";
    public static final String WAY_TYPE_GOODS = "3003";
            //营销方式类型 1001 文本 2002 图片 3003 商品

    private String wayId;
    private String wayType;
    private String[] wayTypes;
    private String wayTypeName;
    private String wayObjId;
    private String remark;
    private String ruleId;
    private String[] ruleIds;

    private String textName;
    private String picName;
    private String goodsName;


    private Date createTime;

    private String statusCd = "0";


    public String getWayId() {
        return wayId;
    }

    public void setWayId(String wayId) {
        this.wayId = wayId;
    }

    public String getWayType() {
        return wayType;
    }

    public void setWayType(String wayType) {
        this.wayType = wayType;
    }

    public String getWayObjId() {
        return wayObjId;
    }

    public void setWayObjId(String wayObjId) {
        this.wayObjId = wayObjId;
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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getWayTypeName() {
        return wayTypeName;
    }

    public void setWayTypeName(String wayTypeName) {
        this.wayTypeName = wayTypeName;
    }

    public String[] getWayTypes() {
        return wayTypes;
    }

    public void setWayTypes(String[] wayTypes) {
        this.wayTypes = wayTypes;
    }

    public String[] getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String[] ruleIds) {
        this.ruleIds = ruleIds;
    }
}
