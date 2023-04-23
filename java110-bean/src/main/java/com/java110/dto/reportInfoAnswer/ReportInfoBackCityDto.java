package com.java110.dto.reportInfoAnswer;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 返省上报数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReportInfoBackCityDto extends PageDto implements Serializable {

    private String backId;
private String idCard;
private String name;
private String tel;
private String remark;
private String source;
private String communityId;
private String sourceCityName;
private String userId;
private String sourceCity;
private String backTime;


    private Date createTime;

    private String statusCd = "0";


    public String getBackId() {
        return backId;
    }
public void setBackId(String backId) {
        this.backId = backId;
    }
public String getIdCard() {
        return idCard;
    }
public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getTel() {
        return tel;
    }
public void setTel(String tel) {
        this.tel = tel;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getSource() {
        return source;
    }
public void setSource(String source) {
        this.source = source;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getSourceCityName() {
        return sourceCityName;
    }
public void setSourceCityName(String sourceCityName) {
        this.sourceCityName = sourceCityName;
    }
public String getUserId() {
        return userId;
    }
public void setUserId(String userId) {
        this.userId = userId;
    }
public String getSourceCity() {
        return sourceCity;
    }
public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }
public String getBackTime() {
        return backTime;
    }
public void setBackTime(String backTime) {
        this.backTime = backTime;
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
}
