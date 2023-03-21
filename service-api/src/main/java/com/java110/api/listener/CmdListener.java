package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.order.OrderDto;
import com.java110.entity.center.AppService;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Java110Listener("cmdListener")
public class CmdListener extends AbstractServiceApiListener {
    private final static Logger logger = LoggerFactory.getLogger(CmdListener.class);
    //databus 业务类型
    private static final String DATABUS_SWITCH = "DATABUS_SWITCH";
    private static final String DATABUS_SWITCH_ON = "ON"; // 开关打开


    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AppService service = event.getAppService();
        Map<String, String> reqHeader = context.getRequestCurrentHeaders();
        HttpHeaders header = new HttpHeaders();
        for (String key : context.getRequestCurrentHeaders().keySet()) {
            if("user-name".equals(key)){
                continue;
            }
            if("userName".equals(key)){
                continue;
            }
            header.add(key, reqHeader.get(key));
        }
        if (reqHeader.containsKey(CommonConstant.USER_ID)
                && (!reqJson.containsKey("userId") || StringUtil.isEmpty(reqJson.getString("userId")))) {
            reqJson.put("userId", reqHeader.get(CommonConstant.USER_ID));
        }
        if (reqHeader.containsKey(CommonConstant.USER_ID)
                && (!reqJson.containsKey("loginUserId") || StringUtil.isEmpty(reqJson.getString("loginUserId")))) {
            reqJson.put("loginUserId", reqHeader.get(CommonConstant.LOGIN_U_ID));
        }
        if (reqHeader.containsKey(CommonConstant.STORE_ID)
                && (!reqJson.containsKey("storeId") || StringUtil.isEmpty(reqJson.getString("storeId")))) {
            reqJson.put("storeId", reqHeader.get(CommonConstant.STORE_ID));
        }
        //解决头部userName以及查询userName兼容问题
        if ((reqJson.containsKey("userName") || !StringUtil.isEmpty(reqJson.getString("userName")))) {
            reqJson.put("searchUserName", reqJson.getString("userName"));
        }
        if (reqHeader.containsKey(CommonConstant.LOGIN_USER_NAME)
                && (!reqJson.containsKey("userName") || StringUtil.isEmpty(reqJson.getString("userName")))) {
            reqJson.put("userName", reqHeader.get(CommonConstant.LOGIN_USER_NAME));
        }

        HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toJSONString(), header);
        String orgRequestUrl = context.getRequestHeaders().get("REQUEST_URL");

        String serviceCode = service.getServiceCode();

        serviceCode = serviceCode.startsWith("/") ? serviceCode : ("/" + serviceCode);

        String requestUrl = service.getUrl() + "/cmd" + serviceCode;
        //
        ResponseEntity responseEntity = null;
        if (!StringUtil.isNullOrNone(orgRequestUrl)) {
            String param = orgRequestUrl.contains("?") ? orgRequestUrl.substring(orgRequestUrl.indexOf("?") + 1, orgRequestUrl.length()) : "";
            requestUrl += ("?" + param);
        }
        try {
            responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
            HttpHeaders headers = responseEntity.getHeaders();
            String oId = "-1";
            if (headers.containsKey(OrderDto.O_ID)) {
                oId = headers.get(OrderDto.O_ID).get(0);
            }


        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            logger.error("请求下游服务【" + requestUrl + "】异常，参数为" + httpEntity + e.getResponseBodyAsString(), e);
            String body = e.getResponseBodyAsString();

            if (StringUtil.isJsonObject(body)) {
                JSONObject bodyObj = JSONObject.parseObject(body);
                if (bodyObj.containsKey("message") && !StringUtil.isEmpty(bodyObj.getString("message"))) {
                    body = bodyObj.getString("message");
                }
            }
            responseEntity = new ResponseEntity<String>(body, e.getStatusCode());
        }

        logger.debug("API 服务调用下游服务请求：{}，返回为：{}", httpEntity, responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, String.valueOf(responseEntity.getBody()));
            context.setResponseEntity(responseEntity);

            return;
        }
        if (StringUtils.isEmpty(responseEntity.getBody() + "")) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "处理失败");
            context.setResponseEntity(responseEntity);
            return;
        }
        context.setResponseEntity(responseEntity);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SYSTEM_CMD;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }
}
