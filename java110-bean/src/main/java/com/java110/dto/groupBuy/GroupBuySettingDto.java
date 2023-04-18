package com.java110.dto.groupBuy;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 拼团设置数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class GroupBuySettingDto extends PageDto implements Serializable {

    private String groupBuyName;
    private String groupBuyDesc;
    private String validHours;
    private String startTime;
    private String endTime;
    private String storeId;
    private String settingId;


    private Date createTime;

    private String statusCd = "0";


    public String getGroupBuyName() {
        return groupBuyName;
    }

    public void setGroupBuyName(String groupBuyName) {
        this.groupBuyName = groupBuyName;
    }

    public String getGroupBuyDesc() {
        return groupBuyDesc;
    }

    public void setGroupBuyDesc(String groupBuyDesc) {
        this.groupBuyDesc = groupBuyDesc;
    }

    public String getValidHours() {
        return validHours;
    }

    public void setValidHours(String validHours) {
        this.validHours = validHours;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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
