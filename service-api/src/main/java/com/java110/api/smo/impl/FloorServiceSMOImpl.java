package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.api.smo.IFloorServiceSMO;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
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
 * 小区楼实现类
 * <p>
 * add by wuxw 2019-04-22
 */

@Service("floorServiceSMOImpl")
public class FloorServiceSMOImpl extends DefaultAbstractComponentSMO implements IFloorServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(FloorServiceSMOImpl.class);



    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询 楼栋
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    public ResponseEntity<String> getFloor(IPageData pd){
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("page",1);
        paramIn.put("row",1);
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_FLOOR);

        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, "",
                "floor.queryFloors" +  mapToUrlParam(paramIn),
                HttpMethod.GET);

        return responseEntity;
    }

    /**
     * 查询小区楼
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    @Override
    public ResponseEntity<String> listFloor(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        if(paramIn.containsKey("row")){
            paramIn.put("rows",paramIn.getString("row"));
        }
        validateListFloor(pd,paramIn);

        int page = Integer.parseInt(paramIn.getString("page"));
        int rows = Integer.parseInt(paramIn.getString("rows"));
        String communityId = paramIn.getString("communityId");

        //小区楼编号
        String floorNum = paramIn.getString("floorNum");


        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_FLOOR);

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
        String apiUrl = "floor.queryFloors" + mapToUrlParam(paramIn);

        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject resultObjs = JSONObject.parseObject(responseEntity.getBody().toString());
        resultObjs.put("row", rows);
        resultObjs.put("page", page);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveFloor(IPageData pd) {

        validateSaveFloor(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_FLOOR);

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
                "floor.saveFloor",
                HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 编辑小区楼信息
     *
     * @param pd 页面数据封装对象
     * @return
     */
    @Override
    public ResponseEntity<String> editFloor(IPageData pd) {

        validateEditFloor(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_FLOOR);

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
        paramIn.put("name", paramIn.getString("floorName"));
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "floor.editFloor",
                HttpMethod.POST);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteFloor(IPageData pd) {
        validateDeleteFloor(pd);
        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_FLOOR);
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

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "floor.deleteFloor",
                HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 删除小区楼 校验
     *
     * @param pd 页面数据封装
     */
    private void validateDeleteFloor(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorId", "未包含小区楼ID");

    }

    /**
     * 校验保存小区楼 信息
     *
     * @param pd 页面数据封装
     */
    private void validateEditFloor(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorId", "未包含小区楼ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorName", "未包含小区楼名称");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorNum", "未包含小区楼编码");
        Assert.jsonObjectHaveKey(pd.getReqData(), "remark", "未包含小区楼备注");
    }

    /**
     * 校验保存小区楼 信息
     *
     * @param pd 页面数据封装
     */
    private void validateSaveFloor(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "name", "未包含小区楼名称");
        Assert.jsonObjectHaveKey(pd.getReqData(), "floorNum", "未包含小区楼编码");
        Assert.jsonObjectHaveKey(pd.getReqData(), "remark", "未包含小区楼备注");
    }

    /**
     * 校验查询小区楼信息
     *
     * @param pd 页面封装对象
     */
    private void validateListFloor(IPageData pd, JSONObject paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(paramIn, "rows", "请求报文中未包含rows节点");
        Assert.jsonObjectHaveKey(paramIn, "communityId", "请求报文中未包含communityId节点");
        //JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("page"), "page不是数字");
        Assert.isInteger(paramIn.getString("rows"), "rows不是数字");
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        int rows = Integer.parseInt(paramIn.getString("rows"));


        if (rows > MAX_ROW) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "rows 数量不能大于50");
        }

    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
