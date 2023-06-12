package com.java110.boot.smo.api;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IApiSMO {

    /**
     *  主要完成员工是否有访问小区的权限校验
     * @param body
     * @param headers
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
     ResponseEntity<String> doApi(String body, Map<String, String> headers, HttpServletRequest request) throws UnsupportedEncodingException;
}
