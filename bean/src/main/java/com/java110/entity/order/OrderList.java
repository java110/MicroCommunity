package com.java110.entity.order;

import java.util.Date;
import java.util.Set;

/**
 * 购物车信息表
 * Created by wuxw on 2017/4/9.
 */
public class OrderList {

    //购物车ID
    private String olId;

    //渠道ID
    private String channelId;

    //客户ID
    private String custId;

    //购物车类型，网站 1 微信 2 APP 3 对应 order_list_type
    private String olTypeCd;

    // 外部系统ID
    private String extSystemId;

    //备注
    private String remark;

    private Date create_dt;

    private String status_cd;


    private Set<OrderListAttr> orderListAttrs;


    public String getOlId() {
        return olId;
    }

    public void setOlId(String olId) {
        this.olId = olId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getOlTypeCd() {
        return olTypeCd;
    }

    public void setOlTypeCd(String olTypeCd) {
        this.olTypeCd = olTypeCd;
    }

    public String getExtSystemId() {
        return extSystemId;
    }

    public void setExtSystemId(String extSystemId) {
        this.extSystemId = extSystemId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public Set<OrderListAttr> getOrderListAttrs() {
        return orderListAttrs;
    }

    public void setOrderListAttrs(Set<OrderListAttr> orderListAttrs) {
        this.orderListAttrs = orderListAttrs;
    }
}
