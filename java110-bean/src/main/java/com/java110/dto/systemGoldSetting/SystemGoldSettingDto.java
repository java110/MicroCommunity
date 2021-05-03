package com.java110.dto.systemGoldSetting;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 金币设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class SystemGoldSettingDto extends PageDto implements Serializable {

    private String buyPrice;
private String goldName;
private String goldType;
private String validity;
private String state;
private String usePrice;
private String settingId;


    private Date createTime;

    private String statusCd = "0";


    public String getBuyPrice() {
        return buyPrice;
    }
public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }
public String getGoldName() {
        return goldName;
    }
public void setGoldName(String goldName) {
        this.goldName = goldName;
    }
public String getGoldType() {
        return goldType;
    }
public void setGoldType(String goldType) {
        this.goldType = goldType;
    }
public String getValidity() {
        return validity;
    }
public void setValidity(String validity) {
        this.validity = validity;
    }
public String getState() {
        return state;
    }
public void setState(String state) {
        this.state = state;
    }
public String getUsePrice() {
        return usePrice;
    }
public void setUsePrice(String usePrice) {
        this.usePrice = usePrice;
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
