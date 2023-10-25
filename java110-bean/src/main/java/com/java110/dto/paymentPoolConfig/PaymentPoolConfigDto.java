package com.java110.dto.paymentPoolConfig;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 支付费用项数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PaymentPoolConfigDto extends PageDto implements Serializable {

    private String ppcId;
private String configId;
private String feeName;
private String communityId;
private String ppId;


    private Date createTime;

    private String statusCd = "0";


    public String getPpcId() {
        return ppcId;
    }
public void setPpcId(String ppcId) {
        this.ppcId = ppcId;
    }
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
    }
public String getFeeName() {
        return feeName;
    }
public void setFeeName(String feeName) {
        this.feeName = feeName;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getPpId() {
        return ppId;
    }
public void setPpId(String ppId) {
        this.ppId = ppId;
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
