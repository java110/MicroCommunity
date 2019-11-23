package com.java110.app.smo;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public abstract class AppAbstractComponentSMO extends AbstractComponentSMO {

    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);


    /**
     * 调用中心服务
     *
     * @return
     */
    @Override
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, IPageData pd, String param, String url, HttpMethod httpMethod) {

        Assert.notNull(pd.getAppId(), "请求头中未包含应用信息");
        ResponseEntity<String> responseEntity = null;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), pd.getAppId());
        header.add(CommonConstant.HTTP_USER_ID.toLowerCase(), StringUtil.isEmpty(pd.getUserId()) ? CommonConstant.ORDER_DEFAULT_USER_ID : pd.getUserId());
        header.add(CommonConstant.HTTP_TRANSACTION_ID.toLowerCase(), pd.getTransactionId());
        header.add(CommonConstant.HTTP_REQ_TIME.toLowerCase(), pd.getRequestTime());
        header.add(CommonConstant.HTTP_SIGN.toLowerCase(), "");
        HttpEntity<String> httpEntity = new HttpEntity<String>(param, header);
        //logger.debug("请求中心服务信息，{}", httpEntity);
        try {
            responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>("请求下游系统异常，" + e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, httpEntity, responseEntity);
            return responseEntity;
        }

    }

}
