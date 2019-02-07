package com.java110.entity.order;

import java.util.Date;

/**
 * @author wux
 * @create 2019-02-05 上午11:35
 * @desc 订单公共部分
 **/
public class BaseOrder {


    /**
     * 订单完成时间
     */
    private Date finishTime;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 订单状态
     */
    private String statusCd;


    /**
     * 创建时间
     */
    private Date createTime;





    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
