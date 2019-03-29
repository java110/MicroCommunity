package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.ICompanyServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 初始化公司实现类
 * Created by Administrator on 2019/3/28.
 */
@Service("companyServiceSMOImpl")
public class CompanyServiceSMOImpl extends BaseComponentSMO implements ICompanyServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询 商户类别
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> getStoreType(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        Assert.hasLength(pd.getUserId(),"用户还未登录请先登录");

        responseEntity = this.callCenterService(restTemplate,pd,"", ServiceConstant.SERVICE_API_URL+"/api/query.store.type?type=all", HttpMethod.GET);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            Assert.jsonObjectHaveKey(responseEntity.getBody(),"storeType","查询中心服务异常，不是有效json或未包含storeType节点");
            //将storeType 返回出去
            responseEntity = new ResponseEntity<String>(JSONObject.parseObject(responseEntity.getBody()).getJSONArray("storeType").toJSONString(),HttpStatus.OK);
        }
        return responseEntity;
    }

    /**
     * 保存公司信息
     * @param pd
     * @return
     */
    @Autowired
    public ResponseEntity<String> saveCompanyInfo(IPageData pd){
        ResponseEntity<String> responseEntity = null;
        Assert.hasLength(pd.getUserId(),"用户还未登录请先登录");

        validateCompanyInfo(pd.getReqData());
        responseEntity = this.callCenterService(restTemplate,pd,pd.getReqData(), ServiceConstant.SERVICE_API_URL+"/api/query.store.type?type=all", HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 校验公司信息
     * @param param
     */
    private void validateCompanyInfo(String param){

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
