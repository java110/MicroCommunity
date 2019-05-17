package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IOwnerServiceSMO;
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

@Service("ownerServiceSMOImpl")
public class OwnerServiceSMOImpl extends BaseComponentSMO implements IOwnerServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(OwnerServiceSMOImpl.class);



    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询小区楼
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    @Override
    public ResponseEntity<String> listOwner(IPageData pd) {

        validateListOwner(pd);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        int page = Integer.parseInt(paramIn.getString("page"));
        int row = Integer.parseInt(paramIn.getString("row"));
        String communityId = paramIn.getString("communityId");


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
        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/owner.queryOwners" + mapToUrlParam(paramIn);


        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject resultObjs = JSONObject.parseObject(responseEntity.getBody().toString());
        resultObjs.put("row", row);
        resultObjs.put("page", page);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> saveOwner(IPageData pd) {

        validateSaveOwner(pd);

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
                ServiceConstant.SERVICE_API_URL + "/api/owner.saveOwner",
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
    public ResponseEntity<String> editOwner(IPageData pd) {

        validateEditOwner(pd);

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
        paramIn.put("name", paramIn.getString("ownerName"));
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/owner.editOwner",
                HttpMethod.POST);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteOwner(IPageData pd) {
        validateDeleteOwner(pd);
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
                ServiceConstant.SERVICE_API_URL + "/api/owner.deleteOwner",
                HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 删除小区楼 校验
     *
     * @param pd 页面数据封装
     */
    private void validateDeleteOwner(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "ownerId", "未包含小区楼ID");

    }

    /**
     * 校验保存小区楼 信息
     *
     * @param pd 页面数据封装
     */
    private void validateEditOwner(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "ownerId", "未包含小区楼ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(pd.getReqData(), "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(pd.getReqData(), "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(pd.getReqData(), "remark", "未包含小区楼备注");
    }

    /**
     * 校验保存小区楼 信息
     *
     * @param pd 页面数据封装
     */
    private void validateSaveOwner(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "age", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(pd.getReqData(), "link", "请求报文中未包含link");
        Assert.jsonObjectHaveKey(pd.getReqData(), "sex", "请求报文中未包含sex");
        Assert.jsonObjectHaveKey(pd.getReqData(), "remark", "未包含小区楼备注");
    }

    /**
     * 校验查询小区楼信息
     *
     * @param pd 页面封装对象
     */
    private void validateListOwner(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "page", "请求报文中未包含page节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "row", "请求报文中未包含row节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("page"), "page不是数字");
        Assert.isInteger(paramIn.getString("row"), "rows不是数字");
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        int row = Integer.parseInt(paramIn.getString("row"));


        if (row > MAX_ROW) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "row 数量不能大于50");
        }

    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
