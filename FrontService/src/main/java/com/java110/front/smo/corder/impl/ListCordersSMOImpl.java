package com.java110.front.smo.corder.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.advert.IListAdvertItemSMO;
import com.java110.front.smo.corder.IListCordersSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service("listCordersItemSMOImpl")
public class ListCordersSMOImpl extends AbstractComponentSMO implements IListCordersSMO {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        super.validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws IOException {

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/corders.listCorders" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listCorders(IPageData pd) throws SMOException {
        return super.businessProcess(pd);
    }
}
