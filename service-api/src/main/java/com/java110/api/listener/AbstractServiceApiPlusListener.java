package com.java110.api.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.ResultVo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Map;

/**
 * 主要目的将soService 方法拆分为校验部分 和业务处理部分
 * Created by wuxw on 2018/11/15.
 */
public abstract class AbstractServiceApiPlusListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(AbstractServiceApiPlusListener.class);

    //订单服务统一地址，这个不会变，直接在这里写死
    private String ORDER_SERVICE_URL = "http://order-service/orderApi/service";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 业务处理
     *
     * @param event
     */
    public final void soService(ServiceDataFlowEvent event) throws ParseException {

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();

        logger.debug("API服务 --- 请求参数为：{}", reqJson.toJSONString());
        dataFlowContext.setResponseEntity(null);

        validate(event, reqJson);

        doSoService(event, dataFlowContext, reqJson);

        //服务合并处理
//        JSONObject paramIn = mergeService(dataFlowContext);
//
//        ResponseEntity<String> responseEntity = this.callOrderService(dataFlowContext, paramIn);
//
//        dataFlowContext.setResponseEntity(responseEntity);

        //提交事务
        commit(dataFlowContext);

        logger.debug("API服务 --- 返回报文信息：{}", dataFlowContext.getResponseEntity());

    }

    /**
     * 提前提交事务
     *
     * @param dataFlowContext
     */
    public void commit(DataFlowContext dataFlowContext) {

        JSONArray businesses = dataFlowContext.getServiceBusiness();

        if (businesses == null || businesses.size() < 1) {
            return;
        }

        //服务合并处理
        JSONObject paramIn = mergeService(dataFlowContext);

        ResponseEntity<String> responseEntity = this.callOrderService(dataFlowContext, paramIn);

        //组装符合要求报文
        ResultVo resultVo = null;

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            resultVo = new ResultVo(ResultVo.ORDER_ERROR, responseEntity.getBody());
        } else {
            String orderResult = responseEntity.getBody();
            if (orderResult.startsWith("{")) {
                resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, JSONObject.parse(orderResult));
            } else {
                resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, JSONArray.parse(orderResult));
            }
        }

        if (dataFlowContext.getResponseEntity() == null || responseEntity.getStatusCode() != HttpStatus.OK) {
            responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
            dataFlowContext.setResponseEntity(responseEntity);
        }

        dataFlowContext.setServiceBusiness(new JSONArray());

    }

    /**
     * 合并服务 拼接最终报文
     *
     * @param dataFlowContext
     * @return
     */
    private JSONObject mergeService(DataFlowContext dataFlowContext) {
        JSONArray services = dataFlowContext.getServiceBusiness();

        JSONArray tmpServices = new JSONArray();

        JSONObject service = null;
        JSONObject existsService = null;
        for (int serIndex = 0; serIndex < services.size(); serIndex++) {
            service = services.getJSONObject(serIndex);
            service.put(CommonConstant.HTTP_SEQ, serIndex + 1);
            existsService = getTmpService(tmpServices, service.getString(CommonConstant.HTTP_BUSINESS_TYPE_CD));
            if (existsService == null) {
                tmpServices.add(service);
                continue;
            }
            JSONObject data = existsService.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS);
//            //获取到business
//            JSONArray businesses = data.getJSONArray(service.getString(CommonConstant.HTTP_BUSINESS_TYPE_CD));
            JSONObject tmpData = service.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS);
//            businesses.addAll(tmpData.getJSONArray(service.getString(CommonConstant.HTTP_BUSINESS_TYPE_CD)));
            //循环当前data 中的节点
            for (String businessName : tmpData.keySet()) {
                //已经存在这个 节点
                if (data.containsKey(businessName)) {
                    JSONArray tmpDataBusinesses = data.getJSONArray(businessName);
                    tmpDataBusinesses.addAll(tmpData.getJSONArray(businessName));
                } else {
                    data.put(businessName, tmpData.getJSONArray(businessName));
                }
            }
        }

        return restToCenterProtocol(tmpServices, dataFlowContext.getRequestCurrentHeaders());
    }

    /**
     * 临时服务中 是否包含当前业务
     *
     * @param tmpServices  临时服务
     * @param businessType 当前业务
     * @return 包含为true 否则为 false
     */
    private JSONObject getTmpService(JSONArray tmpServices, String businessType) {
        if (tmpServices == null || tmpServices.size() < 1) {
            return null;
        }
        for (int serIndex = 0; serIndex < tmpServices.size(); serIndex++) {
            if (businessType.equals(tmpServices.getJSONObject(serIndex).getString(CommonConstant.HTTP_BUSINESS_TYPE_CD))) {
                return tmpServices.getJSONObject(serIndex);
            }
        }

        return null;
    }


    /**
     * 将rest 协议转为 订单协议
     *
     * @param businesses 多个业务
     * @param headers    订单头信息
     * @return
     */
    public JSONObject restToCenterProtocol(JSONArray businesses, Map<String, String> headers) {
        headers.put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONObject centerProtocol = JSONObject.parseObject("{\"orders\":{},\"business\":[]}");
        freshOrderProtocol(centerProtocol.getJSONObject("orders"), headers);
        centerProtocol.put("business", businesses);
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

            if (CommonConstant.ORDER_PROCESS.equals(key)) {
                orders.put("orderProcess", headers.get(CommonConstant.ORDER_PROCESS));
            }

            if (CommonConstant.O_ID.equals(key)) {
                orders.put("oId", headers.get(CommonConstant.O_ID));
            }
        }

    }

    /**
     * 数据格式校验方法
     *
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    protected abstract void validate(ServiceDataFlowEvent event, JSONObject reqJson) throws ParseException;


    /**
     * 业务处理类
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     */
    protected abstract void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException;


    @Override
    public int getOrder() {
        return 0;
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
     * {
     *     HTTP_BUSINESS_TYPE_CD:"",
     *     HTTP_SEQ:"",
     *     HTTP_INVOKE_MODEL:""
     *     datas:{
     *
     *         a:1,
     *         b:2
     *     }
     * }
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


    /**
     * 调用下游服务
     *
     * @param context
     * @return
     */
    public ResponseEntity<String> callOrderService(DataFlowContext context, JSONObject paramIn) {

        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        ResponseEntity responseEntity = null;
        if (paramIn == null || paramIn.isEmpty()) {
            paramIn = context.getReqJson();
        }
        String serviceUrl = ORDER_SERVICE_URL;
        HttpEntity<String> httpEntity = null;
        HttpHeaders header = new HttpHeaders();
        for (String key : context.getRequestCurrentHeaders().keySet()) {
            if (CommonConstant.HTTP_SERVICE.toLowerCase().equals(key.toLowerCase())) {
                continue;
            }
            header.add(key, context.getRequestCurrentHeaders().get(key));
        }
        try {
            httpEntity = new HttpEntity<String>(JSONObject.toJSONString(paramIn), header);
            responseEntity = restTemplate.exchange(serviceUrl, HttpMethod.POST, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        }
        return responseEntity;
    }

}
