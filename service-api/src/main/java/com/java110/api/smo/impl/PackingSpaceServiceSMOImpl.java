package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 小区楼实现类
 * <p>
 * add by wuxw 2019-04-22
 */

@Service("parkingSpaceServiceSMOImpl")
public class PackingSpaceServiceSMOImpl extends DefaultAbstractComponentSMO implements IParkingSpaceServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(PackingSpaceServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询小区楼
     *
     * @param pd 页面数据封装对象
     * @return 返回 ResponseEntity对象包含 http状态 信息 body信息
     */
    @Override
    public ResponseEntity<String> listParkingSpace(IPageData pd) {

        validateListParkingSpace(pd);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        int page = Integer.parseInt(paramIn.getString("page"));
        int row = Integer.parseInt(paramIn.getString("row"));
        String communityId = paramIn.getString("communityId");


        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE);

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
        String apiUrl = "parkingSpace.queryParkingSpaces" + mapToUrlParam(paramIn);


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
    public ResponseEntity<String> saveParkingSpace(IPageData pd) {

        validateSaveParkingSpace(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE);

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
                "parkingSpace.saveParkingSpace",
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
    public ResponseEntity<String> editParkingSpace(IPageData pd) {

        validateEditParkingSpace(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE);

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
                "parkingSpace.editParkingSpace",
                HttpMethod.POST);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteParkingSpace(IPageData pd) {
        validateDeleteParkingSpace(pd);
        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE);
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
                "parkingSpace.deleteParkingSpace",
                HttpMethod.POST);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listParkingSpaceByOwner(IPageData pd) {
        validateListParkingSpaceByOwner(pd);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String communityId = paramIn.getString("communityId");

        //校验用户是否有权限
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE_FOR_OWNER);

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
        String apiUrl = "parkingSpace.queryParkingSpacesByOwner" + mapToUrlParam(paramIn);


        responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject psInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray pss = psInfo.getJSONArray("parkingSpaces");

        Map feeMap = null;
        JSONObject resultFeeInfo = null;
        JSONObject psObj = null;
        for (int roomIndex = 0; roomIndex < pss.size(); roomIndex++) {
            psObj = pss.getJSONObject(roomIndex);
            feeMap = new HashMap();
            feeMap.put("communityId", communityId);
            feeMap.put("psId", psObj.getString("psId"));
            apiUrl = "fee.queryFeeByParkingSpace" + mapToUrlParam(feeMap);
            responseEntity = this.callCenterService(restTemplate, pd, "",
                    apiUrl,
                    HttpMethod.GET);

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                //throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "当前房屋[" + roomObj.getString("roomNum") + "]没有物业费信息，数据错误");

                continue;
            }

            resultFeeInfo = JSONObject.parseObject(responseEntity.getBody().toString());
            psObj.putAll(resultFeeInfo);

        }

        responseEntity = new ResponseEntity(psInfo.toJSONString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> exitParkingSpace(IPageData pd) {
        validateExitParkingSpace(pd);
        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_OWNER_ROOM);

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
        paramIn.put("storeId", storeId);
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                "parkingSpace.exitParkingSpace",
                HttpMethod.POST);

        return responseEntity;
    }

    /**
     * 数据合规性校验
     * @param pd 页面数据封装
     */
    private void validateExitParkingSpace(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "ownerId", "请求报文中未包含ownerId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "psId", "请求报文中未包含psId节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(paramIn.getString("psId"), "psId不能为空");
    }


    /**
     * 删除小区楼 校验
     *
     * @param pd 页面数据封装
     */
    private void validateDeleteParkingSpace(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "psId", "未包含停车位ID");
        //Assert.jsonObjectHaveKey(pd.getReqData(), "parkingSpaceTypeCd", "请求报文中未包含parkingSpaceTypeCd节点");

    }

    /**
     * 校验保存小区楼 信息
     *
     * @param pd 页面数据封装
     */
    private void validateEditParkingSpace(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "psId", "未包含psId");
        Assert.jsonObjectHaveKey(pd.getReqData(), "num", "请求报文中未包含num");
        Assert.jsonObjectHaveKey(pd.getReqData(), "area", "请求报文中未包含area");
        Assert.jsonObjectHaveKey(pd.getReqData(), "paId", "请求报文中未包含停车场信息");
    }

    /**
     * 校验保存小区楼 信息
     *
     * @param pd 页面数据封装
     */
    private void validateSaveParkingSpace(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "num", "请求报文中未包含num");
        Assert.jsonObjectHaveKey(pd.getReqData(), "area", "请求报文中未包含area");
        Assert.jsonObjectHaveKey(pd.getReqData(), "paId", "请求报文中未包含停车场信息");
    }

    /**
     * 校验查询小区楼信息
     *
     * @param pd 页面封装对象
     */
    private void validateListParkingSpace(IPageData pd) {
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

    /**
     * 校验查询小区楼信息
     *
     * @param pd 页面封装对象
     */
    private void validateListParkingSpaceByOwner(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "ownerId", "请求报文中未包含ownerId节点");
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("ownerId"), "业主ID不能为空");


    }



    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
