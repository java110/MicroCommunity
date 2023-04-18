package com.java110.dto.market;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 营销对象数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MarketRuleObjDto extends PageDto implements Serializable {

    public static final String OBJ_TYPE_ACCESS_CONTROL = "1001";
    public static final String OBJ_TYPE_BARRIER = "2002";
    public static final String OBJ_TYPE_PAY_FINISH = "3003";
    public static final String OBJ_TYPE_REPAIR_SUMMIT = "4004";
    //对象类型 1001 门禁 2002 车辆道闸 3003 手机支付完成 4004 报修单提交

    private String objId;
    private String remark;
    private String ruleId;
    private String[] ruleIds;
    private String objType;
    private String[] objTypes;
    private String objTypeName;


    private Date createTime;

    private String statusCd = "0";


    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
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

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
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

    public String getObjTypeName() {
        return objTypeName;
    }

    public void setObjTypeName(String objTypeName) {
        this.objTypeName = objTypeName;
    }

    public String[] getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String[] ruleIds) {
        this.ruleIds = ruleIds;
    }

    public String[] getObjTypes() {
        return objTypes;
    }

    public void setObjTypes(String[] objTypes) {
        this.objTypes = objTypes;
    }
}
