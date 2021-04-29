package com.java110.core.event.center.event;

import com.java110.core.context.IOrderDataFlowContext;

import java.util.Map;

/**
 * 请求进来事件
 * Created by wuxw on 2018/7/2.
 */
public class ReceiveRequestEvent extends DataFlowEvent {

    private final String requestData;
    private final Map<String,String> headers;
    /**
     * Constructs a prototypical Event.
     *
     * @param source   The object on which the Event initially occurred.
     * @param dataFlow
     * @throws IllegalArgumentException if source is null.
     */
    public ReceiveRequestEvent(Object source, IOrderDataFlowContext dataFlow,
                               String requestData, Map<String,String> headers) {
        super(source, dataFlow);
        this.requestData = requestData;
        this.headers = headers;
    }

    public String getRequestData() {
        return requestData;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
