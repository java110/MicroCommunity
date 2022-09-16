package com.java110.api.smo.login.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.api.smo.AppAbstractComponentSMO;
import com.java110.api.smo.login.IPropertyAppLoginSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * wx登录
 */
@Service("propertyAppLoginSMOImpl")
public class PropertyAppLoginSMOImpl extends DefaultAbstractComponentSMO implements IPropertyAppLoginSMO {

    private final static Logger logger = LoggerFactory.getLogger(PropertyAppLoginSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> doLogin(IPageData pd) throws SMOException {
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
        responseEntity = this.callCenterService(restTemplate, pd, loginInfo.toJSONString(), "user.service.login", HttpMethod.POST);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject userInfo = JSONObject.parseObject(responseEntity.getBody());

        if (userInfo.containsKey("code") && 0 != userInfo.getIntValue("code")) {
            return responseEntity;
        }

        //根据用户查询商户信息
        String userId = userInfo.getString("userId");

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = super.getStoreInfo(pd, restTemplate);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject storeInfo = JSONObject.parseObject(responseEntity.getBody().toString());

        Assert.jsonObjectHaveKey(storeInfo, "storeId", "根据员工未查到商户信息");
        Assert.jsonObjectHaveKey(storeInfo, "storeTypeCd", "根据员工未查到商户类型信息");
        userInfo.put("storeId", storeInfo.getString("storeId"));
        userInfo.put("storeName", storeInfo.getString("name"));
        userInfo.put("storeTypeCd", storeInfo.getString("storeTypeCd"));
        JSONObject paramOut = new JSONObject();
        paramOut.put("result", 0);
        paramOut.put("code", 0);
        paramOut.put("msg", "成功");
        paramOut.put("userInfo", userInfo);
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
