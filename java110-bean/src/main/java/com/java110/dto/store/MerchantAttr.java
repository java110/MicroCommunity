package com.java110.dto.store;

import com.java110.dto.DefaultAttrEntity;

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
