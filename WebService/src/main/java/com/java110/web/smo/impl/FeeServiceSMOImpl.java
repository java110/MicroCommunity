package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.FeeTypeConstant;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.common.util.StringUtil;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IFeeServiceSMO;
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
@Service("feeServiceSMOImpl")
public class FeeServiceSMOImpl extends BaseComponentSMO implements IFeeServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(FeeServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> loadPropertyConfigFee(IPageData pd, String feeTypeCd) {
        validateLoadPropertyConfigFee(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PROPERTY_CONFIG_FEE);

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
        if (!StringUtil.isEmpty(feeTypeCd)) {
            paramIn.put("feeTypeCd", feeTypeCd);
        }
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/fee.queryFeeConfig" + mapToUrlParam(paramIn),
                HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONArray feeConfigs = JSONArray.parseArray(responseEntity.getBody().toString());

        if (feeConfigs != null && feeConfigs.size() > 1) {
            responseEntity = new ResponseEntity<String>("数据异常，请检查配置数据", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        if (feeConfigs != null && feeConfigs.size() > 0) {
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(feeConfigs.get(0)), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<String>("{}", HttpStatus.OK);

        }


        return responseEntity;
    }


    @Override
    public ResponseEntity<String> loadParkingSpaceConfigFee(IPageData pd) {
        validateLoadPropertyConfigFee(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE_CONFIG_FEE);

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
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/fee.queryFeeConfig" + mapToUrlParam(paramIn),
                HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }
        JSONArray feeConfigs = JSONArray.parseArray(responseEntity.getBody().toString());

        if (feeConfigs != null && feeConfigs.size() > 1) {
            responseEntity = new ResponseEntity<String>("数据异常，请检查配置数据", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        if (feeConfigs != null && feeConfigs.size() > 0) {
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(feeConfigs.get(0)), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<String>("{}", HttpStatus.OK);

        }


        return responseEntity;
    }

    @Override
    public ResponseEntity<String> payFee(IPageData pd) {
        validatePayFee(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PROPERTY_FEE);

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
                ServiceConstant.SERVICE_API_URL + "/api/fee.payFee",
                HttpMethod.POST);


        return responseEntity;
    }

    /**
     * 查询主费用
     *
     * @param pd 页面数据封装对象
     * @return
     */
    @Override
    public ResponseEntity<String> loadFeeByRoomId(IPageData pd) {
        validateLoadFee(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PROPERTY_CONFIG_FEE);

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
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/fee.queryFee" + mapToUrlParam(paramIn),
                HttpMethod.GET);
        return responseEntity;
    }


    /**
     * 查询主费用
     *
     * @param pd 页面数据封装对象
     * @return
     */
    @Override
    public ResponseEntity<String> loadFeeByPsId(IPageData pd) {
        validateLoadParkingSpaceFee(pd);

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
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/fee.queryFeeByParkingSpace" + mapToUrlParam(paramIn),
                HttpMethod.GET);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> loadFeeDetail(IPageData pd) {
        validateLoadFeeDetail(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PROPERTY_FEE);

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
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/fee.queryFeeDetail" + mapToUrlParam(paramIn),
                HttpMethod.GET);
        return responseEntity;
    }

    /**
     * @param pd 页面数据封装对象
     * @return
     */
    @Override
    public ResponseEntity<String> saveOrUpdatePropertyFeeConfig(IPageData pd) {
        validateLoadPropertyConfigFee(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PROPERTY_CONFIG_FEE);

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
        paramIn.put("feeTypeCd", FeeTypeConstant.FEE_TYPE_PROPERTY);
        if (!paramIn.containsKey("configId") || StringUtil.isEmpty(paramIn.getString("configId"))) {
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                    ServiceConstant.SERVICE_API_URL + "/api/fee.saveFeeConfig",
                    HttpMethod.POST);
        } else {
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                    ServiceConstant.SERVICE_API_URL + "/api/fee.updateFeeConfig",
                    HttpMethod.POST);
        }

        return responseEntity;
    }


    /**
     * @param pd 页面数据封装对象
     * @return
     */
    @Override
    public ResponseEntity<String> saveOrUpdateParkingSpaceFeeConfig(IPageData pd) {
        validateLoadParkingSpaceConfigFee(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE_CONFIG_FEE);

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

        if (!paramIn.containsKey("configId") || StringUtil.isEmpty(paramIn.getString("configId"))) {
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                    ServiceConstant.SERVICE_API_URL + "/api/fee.saveFeeConfig",
                    HttpMethod.POST);
        } else {
            responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                    ServiceConstant.SERVICE_API_URL + "/api/fee.updateFeeConfig",
                    HttpMethod.POST);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> listArrearsFee(IPageData pd) {
        validateListFee(pd);

        //校验员工是否有权限操作
        //super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_PARKING_SPACE_CONFIG_FEE);

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
                ServiceConstant.SERVICE_API_URL + "/api/fee.queryArrearsFee",
                HttpMethod.POST);

        return responseEntity;
    }


    private void validateLoadFee(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "feeTypeCd", "请求报文中未包含feeTypeCd节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("roomId"), "房屋ID不能为空");
        Assert.hasLength(paramIn.getString("feeTypeCd"), "费用类型不能为空");
    }


    private void validateLoadParkingSpaceFee(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "psId", "请求报文中未包含psId节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("psId"), "停车位ID不能为空");
    }


    private void validateListFee(IPageData pd){
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "feeTypeCd", "请求报文中未包含feeTypeCd节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("feeTypeCd"), "停车位feeTypeCd不能为空");
    }


    /**
     * 校验缴费参数
     *
     * @param pd
     */
    private void validatePayFee(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "feeId", "请求报文中未包含feeId节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("cycles"), "周期不能为空");
        Assert.hasLength(paramIn.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(paramIn.getString("feeId"), "费用ID不能为空");
    }


    private void validateLoadFeeDetail(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "feeId", "请求报文中未包含roomId节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(paramIn.getString("feeId"), "费用ID不能为空");
    }

    /**
     * 小区房屋查询数据校验
     *
     * @param pd 页面数据封装对象
     */
    private void validateLoadPropertyConfigFee(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
    }

    /**
     * 小区房屋查询数据校验
     *
     * @param pd 页面数据封装对象
     */
    private void validateLoadParkingSpaceConfigFee(IPageData pd) {
        validateLoadPropertyConfigFee(pd);

        Assert.jsonObjectHaveKey(pd.getReqData(), "feeTypeCd", "请求报文中未包含feeTypeCd节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("feeTypeCd"), "费用类型不能为空");


    }


    /**
     * 校验数据合法性
     *
     * @param pd
     */
    private void validateSaveOrUpdatePropertyFeeConfig(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "squarePrice", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "additionalAmount", "请求报文中未包含communityId节点");


        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasLength(paramIn.getString("communityId"), "小区ID不能为空");
        Assert.isMoney(paramIn.getString("squarePrice"), "不是有效金额格式");
        Assert.isMoney(paramIn.getString("additionalAmount"), "不是有效金额格式");


    }

    /**
     * 校验前台传入房屋信息
     *
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
        Assert.jsonObjectHaveKey(pd.getReqData(), "state", "请求报文中未包含state节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "unitPrice", "请求报文中未包含unitPrice节点");
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.isInteger(reqJson.getString("section"), "房间数不是有效数字");
        Assert.isMoney(reqJson.getString("builtUpArea"), "建筑面积数据格式错误");
        Assert.isMoney(reqJson.getString("unitPrice"), "房屋单价数据格式错误");

        if (!"1010".equals(reqJson.getString("apartment")) && !"2020".equals(reqJson.getString("apartment"))) {
            throw new IllegalArgumentException("不是有效房屋户型 传入数据错误");
        }

        if (!"2001".equals(reqJson.getString("state"))
                && !"2002".equals(reqJson.getString("state"))
                && !"2003".equals(reqJson.getString("state"))
                && !"2004".equals(reqJson.getString("state"))) {
            throw new IllegalArgumentException("不是有效房屋状态 传入数据错误");
        }

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
