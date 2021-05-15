package com.java110.entity.merchant;


import com.java110.entity.DefaultBoAttrEntity;

import java.util.Date;

/**
 * 商户属性表 bo_merchant_type
 * Created by wuxw on 2017/5/20.
 */
public class BoMerchantAttr extends DefaultBoAttrEntity implements Comparable<BoMerchantAttr> {

    private String boId;

    private String merchantId;

    private String attrCd;

    private String value;

    private String state;

    private Date create_dt;


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

    public String getAttrCd() {
        return attrCd;
    }

    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    /**
     * 将过程数据转为实例数据
     * @return
     */
    public MerchantAttr convert(){
        MerchantAttr merchantAttr = new MerchantAttr();

        merchantAttr.setMerchantId(this.getMerchantId());
        merchantAttr.setAttrCd(this.getAttrCd());
        merchantAttr.setValue(this.getValue());
        return merchantAttr;
    }

    @Override
    public int compareTo(BoMerchantAttr otherBoMerchant) {
        if("DEL".equals(this.getState()) && "ADD".equals(otherBoMerchant.getState())) {
            return -1;
        }
        return 0;
    }
}
