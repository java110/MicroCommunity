package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IRoomServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 房屋服务实现类
 */
@Service("roomServiceSMOImpl")
public class RoomServiceSMOImpl extends BaseComponentSMO implements IRoomServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(RoomServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> saveRoom(IPageData pd) {
       validateSaveRoom(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_ROOM);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String communityId = paramIn.getString("communityId");
        ResponseEntity responseEntity = super.getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeTypeCd", "根据用户ID查询商户类型失败，未包含storeTypeCd节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");
        //数据校验是否 商户是否入驻该小区
        super.checkStoreEnterCommunity(pd, storeId, storeTypeCd, communityId, restTemplate);
        paramIn.put("userId", pd.getUserId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/room.saveRoom",
                HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 校验前台传入房屋信息
     * @param pd 页面数据封装
     */
    private void validateSaveRoom(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "unitId", "请求报文中未包含unitId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "roomNum", "请求报文中未包含roomNum节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "layer", "请求报文中未包含layer节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "section", "请求报文中未包含section节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "apartment", "请求报文中未包含apartment节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "builtUpArea", "请求报文中未包含builtUpArea节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "unitPrice", "请求报文中未包含unitPrice节点");
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(reqJson.getString("section"), "房间数不是有效数字");
        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("unitPrice"), "房屋单价数据格式错误");

        if (!"1010".equals(reqJson.getString("apartment")) && !"2020".equals(reqJson.getString("apartment"))) {
            throw new IllegalArgumentException("不是有效房屋户型 传入数据错误");
        }

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
