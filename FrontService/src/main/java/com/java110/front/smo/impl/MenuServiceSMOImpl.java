package com.java110.front.smo.impl;

import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.core.component.BaseComponentSMO;
import com.java110.front.smo.IMenuServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2019/4/1.
 */
@Service("menuServiceSMOImpl")
public class MenuServiceSMOImpl extends BaseComponentSMO implements IMenuServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(FlowServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;


    /**
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> queryMenusByUserId(IPageData pd) {
        ResponseEntity<String> responseEntity = null;

        Assert.hasLength(pd.getUserId(), "用户还没有登录");

        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/query.menu.info?userId=" + pd.getUserId(), HttpMethod.GET);
        return responseEntity;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
