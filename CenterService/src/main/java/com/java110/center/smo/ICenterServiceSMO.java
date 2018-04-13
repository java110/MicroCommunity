package com.java110.center.smo;

import javax.servlet.http.HttpServletRequest;

/**
 * 中心服务 SMO 业务逻辑接口
 * Created by wuxw on 2018/4/13.
 */
public interface ICenterServiceSMO {

    /**
     * 业务统一处理服务方法
     * @param reqJson 请求报文json
     * @return
     */
    public String service(String reqJson, HttpServletRequest request) ;
}
