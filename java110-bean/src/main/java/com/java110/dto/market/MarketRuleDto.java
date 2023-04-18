package com.java110.dto.market;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销规则数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketRuleDto extends PageDto implements Serializable {

    private String name;
private String remark;
private String ruleId;


    private Date createTime;

    private String statusCd = "0";


    public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getRuleId() {
        return ruleId;
    }
public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
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
