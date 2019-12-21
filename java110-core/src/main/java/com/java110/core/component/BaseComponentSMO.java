package com.java110.core.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * 查询商户信息
     *
     * @return
     */
    protected ResponseEntity<String> getStoreInfo(IPageData pd, RestTemplate restTemplate) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, pd, "", ServiceConstant.SERVICE_API_URL + "/api/query.store.byuser?userId=" + pd.getUserId(), HttpMethod.GET);

        return responseEntity;
    }

    /**
     * 查询商户信息
     *
     * @return
     */
    protected void checkStoreEnterCommunity(IPageData pd, String storeId, String storeTypeCd, String communityId, RestTemplate restTemplate) {
        Assert.hasLength(pd.getUserId(), "用户未登录请先登录");
        ResponseEntity<String> responseEntity = null;
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/query.myCommunity.byMember?memberId=" + storeId + "&memberTypeCd="
                        + MappingCache.getValue(MappingConstant.DOMAIN_STORE_TYPE_2_COMMUNITY_MEMBER_TYPE, storeTypeCd), HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "还未入驻小区，请先入驻小区");
        }

        Assert.jsonObjectHaveKey(responseEntity.getBody().toString(), "communitys", "还未入驻小区，请先入驻小区");

        JSONObject community = JSONObject.parseObject(responseEntity.getBody().toString());

        JSONArray communitys = community.getJSONArray("communitys");

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
     * map 参数转 url get 参数 非空值转为get参数 空值忽略
     *
     * @param info map数据
     * @return url get 参数 带？
     */
    protected String mapToUrlParam(Map info) {
        String urlParam = "";
        if (info == null || info.isEmpty()) {
            return urlParam;
        }

        urlParam += "?";

        for (Object key : info.keySet()) {
            if (StringUtils.isEmpty(info.get(key) + "")) {
                continue;
            }

            urlParam += (key + "=" + info.get(key) + "&");
        }

        urlParam = urlParam.endsWith("&") ? urlParam.substring(0, urlParam.length() - 1) : urlParam;

        return urlParam;
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
        if (paramIn.containsKey("communityId")&& !StringUtil.isEmpty(paramIn.getString("communityId"))) {
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
