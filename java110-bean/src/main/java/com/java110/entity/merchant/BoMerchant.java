package com.java110.entity.merchant;


import com.java110.entity.DefaultBoEntity;

import java.util.Date;
import java.util.List;

/**
 * 对应商户 过程表 bo_merchant
 * Created by wuxw on 2017/5/20.
 */
public class BoMerchant extends DefaultBoEntity implements Comparable<BoMerchant>{

    private String boId;

    private String merchantId;

    private String logoImg;

    private String name;

    private String address;

    private String type_cd;

    private Date start_dt;

    private Date end_dt;

    private String state;

    private Date create_dt;

    private List<BoMerchantAttr> boMerchantAttrs;


    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }


    public List<BoMerchantAttr> getBoMerchantAttrs() {
        return boMerchantAttrs;
    }

    public void setBoMerchantAttrs(List<BoMerchantAttr> boMerchantAttrs) {
        this.boMerchantAttrs = boMerchantAttrs;
    }

    /**
     * 转为实例数据
     * @return
     */
    public Merchant convert(){

        Merchant merchant = new Merchant();
        merchant.setMerchantId(this.getMerchantId());
        merchant.setName(this.getName());
        merchant.setAddress(this.getAddress());
        merchant.setStart_dt(this.getStart_dt());
        merchant.setLogoImg(this.getLogoImg());
        merchant.setEnd_dt(this.getEnd_dt());
      

        return merchant;
    }


    /**
     * 排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(BoMerchant otherBoMerchant) {

        if("DEL".equals(this.getState()) && "ADD".equals(otherBoMerchant.getState())) {
            return -1;
        }
        return 0;
    }
}
