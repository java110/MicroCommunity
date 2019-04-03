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

    /**
     * 加载员工数据
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> loadData(IPageData pd) {


        Assert.jsonObjectHaveKey(pd.getReqData(),"page","请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(pd.getReqData(),"rows","请求报文中未包含rows节点");
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("page"),"page不是数字");
        Assert.isInteger(paramIn.getString("rows"),"rows不是数字");
        int page = Integer.parseInt(paramIn.getString("page"));
        int rows = Integer.parseInt(paramIn.getString("rows"));
        if(rows>50){
            return new ResponseEntity<String>("rows 数量不能大于50",HttpStatus.BAD_REQUEST);
        }
        page = (page-1)*rows;
        ResponseEntity responseEntity = super.getStoreInfo(pd,restTemplate);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(),"storeId","根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        responseEntity = this.callCenterService(restTemplate,pd,"",
                ServiceConstant.SERVICE_API_URL+"/api/query.store.users?row="+rows+"&page="+page+"&storeId="+storeId, HttpMethod.GET);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }

        String result = "{'total':1,'page':1,'row':10,'datas':[" +
                "{'userId':'111','name':'123','email':'928255095@qq.com','address':'张安1','sex':'男','tel':'17797173944','statusCd':'0','createTime':'2019-03-19'}," +
                "{'userId':'111','name':'123','email':'928255095@qq.com','address':'张安2','sex':'男','tel':'17797173945','statusCd':'0','createTime':'2019-03-19'}," +
                "{'userId':'111','name':'123','email':'928255095@qq.com','address':'张安3','sex':'男','tel':'17797173946','statusCd':'0','createTime':'2019-03-19'}" +
                "]}";

        JSONObject resultObjs = JSONObject.parseObject(result);
        resultObjs.put("row",rows);
        resultObjs.put("page",page);
        return responseEntity;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
