package com.java110.dto.order;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.PageDto;

import java.io.Serializable;

public class OrderItemDto extends PageDto implements Serializable {


    private String bId;
    private String oId;
    private String createTime;
    private String action;
    private String actionObj;
    private String finishTime;
    private String remark;
    private String serviceName;
    private String logText;

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionObj() {
        return actionObj;
    }

    public void setActionObj(String actionObj) {
        this.actionObj = actionObj;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
