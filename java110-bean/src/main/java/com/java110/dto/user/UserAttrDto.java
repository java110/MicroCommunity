package com.java110.dto.user;

import com.java110.dto.PageDto;

import java.io.Serializable;

public class UserAttrDto extends PageDto implements Serializable {

    public static final String SPEC_KEY = "100202061602";//用户临时key

    public static final String SPEC_OPEN_ID = "100201911001";//用户微信OPENID
    public static final String SPEC_UNION_ID = "100201911002";//用户微信UNIONID
    public static final String SPEC_MALL_OPEN_ID = "100201911003"; // 商城openId

    public static final String SPEC_PROPERTY_USER_ID = "100202106001";//物业系统用户ID

    private String attrId;
    private String userId;
    private String specCd;
    private String value;
    private String createTime;
    private String statusCd;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
