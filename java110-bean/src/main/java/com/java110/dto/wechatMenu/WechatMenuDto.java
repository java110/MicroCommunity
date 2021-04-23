package com.java110.dto.wechatMenu;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 公众号菜单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WechatMenuDto extends PageDto implements Serializable {

    public static final String MENU_LEVEL_ONE = "101"; //一级菜单
    public static final String MENU_LEVEL_TWO = "202"; //二级菜单

    public static final String MENU_TYPE_VIEW = "view"; // 连接

    public static final String MENU_TYPE_MINIPROGRAM = "miniprogram";// 小程序
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
    private String menuTypeName;


    private Date createTime;

    private String statusCd = "0";


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

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }
}
