package com.java110.entity.merchant;

import java.util.Date;
import java.util.List;

/**
 * Created by wuxw on 2017/5/20.
 */
public class Merchant {

    private String merchantId;

    private String logoImg;

    private String name;

    private String address;

    private String type;

    private Date start_dt;

    private Date end_dt;

    private String status_cd;

    private Date create_dt;

    private List<MerchantAttr> merchantAttrs ;


    /**
     * 分装实体数据
     * @param boMerchant
     * @return
     */
    public static Merchant getMerchant(BoMerchant boMerchant){
        Merchant merchant = new Merchant();
        merchant.setAddress(boMerchant.getAddress());
        merchant.setCreate_dt(boMerchant.getCreate_dt());
        merchant.setEnd_dt(boMerchant.getEnd_dt());
        merchant.setLogoImg(boMerchant.getLogoImg());
        merchant.setMerchantId(boMerchant.getMerchantId());
        merchant.setName(boMerchant.getName());
        merchant.setStart_dt(boMerchant.getStart_dt());

        return merchant;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(Date start_dt) {
        this.start_dt = start_dt;
    }

    public Date getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(Date end_dt) {
        this.end_dt = end_dt;
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    public List<MerchantAttr> getMerchantAttrs() {
        return merchantAttrs;
    }

    public void setMerchantAttrs(List<MerchantAttr> merchantAttrs) {
        this.merchantAttrs = merchantAttrs;
    }
}
