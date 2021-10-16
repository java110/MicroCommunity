package com.java110.api.smo.login;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 组织管理管理服务接口类
 * <p>
 * add by wuxw 2019-06-29
 */
public interface IOwnerAppLoginSMO {

    /**
     * 获取微信回话信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> doLogin(IPageData pd) throws SMOException;

    /**
     * 根据key 登录
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> doLoginByKey(IPageData pd) throws SMOException;

    /**
     * 获取access_token
     *
     * @param pd
     * @return
     * @throws SMOException
     */
    ResponseEntity<String> getPageAccessToken(IPageData pd, HttpServletRequest request) throws SMOException;


    /**
     * 微信刷新token
     *
     * @param pd
     * @return
     * @throws SMOException
     */
    ResponseEntity<String> refreshToken(IPageData pd, String redirectUrl, String errorUrl,
                                        String loginFlag,
                                        HttpServletRequest request, HttpServletResponse response) throws SMOException;

    /**
     * 刷新openId
     *
     * @param pd
     * @param redirectUrl
     * @param request
     * @param response
     * @return
     */
    ResponseEntity<String> refreshOpenId(IPageData pd, String redirectUrl, String wAppId, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity openServiceNotifyOpenId(IPageData pd, HttpServletRequest request);
}
