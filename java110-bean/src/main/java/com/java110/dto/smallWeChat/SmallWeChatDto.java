package com.java110.dto.smallWeChat;

import com.java110.dto.PageDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 微信配置表数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class SmallWeChatDto extends PageDto implements Serializable {

    public static final String OBJ_TYPE_COMMUNITY ="1000";
    public static final String WECHAT_TYPE_PUBLIC ="1100";

    private String mchId;
    private String mchName;
    private String storeId;
    private String wechatType;
    private String weChatType;
    private String certPath;
    private String appid;
    private String appsecret;
    private String appId;
    private String appSecret;
    private String name;
    private String objId;
    private String wechatId;
    private String weChatId;
    private String payPassword;
    private String objType;
    private String remarks;

    private List<SmallWechatAttrDto> smallWechatAttrs;


    private Date createTime;

    private String statusCd = "0";


    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getWechatType() {
        return wechatType;
    }

    public void setWechatType(String wechatType) {
        this.wechatType = wechatType;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
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

    public List<SmallWechatAttrDto> getSmallWechatAttrs() {
        return smallWechatAttrs;
    }

    public void setSmallWechatAttrs(List<SmallWechatAttrDto> smallWechatAttrs) {
        this.smallWechatAttrs = smallWechatAttrs;
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
