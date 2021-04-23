package com.java110.core.event.center.event;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IOrderDataFlowContext;
import com.java110.entity.order.Business;

/**
 * 调用业务系统事件
 * Created by wuxw on 2018/7/2.
 */
public class InvokeBusinessBSuccessEvent extends DataFlowEvent {

    private Business business;

    /**
     * 业务系统返回数据
     */
    private JSONObject businessResponseData;


    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public InvokeBusinessBSuccessEvent(Object source, IOrderDataFlowContext dataFlow, Business business,JSONObject businessResponseData) {
        super(source, dataFlow);
        this.business = business;
        this.businessResponseData = businessResponseData;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public InvokeBusinessBSuccessEvent(Object source, IOrderDataFlowContext dataFlow, Business business) {
        super(source, dataFlow);
        this.business = business;
    }

    public Business getBusiness() {
        return business;
    }

    /**
     * 获取业务数据
     * @return
     */
    public JSONObject getBusinessResponseData() {
        return businessResponseData;
    }
}
