package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.web.smo.ILoginServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 登录信息实现类
 * Created by wuxw on 2019/3/20.
 */

@Service("loginServiceSMOImpl")
public class LoginServiceSMOImpl extends BaseServiceSMO implements ILoginServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(LoginServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;


    /**
     * 登录处理
     * @param pd 页面请求对象
     * @return
     */
    @Override
    public ResponseEntity<String> doLogin(IPageData pd) {

        ResponseEntity<String> responseEntity = null;

        Assert.jsonObjectHaveKey(pd.getReqData(),"username","请求报文格式错误或未包含username信息");
        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());
        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("passwd")));
        responseEntity = this.callCenterService(restTemplate,pd,loginInfo.toJSONString(),ServiceConstant.SERVICE_API_URL+"/api/user.service.login",HttpMethod.POST);
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            pd.setToken(responseEntity.getBody());
        }
        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
