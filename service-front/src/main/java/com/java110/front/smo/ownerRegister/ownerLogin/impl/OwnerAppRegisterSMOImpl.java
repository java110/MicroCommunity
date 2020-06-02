package com.java110.front.smo.ownerRegister.ownerLogin.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.front.smo.AppAbstractComponentSMO;
import com.java110.front.smo.ownerRegister.ownerLogin.IOwnerAppRegisterSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * wx登录
 */
@Service("ownerAppRegisterSMOImpl")
public class OwnerAppRegisterSMOImpl extends AppAbstractComponentSMO implements IOwnerAppRegisterSMO {

    private final static Logger logger = LoggerFactory.getLogger(OwnerAppRegisterSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String>
    doLogin(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        Assert.hasKeyAndValue(paramIn, "username", "请求报文中未包含用户名");
        Assert.hasKeyAndValue(paramIn, "password", "请求报文中未包含密码");
        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_ORG);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {

        logger.debug("doLogin入参：" + paramIn.toJSONString());
        ResponseEntity<String> responseEntity;

        JSONObject loginInfo = JSONObject.parseObject(pd.getReqData());

        loginInfo.put("passwd", AuthenticationFactory.passwdMd5(loginInfo.getString("password")));
        responseEntity = this.callCenterService(restTemplate, pd, loginInfo.toJSONString(), ServiceConstant.SERVICE_API_URL +"/api/user.service.login", HttpMethod.POST);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());

        //根据用户查询商户信息
        String userId = userInfo.getString("userId");

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate,pd,"", ServiceConstant.SERVICE_API_URL + "/api/owner.listAppUserBindingOwners?userid="+userId,HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject ownerInfo = JSONObject.parseObject(responseEntity.getBody().toString());

        if(ownerInfo.getInteger("total") != 1){
            responseEntity = new ResponseEntity<>("用户未绑定业主",HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        JSONObject appUser = ownerInfo.getJSONArray("auditAppUserBindingOwners").getJSONObject(0);
        appUser.put("userId",userId);
        appUser.put("userName",paramIn.getString("username"));
        JSONObject paramOut = new JSONObject();
        paramOut.put("result", 0);
        paramOut.put("owner", appUser);
        paramOut.put("token", userInfo.getString("token"));
        //pd.setToken(JSONObject.parseObject(responseEntity.getBody()).getString("token"));

        return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
