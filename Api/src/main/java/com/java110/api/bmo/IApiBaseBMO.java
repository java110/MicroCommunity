package com.java110.api.bmo;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.context.DataFlowContext;
import org.springframework.http.ResponseEntity;

public interface IApiBaseBMO {
    /**
     * 调用下游服务
     *
     * @param context
     * @param serviceCode 下游服务
     * @return
     */
     ResponseEntity<String> callService(DataFlowContext context, String serviceCode, JSONArray businesses);
}
