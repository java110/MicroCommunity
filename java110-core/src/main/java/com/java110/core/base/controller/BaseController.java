package com.java110.core.base.controller;


import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.NoAuthorityException;
import com.java110.common.log.LoggerEngine;

import com.java110.core.base.AppBase;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;

/**
 * 所有控制类的父类，统一参数处理
 * 或公用逻辑处理
 * Created by wuxw on 2017/2/23.
 */
public class BaseController extends AppBase {


    /**
     * 检查用户登录
     * @throws NoAuthorityException
     */
    protected void checkLogin() throws NoAuthorityException{
        if(false){
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR,"用户未登录，请登录！");
        }
    }


    /**
     * 将url参数写到header map中
     * @param request
     */
    protected void initUrlParam(HttpServletRequest request,Map headers) {
		/*put real ip address*/

        Map readOnlyMap = request.getParameterMap();

        StringBuffer queryString = new StringBuffer(request.getRequestURL()!=null?request.getRequestURL():"");

        if (readOnlyMap != null && !readOnlyMap.isEmpty()) {
            queryString.append("?");
            Set<String> keys = readOnlyMap.keySet();
            for (Iterator it = keys.iterator(); it.hasNext();) {
                String key = (String) it.next();
                String[] value = (String[]) readOnlyMap.get(key);
//                String[] value = (String[]) readOnlyMap.get(key);
                if(value.length>1) {
                    headers.put(key, value[0]);
                    for(int j =0 ;j<value.length;j++){
                        queryString.append(key);
                        queryString.append("=");
                        queryString.append(value[j]);
                        queryString.append("&");
                    }

                } else {
                    headers.put(key, value[0]);
                    queryString.append(key);
                    queryString.append("=");
                    queryString.append(value[0]);
                    queryString.append("&");
                }
            }
        }

		/*put requst url*/
        if (readOnlyMap != null && !readOnlyMap.isEmpty()){
            headers.put("REQUEST_URL",queryString.toString().substring(0, queryString.toString().length() - 1));
        }else{
            headers.put("REQUEST_URL",queryString.toString());
        }

    }

    protected void initHeadParam(HttpServletRequest request,Map headers) {

        Enumeration reqHeaderEnum = request.getHeaderNames();

        while( reqHeaderEnum.hasMoreElements() ) {
            String headerName = (String)reqHeaderEnum.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        headers.put("IP",getIpAddr(request));
    }

    /**
     * 获取IP地址
     * @param request
     * @return
     */
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
