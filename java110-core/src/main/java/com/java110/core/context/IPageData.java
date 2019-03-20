package com.java110.core.context;

import org.springframework.http.ResponseEntity;

/**
 * 页面数据封装对象
 *
 * add by wuxw 2019-03-19
 */
public interface IPageData {

    public String getUserId();

    public String getTransactionId();

    public String getComponentCode();

    public String getComponentMethod();

    public String getToken();

    public void setToken(String token);

    public String getReqData();

    public String getResponseTime();

    public String getRequestTime();


    public ResponseEntity getResponseEntity();


    public void setResponseEntity(ResponseEntity responseEntity);

    public IPageData builder(String userId,String token,String reqData,String componentCode,String componentMethod,String url) throws IllegalArgumentException;

}
