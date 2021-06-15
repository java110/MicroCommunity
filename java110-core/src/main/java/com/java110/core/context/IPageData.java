package com.java110.core.context;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 页面数据封装对象
 * <p>
 * add by wuxw 2019-03-19
 */
public interface IPageData {

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    String getUserId();

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    String getUserName();

    String getAppId();

    /**
     * 获取交易流水
     *
     * @return 交易流水
     */
    String getTransactionId();

    /**
     * 获取组件编码
     *
     * @return 组件编码
     */
    String getComponentCode();

    /**
     * 获取调用的组件方法
     *
     * @return 组件方法
     */
    String getComponentMethod();

    /**
     * 获取token
     *
     * @return token
     */
    String getToken();

    /**
     * 设置token
     *
     * @param token 登录成功时需要设置token
     */
    void setToken(String token);

    /**
     * 获取sessionID
     *
     * @return sessionID
     */
    String getSessionId();

    /**
     * 获取前台请求的数据
     *
     * @return 前台请求的数据
     */
    String getReqData();

    /**
     * 获取返回时间
     *
     * @return 返回时间
     */
    String getResponseTime();

    /**
     * 获取请求时间
     *
     * @return 请求时间
     */
    String getRequestTime();


    /**
     * 获取 ResponseEntity
     *
     * @return ResponseEntity
     */
    ResponseEntity getResponseEntity();


    /**
     * 设置 ResponseEntity
     *
     * @param responseEntity 返回界面时的对象
     */
    void setResponseEntity(ResponseEntity responseEntity);

    /**
     * 获取调用api 地址
     *
     * @return
     */
    public String getApiUrl();

    //设置调用api 服务地址
    public void setApiUrl(String apiUrl);

    public HttpMethod getMethod();

    Map<String, Object> getHeaders();

    public void setMethod(HttpMethod method);

    public String getPayerObjId();

    public void setPayerObjId(String payerObjId);

    public String getPayerObjType();

    public void setPayerObjType(String payerObjType);

    public String getEndTime();

    public void setEndTime(String endTime);

    /**
     * 构建 pd 对象
     *
     * @param userId          用户ID
     * @param token           token
     * @param reqData         请求数据
     * @param componentCode   组件编码
     * @param componentMethod 组件方法
     * @param url             请求url
     * @param sessionId       会话ID
     * @return IPageData对象
     * @throws IllegalArgumentException 参数错误异常
     */
    IPageData builder(String userId,
                      String userName,
                      String token,
                      String reqData,
                      String componentCode,
                      String componentMethod,
                      String url,
                      String sessionId,
                      String appId)
            throws IllegalArgumentException;

    /**
     * 构建 pd 对象
     *
     * @param userId          用户ID
     * @param token           token
     * @param reqData         请求数据
     * @param componentCode   组件编码
     * @param componentMethod 组件方法
     * @param url             请求url
     * @param sessionId       会话ID
     * @return IPageData对象
     * @throws IllegalArgumentException 参数错误异常
     */
    IPageData builder(String userId,
                      String userName,
                      String token,
                      String reqData,
                      String componentCode,
                      String componentMethod,
                      String url,
                      String sessionId,
                      String appId,
                      Map<String, Object> headers)
            throws IllegalArgumentException;

    IPageData builder(String userId,
                      String userName,
                      String token,
                      String reqData,
                      String componentCode,
                      String componentMethod,
                      String url,
                      String sessionId)
            throws IllegalArgumentException;

    IPageData builder(String userId,
                      String userName,
                      String token,
                      String reqData,
                      String componentCode,
                      String componentMethod,
                      String url,
                      String sessionId,
                      String appId,
                      String payerObjId,
                      String payerObjType,
                      String endTime)
            throws IllegalArgumentException;

}
