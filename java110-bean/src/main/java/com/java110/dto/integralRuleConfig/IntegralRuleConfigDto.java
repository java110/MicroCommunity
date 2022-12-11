package com.java110.dto.integralRuleConfig;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 积分规则标准数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class IntegralRuleConfigDto extends PageDto implements Serializable {

    private String configId;
private String ircId;
private String ruleId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
    }
public String getIrcId() {
        return ircId;
    }
public void setIrcId(String ircId) {
        this.ircId = ircId;
    }
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
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
