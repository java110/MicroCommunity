package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IStaffServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 员工服务类
 * Created by Administrator on 2019/4/2.
 */
@Service("staffServiceSMOImpl")
public class StaffServiceSMOImpl extends BaseComponentSMO implements IStaffServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(StaffServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    /**
     * 添加员工信息
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> saveStaff(IPageData pd) {
        logger.debug("保存员工信息入参：{}",pd.toString());
        Assert.jsonObjectHaveKey(pd.getReqData(),"username","请求报文格式错误或未包含用户名信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"email","请求报文格式错误或未包含邮箱信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"tel","请求报文格式错误或未包含手机信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"sex","请求报文格式错误或未包含性别信息");
        Assert.jsonObjectHaveKey(pd.getReqData(),"address","请求报文格式错误或未包含地址信息");

        ResponseEntity responseEntity = super.getStoreInfo(pd,restTemplate);

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(),"storeId","根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("name",reqJson.getString("username"));
        reqJson.put("storeId",storeId);
        reqJson.put("storeTypeCd",storeTypeCd);
        responseEntity = this.callCenterService(restTemplate,pd,reqJson.toJSONString(), ServiceConstant.SERVICE_API_URL+"/api/user.staff.add", HttpMethod.POST);
        return responseEntity;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
