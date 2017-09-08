package com.java110.entity.merchant;

import com.java110.entity.DefaultAttrEntity;
import com.java110.entity.DefaultBoEntity;

import java.util.Date;

/**
 * 商户成员过程实例
 * Created by wuxw on 2017/8/30.
 */
public class BoMerchantMember extends DefaultBoEntity {



    private String merchantId;

    private String memberId;

    private String memberType;


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }


}
