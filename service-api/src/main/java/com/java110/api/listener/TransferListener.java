package com.java110.api.listener;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.dto.order.OrderDto;
import com.java110.entity.center.AppService;
import com.java110.entity.order.Business;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.utils.cache.DatabusCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.DomainContant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Listener("transferListener")
public class TransferListener extends AbstractServiceApiListener {
    private final static Logger logger = LoggerFactory.getLogger(TransferListener.class);
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
            header.add(key, reqHeader.get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toJSONString(), header);
        String orgRequestUrl = context.getRequestHeaders().get("REQUEST_URL");

        //String serviceCode = "/" + reqHeader.get(CommonConstant.HTTP_RESOURCE) + "/" + reqHeader.get(CommonConstant.HTTP_ACTION);
        String serviceCode = service.getServiceCode();
        serviceCode = serviceCode.startsWith("/") ? serviceCode : ("/" + serviceCode);

        String requestUrl = service.getUrl() + serviceCode;

        ResponseEntity responseEntity = null;
        if (!StringUtil.isNullOrNone(orgRequestUrl)) {
            String param = orgRequestUrl.contains("?") ? orgRequestUrl.substring(orgRequestUrl.indexOf("?") + 1, orgRequestUrl.length()) : "";
            requestUrl += ("?" + param);
        }
        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(service.getMethod())) {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(service.getMethod())) {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(service.getMethod())) {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {
                responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
            }
            HttpHeaders headers = responseEntity.getHeaders();
            String oId = "-1";
            if (headers.containsKey(OrderDto.O_ID)) {
                oId = headers.get(OrderDto.O_ID).get(0);
            }

            //进入databus
            if (!CommonConstant.HTTP_METHOD_GET.equals(service.getMethod())) {

                dealDatabus(serviceCode, reqJson, oId);
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
        JSONObject resParam = JSONObject.parseObject(responseEntity.getBody() + "");
        if (resParam.containsKey("code") && resParam.containsKey("msg")) { // 说明微服务返回的是 resultVo 对象直接返回就可以
            context.setResponseEntity(responseEntity);
            return;
        }
        responseEntity = ResultVo.createResponseEntity(resParam);
        context.setResponseEntity(responseEntity);

    }

    /**
     * databus 处理
     * databus 是一种数据清洗和加载器，通过驱动的方式 对数据进行清洗。
     *
     * @param serviceCode
     * @param reqJson
     */
    private void dealDatabus(String serviceCode, JSONObject reqJson, String oId) {
        String databusSwitch = MappingCache.getValue(DomainContant.COMMON_DOMAIN, DATABUS_SWITCH);

        if (!DATABUS_SWITCH_ON.equals(databusSwitch)) {
            return;
        }

        List<BusinessDatabusDto> databusDtos = DatabusCache.getDatabuss();

        if (!hasTypeCd(databusDtos, serviceCode)) {
            return;
        }

        List<Business> businesses = new ArrayList<>();
        Business business = null;
        business = new Business();
        business.setbId("-1");
        business.setBusinessTypeCd(serviceCode);
        business.setoId(oId);
        business.setData(reqJson);
        businesses.add(business);


        try {
            //同步databus
            dataBusInnerServiceSMOImpl.exchange(businesses);
        } catch (Exception e) {
            logger.error("传输databus 失败", e);
        }
    }

    private boolean hasTypeCd(List<BusinessDatabusDto> databusDtos, String serviceCode) {

        for (BusinessDatabusDto databusDto : databusDtos) {
            if (databusDto.getBusinessTypeCd().equals(serviceCode)) {
                return true;
            }
        }

        return false;
    }


    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SYSTEM_TRANSFER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }
}
