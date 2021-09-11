package com.java110.api.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.utils.util.UrlParamToJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public abstract class AppAbstractComponentSMO extends AbstractComponentSMO {

    private static final Logger logger = LoggerFactory.getLogger(AppAbstractComponentSMO.class);
    protected static final String DEFAULT_PAY_ADAPT = "wechatPayAdapt";// 默认微信通用支付
    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private IApiServiceSMO apiServiceSMOImpl;

    @Autowired
    private RestTemplate restTemplate;

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";
    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";


    /**
     * 调用中心服务
     *
     * @return
     */
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, IPageData pd, String param, String url, HttpMethod httpMethod) {

        Assert.notNull(pd.getAppId(), "请求头中未包含应用信息");
        ResponseEntity<String> responseEntity = null;
        if (StringUtil.isEmpty(param)) {
            param = UrlParamToJsonUtil.getJson(url).toJSONString();
        }
        //logger.debug("请求中心服务信息，{}", httpEntity);
        Map<String, String> headers = new HashMap<String, String>();
        if (pd.getHeaders() != null) {
            for (String key : pd.getHeaders().keySet()) {
                headers.put(key, pd.getHeaders().get(key).toString());
            }
        }
        if (!headers.containsKey(CommonConstant.HTTP_USER_ID)) {
            headers.put(CommonConstant.HTTP_USER_ID, StringUtil.isEmpty(pd.getUserId()) ? "-1" : pd.getUserId());
        }

        headers.put(CommonConstant.USER_ID, StringUtil.isEmpty(pd.getUserId()) ? "-1" : pd.getUserId());

        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        headers.put(CommonConstant.HTTP_SERVICE, url);
        headers.put(CommonConstant.HTTP_METHOD, CommonConstant.getHttpMethodStr(httpMethod));
        if (HttpMethod.GET == httpMethod) {
            headers.put("REQUEST_URL", "http://127.0.0.1:8101/" + url + mapToUrlParam(JSONObject.parseObject(param)));
        }
        try {
            responseEntity = apiServiceSMOImpl.service(param, headers);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, param, responseEntity);
        }
        return responseEntity;
    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserInfo(IPageData pd, RestTemplate restTemplate) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, pd, "", "query.user.userInfo?userId=" + pd.getUserId(), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        return responseEntity;

    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserInfoByOpenId(IPageData pd, RestTemplate restTemplate, String openId) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "user.listUsers?openId=" + openId + "&page=1&row=1", HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        return responseEntity;

    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserAndAttr(IPageData pd, RestTemplate restTemplate, Map paramIn) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        if (paramIn != null) {
            paramIn.put("page", "1");
            paramIn.put("row", "1");
        }
        responseEntity = this.callCenterService(restTemplate, pd, "",
                "user.listUsers" + mapToUrlParam(paramIn), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}
        return responseEntity;

    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getOwnerAppUser(IPageData pd, RestTemplate restTemplate, Map paramIn) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        if (paramIn != null) {
            paramIn.put("page", "1");
            paramIn.put("row", "1");
        }
        responseEntity = this.callCenterService(restTemplate, pd, "",
                 ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS + mapToUrlParam(paramIn), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}
        return responseEntity;

    }

    /**
     * 检查用户是否有权限
     *
     * @param pd
     * @param restTemplate
     * @param privilegeCodes
     */
    protected void checkUserHasPrivilege(IPageData pd, RestTemplate restTemplate, String... privilegeCodes) {
        ResponseEntity<String> responseEntity = null;
        if (true) {
            return;
        }
        for (String privilegeCode : privilegeCodes) {
            responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL
                    + "/api/check.user.hasPrivilege?userId=" + pd.getUserId() + "&pId=" + privilegeCode, HttpMethod.GET);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                //throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "用户没有权限操作权限" + privilegeCodes);
                break;
            }
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "用户没有权限操作权限" + privilegeCodes);
        }
    }


}
