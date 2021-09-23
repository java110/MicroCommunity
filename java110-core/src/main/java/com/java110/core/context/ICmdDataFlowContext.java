package com.java110.core.context;

import org.springframework.http.ResponseEntity;

/**
 * 数据上下文对象
 */
public interface ICmdDataFlowContext extends IDataFlowContextPlus{
    /**
     * 获取字符串请求报文 以防 请求报文不是json
     *
     * @return
     */
    String getReqData();

    ResponseEntity getResponseEntity();

    void setResponseEntity(ResponseEntity responseEntity);

    String getServiceCode();
}
