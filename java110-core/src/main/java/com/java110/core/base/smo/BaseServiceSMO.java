package com.java110.core.base.smo;


import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.CommonConstant;
import com.java110.common.util.ProtocolUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.base.AppBase;
import com.java110.core.context.AppContext;
import com.java110.core.context.IPageData;
import com.java110.core.smo.code.IPrimaryKeyInnerServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * 所有服务端的基类
 * 1、报文分装
 * 2、报文解析
 * Created by wuxw on 2017/2/28.
 */
public class BaseServiceSMO extends AppBase {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceSMO.class);

    /**
     * 主键生成
     *
     * @param iPrimaryKeyService 主键生成服务对象
     * @param type               主键类型 如 OL_ID , CUST_ID
     * @return
     * @throws Exception
     */
    protected String queryPrimaryKey(IPrimaryKeyInnerServiceSMO iPrimaryKeyService, String type) throws Exception {
        JSONObject data = new JSONObject();
        data.put("type", type);
        //生成的ID
        String targetId = "-1";
        //要求接口返回 {"RESULT_CODE":"0000","RESULT_INFO":{"user_id":"7020170411000041"},"RESULT_MSG":"成功"}
        String custIdJSONStr = iPrimaryKeyService.queryPrimaryKey(data.toJSONString());
        JSONObject custIdJSONTmp = JSONObject.parseObject(custIdJSONStr);
        if (custIdJSONTmp.containsKey("RESULT_CODE")
                && ProtocolUtil.RETURN_MSG_SUCCESS.equals(custIdJSONTmp.getString("RESULT_CODE"))
                && custIdJSONTmp.containsKey("RESULT_INFO")) {
            //从接口生成olId
            targetId = custIdJSONTmp.getJSONObject("RESULT_INFO").getString(type);
        }
        if ("-1".equals(targetId)) {
            throw new RuntimeException("调用主键生成服务服务失败，" + custIdJSONStr);
        }

        return targetId;
    }

    /**
     * 调用中心服务
     *
     * @return
     */
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, IPageData pd, String param, String url, HttpMethod httpMethod) {
        ResponseEntity<String> responseEntity = null;
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_APP_ID.toLowerCase(), CommonConstant.HC_WEB_APP_ID);
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


    /**
     * 创建上下文对象
     *
     * @return
     */
    protected AppContext createApplicationContext() {
        return AppContext.newInstance();
    }


}
