package com.java110.entity.merchant;

import com.java110.entity.DefaultEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by wuxw on 2017/5/20.
 */
public class Merchant extends DefaultEntity {

    private String merchantId;

    private String logoImg;

    private String name;

    private String address;

    private String type_cd;

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

    public String getType_cd() {
        return type_cd;
    }

    public void setType_cd(String type_cd) {
        this.type_cd = type_cd;
    }

    public List<MerchantAttr> getMerchantAttrs() {
        return merchantAttrs;
    }

    public void setMerchantAttrs(List<MerchantAttr> merchantAttrs) {
        this.merchantAttrs = merchantAttrs;
    }
}
