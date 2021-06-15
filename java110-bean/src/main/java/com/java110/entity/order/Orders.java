package com.java110.entity.order;

import java.util.Date;
import java.util.List;

/**
 * @author wux
 * @create 2019-02-05 上午11:10
 * @desc 订单类 对应表c_orders
 **/
public class Orders extends BaseOrder{

    /**
     * 一次订单提交
     */
    public static final String ORDER_PROCESS_ORDER_ONCE_SUBMIT = "1003001";
    /**
     * 订单预生成
     */
    public static final String ORDER_PROCESS_ORDER_PRE_SUBMIT = "1004001";

    /**
     * 订单确认生成
     */
    public static final String ORDER_PROCESS_ORDER_CONFIRM_SUBMIT = "1005001";

    /**
     * 订单ID
     */
    private String oId;

    /**
     * 组件ID
     */
    private String appId;
    /**
     * 外部交易流水
     */
    private String extTransactionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 外部系统请求时间
     */
    private String requestTime;


    /**
     * 订单类型
     */
    private String orderTypeCd;

    private String orderProcess = ORDER_PROCESS_ORDER_ONCE_SUBMIT;



    private List<OrdersAttrs> ordersAttrs;


    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExtTransactionId() {
        return extTransactionId;
    }

    public void setExtTransactionId(String extTransactionId) {
        this.extTransactionId = extTransactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }



    public List<OrdersAttrs> getOrdersAttrs() {
        return ordersAttrs;
    }

    public void setOrdersAttrs(List<OrdersAttrs> ordersAttrs) {
        this.ordersAttrs = ordersAttrs;
    }

    public String getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(String orderProcess) {
        this.orderProcess = orderProcess;
    }
}
