package com.java110.entity.component;

/**
 * 组件校验结果返回分装实体
 * @ClassName ComponentValidateResult
 * @Description TODO 组件校验结果返回分装实体
 * @Author wuxw
 * @Date 2019/6/23 14:54
 * @Version 1.0
 * add by wuxw 2019/6/23
 **/
public class ComponentValidateResult {


    public ComponentValidateResult(String storeId, String storeTypeCd, String communityId,String userId) {
        this.storeId = storeId;
        this.storeTypeCd = storeTypeCd;
        this.communityId = communityId;
        this.userId = userId;
    }

    private String userId;

    //商户ID
    private String storeId;

    // 商户类型
    private String storeTypeCd;

    private String communityId;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreTypeCd() {
        return storeTypeCd;
    }

    public void setStoreTypeCd(String storeTypeCd) {
        this.storeTypeCd = storeTypeCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
