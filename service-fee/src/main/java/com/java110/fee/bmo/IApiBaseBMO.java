package com.java110.fee.bmo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.entity.center.AppService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IApiBaseBMO {

    /**
     * 调用下游服务
     *
     * @param event
     * @return
     */
    public ResponseEntity<String> callService(ServiceDataFlowEvent event);
    /**
     * 调用下游服务
     *
     * @param context
     * @param serviceCode 下游服务
     * @return
     */
     ResponseEntity<String> callService(DataFlowContext context, String serviceCode, JSONArray businesses);

    /**
     * 调用下游服务
     *
     * @param context
     * @param serviceCode 下游服务
     * @return
     */
    ResponseEntity<String> callService(DataFlowContext context, String serviceCode, JSONObject businesses);

    /**
     * 调用下游服务
     *
     * @param context
     * @param appService 下游服务
     * @return
     */
    ResponseEntity<String> callService(DataFlowContext context, AppService appService, Map paramIn);

    /**
     * 将rest 协议转为 订单协议
     *
     * @param businesses 多个业务
     * @param headers    订单头信息
     * @return
     */
    JSONObject restToCenterProtocol(JSONObject businesses, Map<String, String> headers);
    /**
     * 将rest 协议转为 订单协议
     *
     * @param businesses 多个业务
     * @param headers    订单头信息
     * @return
     */
     JSONObject restToCenterProtocol(JSONArray businesses, Map<String, String> headers);

    public void freshOrderProtocol(JSONObject orders, Map<String, String> headers);

    /**
     * 刷入order信息
     *
     * @param httpHeaders http 头信息
     * @param headers     头部信息
     */
    public void freshHttpHeader(HttpHeaders httpHeaders, Map<String, String> headers);
}
