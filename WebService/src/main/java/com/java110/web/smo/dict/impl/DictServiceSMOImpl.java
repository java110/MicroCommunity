package com.java110.web.smo.dict.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.web.smo.ICarServiceSMO;
import com.java110.web.smo.dict.IDictServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 小区楼实现类
 * <p>
 * add by wuxw 2019-04-22
 */

@Service("dictServiceSMOImpl")
public class DictServiceSMOImpl extends BaseComponentSMO implements IDictServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(DictServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;



    @Override
    public ResponseEntity<String> listDict(IPageData pd) {
        validate(pd);
        //获取请求参数
        JSONObject reqParam = JSONObject.parseObject(pd.getReqData());
        //拉取数据
        String url=ServiceConstant.SERVICE_API_URL.concat("/api/dict.queryDict").concat(mapToUrlParam(reqParam));
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",url , HttpMethod.GET);
        return responseEntity;
    }

    /**
     * 删除小区楼 校验
     *
     * @param pd 页面数据封装
     */
    private void validate(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "name", "未包含name");
        Assert.jsonObjectHaveKey(pd.getReqData(), "type", "未包含type");
        //Assert.jsonObjectHaveKey(pd.getReqData(), "carTypeCd", "请求报文中未包含carTypeCd节点");
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
