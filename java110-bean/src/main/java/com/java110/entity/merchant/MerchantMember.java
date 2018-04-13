package com.java110.entity.merchant;

import com.java110.entity.DefaultEntity;

/**
 * Created by wuxw on 2017/8/30.
 */
public class MerchantMember extends DefaultEntity {

    private long id;

    private String merchantId;

    private String memberId;

    private String memberType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
