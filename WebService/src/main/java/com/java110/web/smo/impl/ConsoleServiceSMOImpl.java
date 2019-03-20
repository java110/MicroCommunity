package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.cache.MappingCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.DateUtil;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.web.smo.IConsoleServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.context.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务服务类
 * Created by wuxw on 2018/4/28.
 */
@Service("consoleServiceSMOImpl")
public class ConsoleServiceSMOImpl  implements IConsoleServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(ConsoleServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;



    /**
     *  colModel 字段处理
     * @param jsonArray
     * @throws SMOException
     */
    private void paramColModelToJson(JSONArray jsonArray) throws SMOException{
        if(jsonArray == null || jsonArray.size() == 0){
            return ;
        }
        try {
            for(int arrIndex = 0; arrIndex < jsonArray.size();arrIndex ++){
                JSONObject tObj = jsonArray.getJSONObject(arrIndex);
                tObj.put("colModel",JSONObject.parse(tObj.getString("colModel")));
            }
        }catch (Exception e){
            logger.error("c_template_col 表 colModel 配置错误",e);
            throw new SMOException(ResponseConstant.RESULT_CODE_CONFIG_ERROR,"colModel 配置错误");
        }
    }

    /**
     * 删除BUTTON 字段 值
     * @param jsonObject
     */
    private void removeButtonName(JSONObject jsonObject){
        JSONArray template = jsonObject.getJSONArray("template");
        for(int colIndex = 0 ; colIndex < template.size() ; colIndex ++){
            if(CommonConstant.TEMPLATE_COLUMN_NAME_BUTTON.equals(template.getJSONObject(colIndex).getString("colName"))){
                template.getJSONObject(colIndex).put("colName","");
            }
        }
    }

    private JSONObject doExecute(Map<String,Object> paramIn) {
        //获取组件
        String appId = MappingCache.getValue(MappingConstant.KEY_CONSOLE_SERVICE_APP_ID);

        Assert.hasLength(appId, "组件不能为空");

        String apiServiceUrl = MappingCache.getValue(MappingConstant.KEY_API_SERVICE_URL);

        Assert.hasLength(apiServiceUrl, "api服务器地址没有配置");

        String securityCode = MappingCache.getValue(MappingConstant.KEY_CONSOLE_SECURITY_CODE);
        Assert.hasLength(securityCode, "签名秘钥没有配置");

        String serviceCode = paramIn.get(ServiceCodeConstant.SERVICE_CODE).toString();
        if(paramIn.containsKey(ServiceCodeConstant.SERVICE_CODE)){
            paramIn.remove(ServiceCodeConstant.SERVICE_CODE);
        }
        if(paramIn.containsKey(ServiceCodeConstant.SERVICE_CODE_NAME)){
            paramIn.remove(ServiceCodeConstant.SERVICE_CODE_NAME);
        }

        if(paramIn.containsKey(CommonConstant.ORDER_USER_ID)){
            paramIn.remove(CommonConstant.ORDER_USER_ID);
        }
        String params = "?";
        for(String key : paramIn.keySet()){
            params += (key +"=" + paramIn.get(key)+"&");
        }
        params = params.lastIndexOf("&") >-1 ? params.substring(0,params.length()-1):params;
        apiServiceUrl = apiServiceUrl.replace("#serviceCode#",serviceCode) + params;
        ResponseEntity<String> responseEntity = null;
        if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_CONSOLE_SERVICE_SECURITY_ON_OFF))) {
            try {
//                requestBody = DataTransactionFactory.encrypt(requestBody, 2048);
//                //调用查询菜单信息
//                HttpHeaders header = new HttpHeaders();
//                header.add(CommonConstant.ENCRYPT, MappingConstant.VALUE_ON);
//                header.add(CommonConstant.ENCRYPT_KEY_SIZE, "2048");
//                HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, header);
//                responseMsg = restTemplate.postForObject(centerServiceUrl, httpEntity, String.class);
//                responseMsg = DataTransactionFactory.decrypt(responseMsg, 2048);
            }catch (Exception e){
                logger.error("调用接口失败",e);
                throw new SMOException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"调用接口失败"+e);
            }
        } else {
            HttpHeaders header = new HttpHeaders();
            header.add(CommonConstant.HTTP_APP_ID, appId);
            header.add(CommonConstant.HTTP_TRANSACTION_ID, GenerateCodeFactory.getTransactionId());
            header.add(CommonConstant.HTTP_REQ_TIME, DateUtil.getNowDefault());
            header.add(CommonConstant.HTTP_SIGN, "");
            HttpEntity<String> httpEntity = new HttpEntity<String>(header);
            try {
                responseEntity = restTemplate.exchange(apiServiceUrl, HttpMethod.GET, httpEntity, String.class);
            }catch (HttpClientErrorException e){
                //responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(),e.getStatusCode());

                return null;
            }
        }

        Assert.isJsonObject(responseEntity.getBody(),"下游系统返回报文不是有效的json格式");

        return JSONObject.parseObject(responseEntity.getBody());
    }



    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void login(IPageData pd) throws SMOException {

    }
}
