package com.java110.po.wechatMenu;

import java.io.Serializable;

public class WechatMenuPo implements Serializable {

    private String pagepath;
    private String appId;
    private String menuLevel;
    private String menuName;
    private String menuType;
    private String menuValue;
    private String communityId;
    private String wechatMenuId;
    private String parentMenuId;
    private String seq;
    private String statusCd="0";

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuValue() {
        return menuValue;
    }

    public void setMenuValue(String menuValue) {
        this.menuValue = menuValue;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getWechatMenuId() {
        return wechatMenuId;
    }

    public void setWechatMenuId(String wechatMenuId) {
        this.wechatMenuId = wechatMenuId;
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }


    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
