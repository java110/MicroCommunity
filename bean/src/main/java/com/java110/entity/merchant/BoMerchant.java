package com.java110.entity.merchant;

import java.util.Date;
import java.util.List;

/**
 * 对应商户 过程表 bo_merchant
 * Created by wuxw on 2017/5/20.
 */
public class BoMerchant {

    private String boId;

    private String merchatId;

    private String logoImg;

    private String name;

    private String address;

    private String type;

    private Date start_dt;

    private Date end_dt;

    private String state;

    private String create_dt;

    private List<BoMerchantAttr> boMerchantAttrs;


    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getMerchatId() {
        return merchatId;
    }

    public void setMerchatId(String merchatId) {
        this.merchatId = merchatId;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(String create_dt) {
        this.create_dt = create_dt;
    }


    public List<BoMerchantAttr> getBoMerchantAttrs() {
        return boMerchantAttrs;
    }

    public void setBoMerchantAttrs(List<BoMerchantAttr> boMerchantAttrs) {
        this.boMerchantAttrs = boMerchantAttrs;
    }
}
