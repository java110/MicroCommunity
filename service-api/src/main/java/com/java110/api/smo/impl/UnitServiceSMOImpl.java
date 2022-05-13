package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.IUnitServiceSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName UnitServiceSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/5/2 19:30
 * @Version 1.0
 * add by wuxw 2019/5/2
 **/
@Service("unitServiceSMOImpl")
public class UnitServiceSMOImpl extends DefaultAbstractComponentSMO implements IUnitServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(UnitServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;


    @Override
    public ResponseEntity<String> listUnits(IPageData pd) {

        validateListUnit(pd);

        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_UNIT);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String communityId = paramIn.getString("communityId");

        //小区楼编号
        String floorId = paramIn.getString("floorId");

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

        String apiUrl = "unit.queryUnits" + mapToUrlParam(paramIn);

        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        return responseEntity;
    }

    /**
     * 保存小区单元信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    @Override
    public ResponseEntity<String> saveUnit(IPageData pd) {

        //校验入参
        validateSaveUnit(pd);

        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_UNIT);

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

        String apiUrl = "unit.saveUnit";

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> updateUnit(IPageData pd) {
        //校验入参
        validateUpdateUnit(pd);

        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_UNIT);

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

        String apiUrl = "unit.updateUnit";

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteUnit(IPageData pd) {
        //校验入参
        validateDeleteUnit(pd);

        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_UNIT);

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

        String apiUrl = "unit.deleteUnit";

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);
        return responseEntity;
    }

    /**
     * 删除单元信息校验
     *
     * @param pd 页面数据封装
     */
    private void validateDeleteUnit(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "unitId", "请求报文中未包含unitId节点");

    }

    /**
     * 校验 保存小区单元参数信息
     *
     * @param pd 页面数据封装
     */
    private void validateSaveUnit(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "unitNum", "请求报文中未包含unitNum节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "layerCount", "请求报文中未包含layerCount节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "lift", "请求报文中未包含lift节点");
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(reqJson.getString("layerCount"), "单元总层数据不是有效数字");

        if (!"1010".equals(reqJson.getString("lift")) && !"2020".equals(reqJson.getString("lift"))) {
            throw new IllegalArgumentException("是否有电梯 传入数据错误");
        }
    }

    /**
     * 校验 修改小区单元参数信息
     *
     * @param pd 页面数据封装
     */
    private void validateUpdateUnit(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "unitNum", "请求报文中未包含unitNum节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "layerCount", "请求报文中未包含layerCount节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "lift", "请求报文中未包含lift节点");
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(reqJson.getString("layerCount"), "单元总层数据不是有效数字");

        if (!"1010".equals(reqJson.getString("lift")) && !"2020".equals(reqJson.getString("lift"))) {
            throw new IllegalArgumentException("是否有电梯 传入数据错误");
        }
    }

    /**
     * 小区单元查询数据校验
     *
     * @param pd 页面数据封装对象
     */
    private void validateListUnit(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
       // Assert.jsonObjectHaveKey(pd.getReqData(), "floorId", "请求报文中未包含floorId节点");
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
