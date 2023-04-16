package com.java110.dto.chargeMachine;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 充电桩参数数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ChargeMachineSpecDto extends PageDto implements Serializable {

    private String specId;
private String cmsId;
private String machineId;
private String specName;
private String specValue;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getSpecId() {
        return specId;
    }
public void setSpecId(String specId) {
        this.specId = specId;
    }
public String getCmsId() {
        return cmsId;
    }
public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }
public String getMachineId() {
        return machineId;
    }
public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
public String getSpecName() {
        return specName;
    }
public void setSpecName(String specName) {
        this.specName = specName;
    }
public String getSpecValue() {
        return specValue;
    }
public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
