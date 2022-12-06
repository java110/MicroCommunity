package com.java110.dto.reserveGoodsConfirmOrder;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 预约订单时间数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ReserveGoodsConfirmOrderDto extends PageDto implements Serializable {

    private String timeId;
private String orderId;
private String goodsId;
private String coId;
private String remark;
private String type;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getTimeId() {
        return timeId;
    }
public void setTimeId(String timeId) {
        this.timeId = timeId;
    }
public String getOrderId() {
        return orderId;
    }
public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
public String getGoodsId() {
        return goodsId;
    }
public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
public String getCoId() {
        return coId;
    }
public void setCoId(String coId) {
        this.coId = coId;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getType() {
        return type;
    }
public void setType(String type) {
        this.type = type;
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
