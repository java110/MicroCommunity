package com.java110.dto.integral;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 积分设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class IntegralSettingDto extends PageDto implements Serializable {

    private String money;
private String thirdFlag;
private String remark;
private String thirdUrl;
private String communityId;
private String settingId;


    private Date createTime;

    private String statusCd = "0";


    public String getMoney() {
        return money;
    }
public void setMoney(String money) {
        this.money = money;
    }
public String getThirdFlag() {
        return thirdFlag;
    }
public void setThirdFlag(String thirdFlag) {
        this.thirdFlag = thirdFlag;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getThirdUrl() {
        return thirdUrl;
    }
public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getSettingId() {
        return settingId;
    }
public void setSettingId(String settingId) {
        this.settingId = settingId;
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
