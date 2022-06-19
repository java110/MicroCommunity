package com.java110.po.store;

import java.io.Serializable;

/**
 * @ClassName SmallWechatPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 12:33
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class SmallWechatPo implements Serializable {

    private String weChatId;
    private String storeId;
    private String name;
    private String appId;
    private String appSecret;
    private String payPassword;
    private String remarks;
    private String objId;
    private String objType;
    private String mchId;
    private String mchName;

    private String weChatType;

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getWeChatType() {
        return weChatType;
    }

    public void setWeChatType(String weChatType) {
        this.weChatType = weChatType;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }
}
