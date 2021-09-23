package com.java110.fee.bmo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ApiBaseBMO implements IApiBaseBMO {

    protected static final int DEFAULT_ORDER = 1;
    //默认序列
    protected static final int DEFAULT_SEQ = 1;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    /**
     * 调用下游服务
     *
     * @param event
     * @return
     */
    public ResponseEntity<String> callService(ServiceDataFlowEvent event) {

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();
        return callService(dataFlowContext, service, dataFlowContext.getReqJson());
    }


    /**
     * 调用下游服务
     *
     * @param context
     * @param serviceCode 下游服务
     * @return
     */
    public ResponseEntity<String> callService(DataFlowContext context, String serviceCode,JSONArray businesses) {
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONObject paramInObj = restToCenterProtocol(businesses, context.getRequestCurrentHeaders());
        return callService(context,serviceCode,paramInObj);
    }

    /**
     * 调用下游服务
     *
     * @param context
     * @param serviceCode 下游服务
     * @return
     */
    public ResponseEntity<String> callService(DataFlowContext context, String serviceCode,JSONObject paramInObj) {

        //将 rest header 信息传递到下层服务中去
        HttpHeaders header = new HttpHeaders();

        freshHttpHeader(header, context.getRequestCurrentHeaders());

        ResponseEntity responseEntity = null;
        AppService appService = DataFlowFactory.getService(context.getAppId(), serviceCode);
        if (appService == null) {
            responseEntity = new ResponseEntity<String>("当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_QUERY_STORE_USERS, HttpStatus.UNAUTHORIZED);
            context.setResponseEntity(responseEntity);
            return responseEntity;
        }
        return callService(context, appService, paramInObj);
    }

    /**
     * 调用下游服务
     *
     * @param context
     * @param appService 下游服务
     * @return
     */
    public ResponseEntity<String> callService(DataFlowContext context, AppService appService, Map paramIn) {

        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");

        ResponseEntity responseEntity = null;
        if (paramIn == null || paramIn.isEmpty()) {
            paramIn = context.getReqJson();
        }


        RestTemplate tmpRestTemplate = appService.getServiceCode().startsWith("out.") ? outRestTemplate : restTemplate;

        String serviceUrl = appService.getUrl();
        HttpEntity<String> httpEntity = null;
        HttpHeaders header = new HttpHeaders();
        for (String key : context.getRequestCurrentHeaders().keySet()) {
            if (CommonConstant.HTTP_SERVICE.toLowerCase().equals(key.toLowerCase())) {
                continue;
            }
            header.add(key, context.getRequestCurrentHeaders().get(key));
        }
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), appService.getServiceCode());
        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(appService.getMethod())) {
                serviceUrl += "?";
                for (Object key : paramIn.keySet()) {
                    serviceUrl += (key + "=" + paramIn.get(key) + "&");
                }

                if (serviceUrl.endsWith("&")) {
                    serviceUrl = serviceUrl.substring(0, serviceUrl.lastIndexOf("&"));
                }
                httpEntity = new HttpEntity<String>("", header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(appService.getMethod())) {
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(appService.getMethod())) {
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
                responseEntity = tmpRestTemplate.exchange(serviceUrl, HttpMethod.POST, httpEntity, String.class);
            }
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>( e.getResponseBodyAsString(), e.getStatusCode());
        }
        return responseEntity;
    }


    /**
     * 将rest 协议转为 订单协议
     *
     * @param businesses 多个业务
     * @param headers    订单头信息
     * @return
     */
    public JSONObject restToCenterProtocol(JSONArray businesses, Map<String, String> headers) {

        JSONObject centerProtocol = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        freshOrderProtocol(centerProtocol.getJSONObject("orders"), headers);
        centerProtocol.put("business", businesses);
        return centerProtocol;
    }


    /**
     * 将rest 协议转为 订单协议
     *
     * @param business
     * @return
     */
    public JSONObject restToCenterProtocol(JSONObject business, Map<String, String> headers) {

        JSONObject centerProtocol = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        freshOrderProtocol(centerProtocol.getJSONObject("orders"), headers);
        centerProtocol.getJSONArray("business").add(business);
        return centerProtocol;
    }



    /**
     * 刷入order信息
     *
     * @param orders  订单信息
     * @param headers 头部信息
     */
    public void freshOrderProtocol(JSONObject orders, Map<String, String> headers) {
        for (String key : headers.keySet()) {

            if (CommonConstant.HTTP_APP_ID.equals(key)) {
                orders.put("appId", headers.get(key));
            }
            if (CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                orders.put("transactionId", headers.get(key));
            }
            if (CommonConstant.HTTP_SIGN.equals(key)) {
                orders.put("sign", headers.get(key));
            }

            if (CommonConstant.HTTP_REQ_TIME.equals(key)) {
                orders.put("requestTime", headers.get(key));
            }
            if (CommonConstant.HTTP_ORDER_TYPE_CD.equals(key)) {
                orders.put("orderTypeCd", headers.get(key));
            }
            if (CommonConstant.HTTP_USER_ID.equals(key)) {
                orders.put("userId", headers.get(key));
            }

            if(CommonConstant.ORDER_PROCESS.equals(key)){
                orders.put("orderProcess", headers.get(CommonConstant.ORDER_PROCESS));
            }

            if(CommonConstant.O_ID.equals(key)){
                orders.put("oId", headers.get(CommonConstant.O_ID));
            }
        }

    }

    /**
     * 刷入order信息
     *
     * @param httpHeaders http 头信息
     * @param headers     头部信息
     */
    public void freshHttpHeader(HttpHeaders httpHeaders, Map<String, String> headers) {
        for (String key : headers.keySet()) {

            if (CommonConstant.HTTP_APP_ID.equals(key)) {
                httpHeaders.add("app_id", headers.get(key));
            }
            if (CommonConstant.HTTP_TRANSACTION_ID.equals(key)) {
                httpHeaders.add("transaction_id", headers.get(key));
            }

            if (CommonConstant.HTTP_REQ_TIME.equals(key)) {
                httpHeaders.add("req_time", headers.get(key));
            }

            if (CommonConstant.HTTP_USER_ID.equals(key)) {
                httpHeaders.add("user_id", headers.get(key));
            }


        }

    }

    /**
     * 新增数据方法
     *
     * @param context 上下文对象
     * @param param   po对象
     */
    public void insert(DataFlowContext context, Object param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessObj = new JSONObject();
        businessObj = JSONObject.parseObject(JSONObject.toJSONString(BeanConvertUtil.beanCovertMap(param)));
        JSONArray businessArr = new JSONArray();
        businessArr.add(businessObj);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(param.getClass().getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }

    /**
     * 新增数据方法
     *
     * {
     *     HTTP_BUSINESS_TYPE_CD:''.
     *    HTTP_SEQ:1,
     *    HTTP_INVOKE_MODEL:'s',
     *    data:[{
     *        ps:Id
     *        xxxxx
     *        xxxx
     *
     *    }]
     * }
     *
     * @param context 上下文对象
     * @param param   po对象
     */
    public void update(DataFlowContext context, Object param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessObj = new JSONObject();
        businessObj = JSONObject.parseObject(JSONObject.toJSONString(BeanConvertUtil.beanCovertMap(param)));
        JSONArray businessArr = new JSONArray();
        businessArr.add(businessObj);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(param.getClass().getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }

    /**
     * 新增数据方法
     *
     * @param context 上下文对象
     * @param param   po对象
     */
    public void delete(DataFlowContext context, Object param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessObj = new JSONObject();
        businessObj = JSONObject.parseObject(JSONObject.toJSONString(BeanConvertUtil.beanCovertMap(param)));
        JSONArray businessArr = new JSONArray();
        businessArr.add(businessObj);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put(param.getClass().getSimpleName(), businessArr);
        context.addServiceBusiness(business);
    }
}
