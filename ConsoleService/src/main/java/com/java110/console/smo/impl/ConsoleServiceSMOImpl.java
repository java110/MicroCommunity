package com.java110.console.smo.impl;

import com.java110.common.cache.MappingCache;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.MappingConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.console.smo.IConsoleServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务服务类
 * Created by wuxw on 2018/4/28.
 */
@Service("consoleServiceSMOImpl")
public class ConsoleServiceSMOImpl extends LoggerEngine implements IConsoleServiceSMO {


    @Autowired
    private RestTemplate restTemplate;
    /**
     * 根据 管理员ID 查询菜单
     * @param manageId
     * @return
     */
    @Override
    public List<Map> getMenuItemsByManageId(String manageId) throws SMOException,IllegalArgumentException{

            //获取组件
            String appId = MappingCache.getValue(MappingConstant.KEY_CONSOLE_SERVICE_APP_ID);

            Assert.hasLength(appId, "组件不能为空");

            String centerServiceUrl = MappingCache.getValue(MappingConstant.KEY_CENTER_SERVICE_URL);

            Assert.hasLength(centerServiceUrl, "中心服务器地址没有配置");

            String securityCode = MappingCache.getValue(MappingConstant.KEY_CONSOLE_SECURITY_CODE);
            Assert.hasLength(securityCode, "签名秘钥没有配置");
            Map paramIn = new HashMap();
            paramIn.put("manageId", manageId);
            String responseMsg = "";
            String requestBody = DataTransactionFactory.createQueryOneCenterServiceRequestJson(appId, manageId, securityCode,
                    DataTransactionFactory.createQueryOneBusinessRequestJson(ServiceCodeConstant.SERVICE_CODE_QUERY_MENU_ITEM,
                    ServiceCodeConstant.SERVICE_CODE_QUERY_MENU_ITEM_NAME, paramIn));
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.KEY_CONSOLE_SERVICE_SECURITY_ON_OFF))) {
                try {
                    requestBody = DataTransactionFactory.encrypt(requestBody, 2048);
                    //调用查询菜单信息
                    HttpHeaders header = new HttpHeaders();
                    header.add(CommonConstant.ENCRYPT, MappingConstant.VALUE_ON);
                    header.add(CommonConstant.ENCRYPT_KEY_SIZE, "2048");
                    HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, header);
                    responseMsg = restTemplate.postForObject(centerServiceUrl, httpEntity, String.class);
                    responseMsg = DataTransactionFactory.decrypt(responseMsg, 2048);
                }catch (Exception e){
                    logger.error("调用接口失败",e);
                    throw new SMOException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"调用接口失败"+e);
                }
            } else {
                responseMsg = restTemplate.postForObject(centerServiceUrl,requestBody,String.class);
            }


        //根据
        return null;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
