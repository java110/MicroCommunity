package com.java110.entity.order;

import java.util.Date;
import java.util.Set;

/**
 *
 * 订单项
 * Created by wuxw on 2017/4/9.
 */
public class BusiOrder {

    // 业务动作ID
    private String boId;

    // 购物车ID
    private String olId;

    // 购物车动作，对应表action_type
    private String actionTypeCd;

    //数据状态 对应 order_status
    private String status_cd;

    //创建时间
    private Date create_dt;

    //开始时间
    private Date start_dt;

    //结束时间
    private Date end_dt;

    //备注
    private String remark;

        private Set<BusiOrderAttr> busiOrderAttrs;

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getOlId() {
        return olId;
    }

    public void setOlId(String olId) {
        this.olId = olId;
    }

    public String getActionTypeCd() {
        return actionTypeCd;
    }

    public void setActionTypeCd(String actionTypeCd) {
        this.actionTypeCd = actionTypeCd;
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    public Date getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(Date start_dt) {
        this.start_dt = start_dt;
    }

    public Date getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(Date end_dt) {
        this.end_dt = end_dt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<BusiOrderAttr> getBusiOrderAttrs() {
        return busiOrderAttrs;
    }

    public void setBusiOrderAttrs(Set<BusiOrderAttr> busiOrderAttrs) {
        this.busiOrderAttrs = busiOrderAttrs;
    }
}
