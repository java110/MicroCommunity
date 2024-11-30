package com.java110.api.smo.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.IApiServiceSMO;
import com.java110.api.smo.api.IApiSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.store.StoreDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service("apiSMOImpl")
public class ApiSMOImpl extends DefaultAbstractComponentSMO implements IApiSMO {

    @Autowired
    private IApiServiceSMO apiServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    private final static Logger logger = LoggerFactory.getLogger(ApiSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected ResponseEntity<String> getStoreInfo(IPageData pd, RestTemplate restTemplate) {

        if (StringUtil.isEmpty(pd.getUserId())) {
            return new ResponseEntity<>("未包含用户信息", HttpStatus.BAD_REQUEST);
        }
        return super.getStoreInfo(pd, restTemplate);
    }

    @Override
    protected ComponentValidateResult validateStoreStaffCommunityRelationship(IPageData pd, RestTemplate restTemplate) {
        // 校验 员工和商户是否有关系
        ResponseEntity responseEntity = getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ComponentValidateResult("", "", "", pd.getUserId(), pd.getUserName());
        }

        JSONObject storeInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        //todo 说明是业主直接返回
        if (!storeInfo.containsKey("storeId")) {
            return new ComponentValidateResult("", "", "", pd.getUserId(), pd.getUserName());
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeTypeCd", "根据用户ID查询商户类型失败，未包含storeTypeCd节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        //开发者和运营不校验小区
        if (StoreDto.STORE_TYPE_ADMIN.equals(storeTypeCd) || StoreDto.STORE_TYPE_DEV.equals(storeTypeCd)) {
            return new ComponentValidateResult(storeId, storeTypeCd, "", pd.getUserId(), pd.getUserName());
        }

        String communityId = "";
        if (paramIn != null && paramIn.containsKey("communityId")
                && !StringUtil.isEmpty(paramIn.getString("communityId"))
                && !"-1".equals(paramIn.getString("communityId"))) {
            communityId = paramIn.getString("communityId");
            checkStoreEnterCommunity(pd, storeId, storeTypeCd, communityId, restTemplate);
        }
        return new ComponentValidateResult(storeId, storeTypeCd, communityId, pd.getUserId(), pd.getUserName());
    }

    @Override
    public ResponseEntity<String> doApi(String body, Map<String, String> headers, HttpServletRequest request) throws UnsupportedEncodingException {

        IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);


        //todo 校验员工时 是否有访问小区的权限
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        //todo 如果 登录用户不为空 则将 前段传递的user-id 重写
        if (!StringUtil.isEmpty(result.getLoginUserId())) {
            headers.remove("user-id");
            headers.remove("user_id");
            headers.put("user-id", result.getUserId());
            headers.put("user_id", result.getUserId());
            headers.put("login-user-id", result.getLoginUserId());
        }
        // todo 如果 商户不为空则 商户ID写入只头信息中 这里的商户ID 可以是物业ID 或者商家ID
        if (!StringUtil.isEmpty(result.getStoreId())) {
            headers.remove("store-id");
            headers.put("store-id", result.getStoreId());
        }

        if (!headers.containsKey("user_id")) {
            headers.put("user_id", "-1");
        }
        if (!headers.containsKey("user-id")) {
            headers.put("user-id", "-1");
        }
        headers.put("store-id", result.getStoreId());
        // todo 应用是否有接口权限校验
        ResponseEntity<String> responseEntity = apiServiceSMOImpl.service(body, headers);
        return responseEntity;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
