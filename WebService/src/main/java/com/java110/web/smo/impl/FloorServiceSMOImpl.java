package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IFloorServiceSMO;
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
 *
 * add by wuxw 2019-04-22
 */

@Service("floorServiceSMOImpl")
public class FloorServiceSMOImpl extends BaseComponentSMO implements IFloorServiceSMO {

    private final static Logger logger = LoggerFactory.getLogger(FloorServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    /**
     * 查询小区楼
     * @param pd 页面数据封装对象
     * @return
     */
    @Override
    public ResponseEntity<String> listFloor(IPageData pd) {

        validateListFloor(pd);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        int page = Integer.parseInt(paramIn.getString("page"));
        int rows = Integer.parseInt(paramIn.getString("rows"));
        String  floorName = paramIn.getString("floorName");
        page = (page-1)*rows;
        ResponseEntity responseEntity = super.getStoreInfo(pd,restTemplate);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(),"storeId","根据用户ID查询商户ID失败，未包含storeId节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        if(StringUtil.isEmpty(floorName)) {
            responseEntity = this.callCenterService(restTemplate, pd, "",
                    ServiceConstant.SERVICE_API_URL + "/api/query.staff.infos?rows=" + rows + "&page=" + page + "&storeId=" + storeId, HttpMethod.GET);
        }else {
            responseEntity = this.callCenterService(restTemplate, pd, "",
                    ServiceConstant.SERVICE_API_URL + "/api/query.staff.byName?rows=" + rows + "&page=" + page + "&storeId=" + storeId+"&name="+floorName, HttpMethod.GET);
        }
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return responseEntity;
        }

        JSONObject resultObjs = JSONObject.parseObject(responseEntity.getBody().toString());
        resultObjs.put("row",rows);
        resultObjs.put("page",page);
        return responseEntity;
    }

    /**
     * 校验查询小区楼信息
     * @param pd
     */
    private void validateListFloor(IPageData pd){
        Assert.jsonObjectHaveKey(pd.getReqData(),"page","请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(pd.getReqData(),"rows","请求报文中未包含rows节点");
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("page"),"page不是数字");
        Assert.isInteger(paramIn.getString("rows"),"rows不是数字");
        int rows = Integer.parseInt(paramIn.getString("rows"));

        if(rows>50){
            throw new SMOException(1999,"rows 数量不能大于50");
        }

    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
