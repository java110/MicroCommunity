package com.java110.dto.trace;

import java.io.Serializable;

public class TraceAnnotationsDto implements Serializable {
    public static final String VALUE_CLIENT_SEND = "cs"; // 客户端发送
    public static final String VALUE_CLIENT_RECEIVE = "cr"; // 客户端接受
    public static final String VALUE_SERVER_SEND = "ss"; // 服务端发送
    public static final String VALUE_SERVER_RECEIVE = "sr"; // 服务端接受
    private long timestamp;
    private String value;


    private TraceEndpointDto endpoint;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TraceEndpointDto getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(TraceEndpointDto endpoint) {
        this.endpoint = endpoint;
    }
}
