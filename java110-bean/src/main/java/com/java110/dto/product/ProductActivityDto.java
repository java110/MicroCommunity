package com.java110.dto.product;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

public class ProductActivityDto extends PageDto implements Serializable {

    public static final String ACT_TYPE_GROUP ="GROUP"; //团购
    public static final String ACT_TYPE_SKILL ="SKILL"; //秒杀



    private String actType;
    private double actPrice; // 活动价格
    private double actStock; // 活动库存
    private double actSales; // 活动销量
    private Date actEndTime; // 活动价格
    private String actProdName; // 活动名称
    private String actProdDesc; // 活动描述

    public double getActPrice() {
        return actPrice;
    }

    public void setActPrice(double actPrice) {
        this.actPrice = actPrice;
    }

    public double getActStock() {
        return actStock;
    }

    public void setActStock(double actStock) {
        this.actStock = actStock;
    }

    public double getActSales() {
        return actSales;
    }

    public void setActSales(double actSales) {
        this.actSales = actSales;
    }

    public Date getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(Date actEndTime) {
        this.actEndTime = actEndTime;
    }

    public String getActProdName() {
        return actProdName;
    }

    public void setActProdName(String actProdName) {
        this.actProdName = actProdName;
    }

    public String getActProdDesc() {
        return actProdDesc;
    }

    public void setActProdDesc(String actProdDesc) {
        this.actProdDesc = actProdDesc;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }
}
