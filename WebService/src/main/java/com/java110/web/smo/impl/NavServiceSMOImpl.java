package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.INavServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 导航栏业务处理类
 * Created by Administrator on 2019/4/1.
 */
@Service("navServiceSMOImpl")
public class NavServiceSMOImpl extends BaseComponentSMO implements INavServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(NavServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 用户退出
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> doExit(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        JSONObject exitInfo = new JSONObject();
        exitInfo.put("token",pd.getToken());
        responseEntity = this.callCenterService(restTemplate,pd,exitInfo.toJSONString(), ServiceConstant.SERVICE_API_URL+"/api/user.service.logout", HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 获取用户信息
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> getUserInfo(IPageData pd) {
        Assert.hasLength(pd.getUserId(),"用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate,pd,"", ServiceConstant.SERVICE_API_URL+"/api/query.user.userInfo?userId="+pd.getUserId(), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            JSONObject tmpUserInfo = JSONObject.parseObject(responseEntity.getBody().toString());
            JSONObject resultUserInfo = new JSONObject();
            resultUserInfo.put("name",tmpUserInfo.getString("name"));
            responseEntity = new ResponseEntity<String>(resultUserInfo.toJSONString(),HttpStatus.OK);
        }
        return responseEntity;
    }
}
