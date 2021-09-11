package com.java110.api.smo.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.common.ICommonPostSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName CommonSMOImpl
 * @Description 通用服务处理
 * @Author wuxw
 * @Date 2020/3/8 23:37
 * @Version 1.0
 * add by wuxw 2020/3/8
 **/
@Service("commonPostSMOImpl")
public class CommonPostSMOImpl extends DefaultAbstractComponentSMO implements ICommonPostSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    /**
     * {
     *     "a":123,
     *     userId:xxx
     *     storeId:xxx
     *
     * }
     * @param pd      页面数据封装
     * @param paramIn 前台数据对象
     * @return
     * @throws Exception
     */
    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ResponseEntity<String> responseEntity = null;
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("userId",result.getUserId());
        paramIn.put("userName",result.getUserName());
        paramIn.put("storeId",result.getStoreId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + pd.getApiUrl(),
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> doService(IPageData pd) {
        return businessProcess(pd);
    }
}
