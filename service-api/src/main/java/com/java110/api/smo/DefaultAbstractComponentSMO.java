package com.java110.api.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.SecureInvocation;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.cache.PrivilegeCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class DefaultAbstractComponentSMO extends AbstractComponentSMO {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAbstractComponentSMO.class);
    protected static final String DEFAULT_PAY_ADAPT = "wechatPayAdapt";// 默认微信通用支付
    private static final String URL_API = "";

    @Autowired
    private IGetCommunityStoreInfoSMO getCommunityStoreInfoSMOImpl;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private IApiServiceSMO apiServiceSMOImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    //微信支付
    public static final String DOMAIN_WECHAT_PAY = "WECHAT_PAY";
    // 微信服务商支付开关
    public static final String WECHAT_SERVICE_PAY_SWITCH = "WECHAT_SERVICE_PAY_SWITCH";
    //开关ON打开
    public static final String WECHAT_SERVICE_PAY_SWITCH_ON = "ON";

    private static final String WECHAT_SERVICE_APP_ID = "SERVICE_APP_ID";

    private static final String WECHAT_SERVICE_MCH_ID = "SERVICE_MCH_ID";


    /**
     * 调用中心服务
     *
     * @return
     */
    protected ResponseEntity<String> callCenterService(RestTemplate restTemplate, IPageData pd, String param, String url, HttpMethod httpMethod) {

        Assert.notNull(pd.getAppId(), "请求头中未包含应用信息");
        ResponseEntity<String> responseEntity = null;
        if (StringUtil.isEmpty(param)) {
            param = UrlParamToJsonUtil.getJson(url).toJSONString();
        }
        //logger.debug("请求中心服务信息，{}", httpEntity);
        Map<String, String> headers = new HashMap<String, String>();
        if (pd.getHeaders() != null) {
            for (String key : pd.getHeaders().keySet()) {
                headers.put(key, pd.getHeaders().get(key).toString());
            }
        }
        if (!headers.containsKey(CommonConstant.HTTP_USER_ID)) {
            headers.put(CommonConstant.HTTP_USER_ID, StringUtil.isEmpty(pd.getUserId()) ? "-1" : pd.getUserId());
        }

        headers.put(CommonConstant.USER_ID, StringUtil.isEmpty(pd.getUserId()) ? "-1" : pd.getUserId());

        if (!headers.containsKey(CommonConstant.HTTP_USER_ID)) {
            headers.put(CommonConstant.HTTP_USER_ID, StringUtil.isEmpty(pd.getUserId()) ? "-1" : pd.getUserId());
        }
        if (!headers.containsKey(CommonConstant.HTTP_APP_ID)) {
            headers.put(CommonConstant.HTTP_APP_ID, pd.getAppId());
        }
        if (!headers.containsKey(CommonConstant.APP_ID)) {
            headers.put(CommonConstant.APP_ID, pd.getAppId());
        }
        if (!headers.containsKey(CommonConstant.HTTP_TRANSACTION_ID)) {
            headers.put(CommonConstant.HTTP_TRANSACTION_ID, GenerateCodeFactory.getUUID());
        }
        if (!headers.containsKey(CommonConstant.HTTP_REQ_TIME)) {
            headers.put(CommonConstant.HTTP_REQ_TIME, DateUtil.getNowDefault());
        }
        if (!headers.containsKey(CommonConstant.HTTP_SIGN)) {
            headers.put(CommonConstant.HTTP_SIGN, "");
        }

        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        headers.put(CommonConstant.HTTP_SERVICE, url);
        headers.put(CommonConstant.HTTP_METHOD, CommonConstant.getHttpMethodStr(httpMethod));

        if (HttpMethod.GET == httpMethod) {
            initUrlParam(JSONObject.parseObject(param), headers);
        }
        if (HttpMethod.GET == httpMethod) {
            headers.put("REQUEST_URL", "http://127.0.0.1:8008/" + url + mapToUrlParam(JSONObject.parseObject(param)));
        }
        try {
            responseEntity = apiServiceSMOImpl.service(param, headers);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, param, responseEntity);
        }
        return responseEntity;
    }

    /**
     * 调用中心服务
     *
     * @return
     */
    protected ResponseEntity<String> callCenterService(Map<String, String> headers, String param, String url, HttpMethod httpMethod) {

        ResponseEntity<String> responseEntity = null;
        if (StringUtil.isEmpty(param)) {
            param = UrlParamToJsonUtil.getJson(url).toJSONString();
        }

        if (!headers.containsKey(CommonConstant.HTTP_USER_ID)) {
            headers.put(CommonConstant.HTTP_USER_ID, "-1");
        }

        headers.put(CommonConstant.USER_ID, "-1");

        if (!headers.containsKey(CommonConstant.HTTP_USER_ID)) {
            headers.put(CommonConstant.HTTP_USER_ID, "-1");
        }
        if (!headers.containsKey(CommonConstant.HTTP_TRANSACTION_ID)) {
            headers.put(CommonConstant.HTTP_TRANSACTION_ID, GenerateCodeFactory.getUUID());
        }
        if (!headers.containsKey(CommonConstant.HTTP_REQ_TIME)) {
            headers.put(CommonConstant.HTTP_REQ_TIME, DateUtil.getNowDefault());
        }
        if (!headers.containsKey(CommonConstant.HTTP_SIGN)) {
            headers.put(CommonConstant.HTTP_SIGN, "");
        }

        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        headers.put(CommonConstant.HTTP_SERVICE, url);
        headers.put(CommonConstant.HTTP_METHOD, CommonConstant.getHttpMethodStr(httpMethod));

        if (HttpMethod.GET == httpMethod) {
            initUrlParam(JSONObject.parseObject(param), headers);
        }
        if (HttpMethod.GET == httpMethod) {
            headers.put("REQUEST_URL", "http://127.0.0.1:8008/" + url + mapToUrlParam(JSONObject.parseObject(param)));
        }
        try {
            responseEntity = apiServiceSMOImpl.service(param, headers);
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("请求地址为,{} 请求中心服务信息，{},中心服务返回信息，{}", url, param, responseEntity);
        }
        return responseEntity;
    }

    /**
     * 将url参数写到header map中
     *
     * @param paramIn
     */
    protected void initUrlParam(Map paramIn, Map headers) {
        /*put real ip address*/

        if (paramIn != null && !paramIn.isEmpty()) {
            Set<String> keys = paramIn.keySet();
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                headers.put(key, paramIn.get(key));
            }
        }


    }


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        return null;
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
        responseEntity = this.callCenterService(restTemplate, pd, "", "query.user.userInfo?userId=" + pd.getUserId(), HttpMethod.GET);
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
                "user.listUsers?openId=" + openId + "&page=1&row=1", HttpMethod.GET);
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
                "user.listUsers" + mapToUrlParam(paramIn), HttpMethod.GET);
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
                ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS + mapToUrlParam(paramIn), HttpMethod.GET);
        // 过滤返回报文中的字段，只返回name字段
        //{"address":"","orderTypeCd":"Q","serviceCode":"","responseTime":"20190401194712","sex":"","localtionCd":"","userId":"302019033054910001","levelCd":"00","transactionId":"-1","dataFlowId":"-1","response":{"code":"0000","message":"成功"},"name":"996icu","tel":"18909780341","bId":"-1","businessType":"","email":""}
        return responseEntity;

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
                    + "check.user.hasPrivilege?userId=" + pd.getUserId() + "&pId=" + privilegeCode, HttpMethod.GET);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                //throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "用户没有权限操作权限" + privilegeCodes);
                break;
            }
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "用户没有权限操作权限" + privilegeCodes);
        }
    }

    protected void hasPrivilege(RestTemplate restTemplate, IPageData pd, String resource) {
        ResponseEntity<String> responseEntity = null;
        //没有用户的情况下不做权限判断
        if (StringUtil.isEmpty(pd.getUserId())) {
            return;
        }
        JSONObject paramIn = new JSONObject();
        //paramIn.put("resource", resource);
        paramIn.put("userId", pd.getUserId());

        //校验资源路劲是否定义权限
        List<BasePrivilegeDto> basePrivilegeDtos = PrivilegeCache.getPrivileges();
        if (basePrivilegeDtos == null || basePrivilegeDtos.size() < 1) {
            return;
        }
        String tmpResource = null;
        boolean hasPrivilege = false;
        for (BasePrivilegeDto privilegeDto : basePrivilegeDtos) {
            if (resource.equals(privilegeDto.getResource())) {
                hasPrivilege = true;
            }
        }
        if (!hasPrivilege) { //权限没有配置，直接跳过
            return;
        }

        ResultVo resultVo = getCommunityStoreInfoSMOImpl.checkUserHasResourceListener(restTemplate, pd, paramIn, pd.getUserId());
        if (resultVo == null ||
                resultVo.getCode() != ResultVo.CODE_OK) {
            throw new UnsupportedOperationException("用户没有权限操作");
        }
        JSONArray privileges = JSONArray.parseArray(resultVo.getMsg());

        hasPrivilege = false;
        if (privileges == null || privileges.size() < 1) {
            throw new UnsupportedOperationException("用户没有权限操作");
        }
        for (int privilegeIndex = 0; privilegeIndex < privileges.size(); privilegeIndex++) {
            tmpResource = privileges.getJSONObject(privilegeIndex).getString("resource");
            if (resource.equals(tmpResource)) {
                hasPrivilege = true;
                break;
            }
        }
        if (!hasPrivilege) {
            throw new UnsupportedOperationException("用户没有权限操作");
        }

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
     * 校验 员工 商户 小区 关系
     * <p>
     * 判断员工和商户是否有关系， 商户和 小区是否有关系
     *
     * @param pd           页面数据封装
     * @param restTemplate http调用工具
     * @return ComponentValidateResult 校验对象
     */
    protected ComponentValidateResult validateStoreStaffCommunityRelationship(IPageData pd, RestTemplate restTemplate) {

        //获取用户id
        String userId = pd.getUserId();
        if (StringUtil.isEmpty(userId)) {
            return new ComponentValidateResult(null, null, null, null, null);
        }
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        //查询当前用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(users, "查询用户信息错误！");
        if (!StringUtil.isEmpty(users.get(0).getLevelCd()) && !users.get(0).getLevelCd().equals("02")) { //02表示普通用户
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
            if (paramIn.containsKey("communityId")
                    && !StringUtil.isEmpty(paramIn.getString("communityId"))
                    && !"-1".equals(paramIn.getString("communityId"))
                    && !StoreDto.STORE_TYPE_ADMIN.equals(storeTypeCd)
                    && !StoreDto.STORE_TYPE_DEV.equals(storeTypeCd)
            ) {
                communityId = paramIn.getString("communityId");
                checkStoreEnterCommunity(pd, storeId, storeTypeCd, communityId, restTemplate);
            }
            return new ComponentValidateResult(storeId, storeTypeCd, communityId, pd.getUserId(), pd.getUserName());
        } else {
            JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

            String communityId = "";
            if (paramIn.containsKey("communityId") && !StringUtil.isEmpty(paramIn.getString("communityId"))) {
                communityId = paramIn.getString("communityId");
            }
            return new ComponentValidateResult(null, null, communityId, pd.getUserId(), pd.getUserName());
        }
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
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> T getForApi(IPageData pd, T param, String serviceCode, Class<T> t) {

        List<T> list = getForApis(pd, param, serviceCode, t);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> T postForApi(IPageData pd, T param, String serviceCode, Class<T> t) {
        List<T> ts = postForApis(pd, param, serviceCode, t);

        if (ts == null || ts.size() < 1) {
            return null;
        }

        return ts.get(0);
    }

    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> List<T> postForApis(IPageData pd, T param, String serviceCode, Class<T> t) {

        String url = URL_API + serviceCode;


        ResponseEntity<String> responseEntity = callCenterService(restTemplate, pd, JSONObject.toJSONString(param), url, HttpMethod.POST);

        JSONObject resultVo = JSONObject.parseObject(responseEntity.getBody());

        if (ResultVo.CODE_MACHINE_OK != resultVo.getInteger("code")) {
            throw new SMOException(resultVo.getString("msg"));
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException(resultVo.getString("msg"));
        }

        Object bObj = resultVo.get("data");
        JSONArray datas = null;
        if (bObj instanceof JSONObject) {
            datas = new JSONArray();
            datas.add(bObj);
        } else {
            datas = (JSONArray) bObj;
        }
        String jsonStr = JSONObject.toJSONString(datas);

        List<T> list = JSONObject.parseArray(jsonStr, t);
        return list;
    }

    /**
     * 查询
     *
     * @param pd          页面对象
     * @param param       传入对象
     * @param serviceCode 服务编码
     * @param t           返回类
     * @param <T>
     * @return
     */
    protected <T> List<T> getForApis(IPageData pd, T param, String serviceCode, Class<T> t) {

        String url = URL_API + serviceCode;
        if (param != null) {
            url += mapToUrlParam(BeanConvertUtil.beanCovertMap(param));
        }

        ResponseEntity<String> responseEntity = callCenterService(restTemplate, pd, "", url, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SMOException("调用" + serviceCode + "失败，" + responseEntity.getBody());
        }

        JSONObject resultVo = JSONObject.parseObject(responseEntity.getBody());

        if (!"0".equals(resultVo.getString("code"))) {
            throw new SMOException(resultVo.getString("msg"));
        }

        Object bObj = resultVo.get("data");
        JSONArray datas = null;
        if (bObj instanceof JSONObject) {
            datas = new JSONArray();
            datas.add(bObj);
        } else {
            datas = (JSONArray) bObj;
        }
        String jsonStr = JSONObject.toJSONString(datas);

        List<T> list = JSONObject.parseArray(jsonStr, t);
        return list;
    }


}
