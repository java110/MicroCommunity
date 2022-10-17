package com.java110.dto.systemInfo;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 系统配置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class SystemInfoDto extends PageDto implements Serializable {

    private String imgUrl;
    private String systemId;
    private String ownerTitle;
    private String defaultCommunityId;
    private String systemTitle;

    private String systemSimpleTitle;

    private String qqMapKey;
    private String subSystemTitle;
    private String companyName;
    private String mallUrl;

    private String ownerUrl;
    private String propertyUrl;

    private String logoUrl;
    private String propertyTitle;


    private Date createTime;

    private String statusCd = "0";


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getOwnerTitle() {
        return ownerTitle;
    }

    public void setOwnerTitle(String ownerTitle) {
        this.ownerTitle = ownerTitle;
    }

    public String getDefaultCommunityId() {
        return defaultCommunityId;
    }

    public void setDefaultCommunityId(String defaultCommunityId) {
        this.defaultCommunityId = defaultCommunityId;
    }

    public String getSystemTitle() {
        return systemTitle;
    }

    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }

    public String getQqMapKey() {
        return qqMapKey;
    }

    public void setQqMapKey(String qqMapKey) {
        this.qqMapKey = qqMapKey;
    }

    public String getSubSystemTitle() {
        return subSystemTitle;
    }

    public void setSubSystemTitle(String subSystemTitle) {
        this.subSystemTitle = subSystemTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMallUrl() {
        return mallUrl;
    }

    public void setMallUrl(String mallUrl) {
        this.mallUrl = mallUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
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

    public String getSystemSimpleTitle() {
        return systemSimpleTitle;
    }

    public void setSystemSimpleTitle(String systemSimpleTitle) {
        this.systemSimpleTitle = systemSimpleTitle;
    }

    public String getOwnerUrl() {
        return ownerUrl;
    }

    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }

    public String getPropertyUrl() {
        return propertyUrl;
    }

    public void setPropertyUrl(String propertyUrl) {
        this.propertyUrl = propertyUrl;
    }
}
