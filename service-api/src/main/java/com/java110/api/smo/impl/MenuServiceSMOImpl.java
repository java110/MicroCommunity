package com.java110.api.smo.impl;

import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.IMenuServiceSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2019/4/1.
 */
@Service("menuServiceSMOImpl")
public class MenuServiceSMOImpl extends DefaultAbstractComponentSMO implements IMenuServiceSMO {

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
                "query.menu.info?userId=" + pd.getUserId() + "&groupType=P_WEB", HttpMethod.GET);
        return responseEntity;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
