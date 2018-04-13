package com.java110.entity.merchant;

import com.java110.entity.DefaultAttrEntity;

import java.util.Date;

/**
 *
 * 商户 属性
 * Created by wuxw on 2017/5/20.
 */
public class MerchantAttr extends DefaultAttrEntity{


    private String merchantId;


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


}
