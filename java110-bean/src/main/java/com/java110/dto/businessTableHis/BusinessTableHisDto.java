package com.java110.dto.businessTableHis;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 订单轨迹数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class BusinessTableHisDto extends PageDto implements Serializable {

    public static final String ACTION_OBJ_HIS_NO = "NO";

    private String actionObj;
    private String businessTypeCd;
    private String hisId;
    private String action;
    private String remark;
    private String actionObjHis;


    private Date createTime;

    private String statusCd = "0";


    public String getActionObj() {
        return actionObj;
    }

    public void setActionObj(String actionObj) {
        this.actionObj = actionObj;
    }

    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActionObjHis() {
        return actionObjHis;
    }

    public void setActionObjHis(String actionObjHis) {
        this.actionObjHis = actionObjHis;
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
