package com.java110.core.event.center.event;

import com.java110.core.context.IOrderDataFlowContext;

import java.util.Map;

/**
 * 规则校验完成事件
 * Created by wuxw on 2018/7/2.
 */
public class DataResponseEvent extends DataFlowEvent {


    private final String responseData;

    private final Map<String,String> headers;
    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public DataResponseEvent(Object source, IOrderDataFlowContext dataFlow, String responseData, Map<String,String> headers) {
        super(source, dataFlow);
        this.responseData = responseData;
        this.headers = headers;
    }

    public String getResponseData() {
        return responseData;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
