package com.java110.core.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.core.smo.IGetCommunityStoreInfoSMO;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wuxw on 2019/3/22.
 */
public class BaseComponentSMO extends BaseServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(BaseComponentSMO.class);

    protected static final int MAX_ROW = 50;

    @Autowired(required = false)
    private IGetCommunityStoreInfoSMO getCommunityStoreInfoSMOImpl;

    /**
     * 调用组件
     *
     * @param componentCode   组件编码
     * @param componentMethod 组件方法
     * @param pd
     * @return
     */
    protected ResponseEntity<String> invokeComponent(String componentCode, String componentMethod, IPageData pd) {

        logger.debug("开始调用组件：{}", pd.toString());

        ResponseEntity<String> responseEntity = null;

        Object componentInstance = ApplicationContextFactory.getBean(componentCode);

        Assert.notNull(componentInstance, "未找到组件对应的处理类，请确认 " + componentCode);
        try {

            Method cMethod = componentInstance.getClass().getDeclaredMethod(componentMethod, IPageData.class);

            Assert.notNull(cMethod, "未找到组件对应处理类的方法，请确认 " + componentCode + "方法：" + componentMethod);

            logger.debug("组件编码{}，组件方法{}，pd 为{}", componentCode, componentMethod, pd.toString());

            responseEntity = (ResponseEntity<String>) cMethod.invoke(componentInstance, pd);
        } catch (Exception e) {
            logger.error("调用组件失败：", e);
            responseEntity = new ResponseEntity<String>("调用组件" + componentCode + ",组件方法" + componentMethod + "失败：" + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            return responseEntity;
        }
    }


    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserInfo(IPageData pd, RestTemplate restTemplate) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL + "/api/query.user.userInfo?userId=" + pd.getUserId(), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        return responseEntity;

    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserInfoByOpenId(IPageData pd, RestTemplate restTemplate, String openId) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/user.listUsers?openId=" + openId + "&page=1&row=1", HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}

        return responseEntity;

    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getUserAndAttr(IPageData pd, RestTemplate restTemplate, Map paramIn) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        if (paramIn != null) {
            paramIn.put("page", "1");
            paramIn.put("row", "1");
        }
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/user.listUsers" + mapToUrlParam(paramIn), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}
        return responseEntity;

    }

    /**
     * 获取用户信息
     *
     * @param pd
     * @param restTemplate
     * @return
     */
    protected ResponseEntity<String> getOwnerAppUser(IPageData pd, RestTemplate restTemplate, Map paramIn) {
        //Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        if (paramIn != null) {
            paramIn.put("page", "1");
            paramIn.put("row", "1");
        }
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/" + ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS + mapToUrlParam(paramIn), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}
        return responseEntity;

    }


    /**
     * 查询商户信息
     *
     * @return
     */
    protected ResponseEntity<String> getStoreInfo(IPageData pd, RestTemplate restTemplate) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");

        ResultVo resultVo = getCommunityStoreInfoSMOImpl.getStoreInfo(pd, restTemplate, pd.getUserId());

        return new ResponseEntity<String>(resultVo.getMsg(), resultVo.getCode() == ResultVo.CODE_OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> getStoreEnterCommunitys(IPageData pd, String storeId, String storeTypeCd, RestTemplate restTemplate) {
        ResultVo resultVo = getCommunityStoreInfoSMOImpl.getStoreEnterCommunitys(pd, storeId, storeTypeCd, restTemplate);
        return new ResponseEntity<String>(resultVo.getMsg(), resultVo.getCode() == ResultVo.CODE_OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * 查询商户信息
     *
     * @return
     */
    protected void checkStoreEnterCommunity(IPageData pd, String storeId, String storeTypeCd, String communityId, RestTemplate restTemplate) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = getStoreEnterCommunitys(pd, storeId, storeTypeCd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "还未入驻小区，请先入驻小区");
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "data", "还未入驻小区，请先入驻小区");

        JSONObject community = JSONObject.parseObject(responseEntity.getBody().toString());

        JSONArray communitys = community.getJSONArray("data");

        if (communitys == null || communitys.size() == 0) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "还未入驻小区，请先入驻小区");
        }

        JSONObject currentCommunity = getCurrentCommunity(communitys, communityId);

        if (currentCommunity == null) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "传入小区ID非法，请正常操作");
        }

    }

    private JSONObject getCurrentCommunity(JSONArray communitys, String communityId) {
        for (int communityIndex = 0; communityIndex < communitys.size(); communityIndex++) {
            if (communityId.equals(communitys.getJSONObject(communityIndex).getString("communityId"))) {
                return communitys.getJSONObject(communityIndex);
            }
        }

        return null;
    }

    /**
     * 检查用户是否有权限
     *
     * @param pd
     * @param restTemplate
     * @param privilegeCodes
     */
    protected void checkUserHasPrivilege(IPageData pd, RestTemplate restTemplate, String... privilegeCodes) {
        ResponseEntity<String> responseEntity = null;
        if (true) {
            return;
        }
        for (String privilegeCode : privilegeCodes) {
            responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL
                    + "/api/check.user.hasPrivilege?userId=" + pd.getUserId() + "&pId=" + privilegeCode, HttpMethod.GET);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                //throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "用户没有权限操作权限" + privilegeCodes);
                break;
            }
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "用户没有权限操作权限" + privilegeCodes);
        }
    }

    /**
     * 根据 请求路径 判断用户是否有权限操作
     *
     * @param pd
     * @param restTemplate
     */
    protected void checkUserHasPrivilege(IPageData pd, RestTemplate restTemplate) {

        //pd.get

    }


    /**
     * 校验 员工 商户 小区 关系
     * <p>
     * 判断员工和商户是否有关系， 商户和 小区是否有关系
     *
     * @param pd           页面数据封装
     * @param restTemplate http调用工具
     * @return ComponentValidateResult 校验对象
     */
    protected ComponentValidateResult validateStoreStaffCommunityRelationship(IPageData pd, RestTemplate restTemplate) {

        // 校验 员工和商户是否有关系
        ResponseEntity responseEntity = getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, responseEntity.getBody() + "");
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeTypeCd", "根据用户ID查询商户类型失败，未包含storeTypeCd节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        String communityId = "";
        if (paramIn.containsKey("communityId") && !StringUtil.isEmpty(paramIn.getString("communityId"))) {
            communityId = paramIn.getString("communityId");
            checkStoreEnterCommunity(pd, storeId, storeTypeCd, communityId, restTemplate);
        }
        return new ComponentValidateResult(storeId, storeTypeCd, communityId, pd.getUserId(), pd.getUserName());
    }

    /**
     * 校验 员工 商户 关系
     * <p>
     * 判断员工和商户是否有关系， 商户和 是否有关系
     *
     * @param pd           页面数据封装
     * @param restTemplate http调用工具
     * @return ComponentValidateResult 校验对象
     */
    protected ComponentValidateResult validateStoreStaffRelationship(IPageData pd, RestTemplate restTemplate) {

        // 校验 员工和商户是否有关系
        ResponseEntity responseEntity = getStoreInfo(pd, restTemplate);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, responseEntity.getBody() + "");
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeId", "根据用户ID查询商户ID失败，未包含storeId节点");
        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "storeTypeCd", "根据用户ID查询商户类型失败，未包含storeTypeCd节点");

        String storeId = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeId");
        String storeTypeCd = JSONObject.parseObject(responseEntity.getBody().toString()).getString("storeTypeCd");

        return new ComponentValidateResult(storeId, storeTypeCd, "", pd.getUserId(), pd.getUserName());
    }

    /**
     * 分页信息校验
     *
     * @param pd 页面数据封装
     */
    protected void validatePageInfo(IPageData pd) {

        Assert.jsonObjectHaveKey(pd.getReqData(), "row", "请求报文中未包含row节点");
        Assert.jsonObjectHaveKey(pd.getReqData(), "page", "请求报文中未包含page节点");

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.isInteger(paramIn.getString("row"), "row必须为数字");
        Assert.isInteger(paramIn.getString("page"), "page必须为数字");
    }
}
