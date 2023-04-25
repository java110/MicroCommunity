/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.api.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.IApiServiceSMO;
import com.java110.api.smo.api.IApiSMO;
import com.java110.api.smo.privilege.IPrivilegeSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.language.Java110Lang;
import com.java110.core.log.LoggerFactory;
import com.java110.utils.constant.CommonConstant;
import com.java110.vo.ResultVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序api处理类
 * <p>
 * 主要用于透传api 直接提供出来的接口
 * <p>
 * 方便快速开发
 * <p>
 * add by wuxw 2019-11-19
 */
@RestController
@RequestMapping(path = "/app")
public class AppController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(AppController.class);



    @Autowired
    private IApiSMO apiSMOImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IPrivilegeSMO privilegeSMOImpl;

    /**
     * 资源请求 post方式
     * <p>
     * /app/user.listUser
     * <p>
     * /api/user.listUser
     *
     * @param service  请求接口方式
     * @param postInfo post内容
     * @param request  请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}", method = RequestMethod.POST)
    @ApiOperation(value = "资源post请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "service", value = "用户编号", required = true, dataType = "String")
    @Java110Lang
    public ResponseEntity<String> servicePost(@PathVariable String service,
                                              @RequestBody String postInfo,
                                              HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_POST);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, postInfo, headers);
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/app/" + service);
            responseEntity = apiSMOImpl.doApi(postInfo, headers,request);
            //todo 写入 token
            wirteToken(request,pd,service,responseEntity);
        } catch (Throwable e) {
            logger.error("请求post 方法[" + service + "]失败：" + postInfo, e);
            responseEntity = ResultVo.error("请求发生异常，" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", service, responseEntity);

        return responseEntity;
    }

    /**
     * 写入 token
     * @param request
     * @param pd
     * @param service
     * @param responseEntity
     */
    private void wirteToken(HttpServletRequest request, IPageData pd, String service, ResponseEntity<String> responseEntity) {
        String[] services = new String[]{
                "login.accessTokenLogin"
        };

        if(responseEntity.getStatusCode() != HttpStatus.OK){
            return;
        }
        boolean flag = false;
        for(String tmpService : services){
            if(tmpService.equals(service)){
                flag =true;
            }
        }

        if(!flag){
            return;
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        if(!"0".equals(paramOut.getString("code"))){
            return;
        }
        String token = paramOut.getJSONObject("data").getString("token");
        pd.setToken(token);
    }

    /**
     * 资源请求 get方式
     *
     * @param service 请求接口方式
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}", method = RequestMethod.GET)
    @ApiOperation(value = "资源get请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "service", value = "用户编号", required = true, dataType = "String")
    @Java110Lang
    public ResponseEntity<String> serviceGet(@PathVariable String service,
                                             HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_GET);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", "", headers);
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/app/" + service);
            responseEntity = apiSMOImpl.doApi(JSONObject.toJSONString(getParameterStringMap(request)), headers, request);

        } catch (Throwable e) {
            logger.error("请求get 方法[" + service + "]失败：", e);
            responseEntity = ResultVo.error("请求发生异常，" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", service, responseEntity);

        return responseEntity;
    }

    /**
     * 资源请求 get方式
     *
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{resource}/{action}", method = RequestMethod.GET)
    @ApiOperation(value = "资源get请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "subServiceGet", value = "用户编号", required = true, dataType = "String")
    @Java110Lang
    public ResponseEntity<String> subServiceGet(
            @PathVariable String resource,
            @PathVariable String action,
            HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, "/" + resource + "/" + action);
            headers.put(CommonConstant.HTTP_RESOURCE, resource);
            headers.put(CommonConstant.HTTP_ACTION, action);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_GET);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", "", headers);
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/app/" + resource + "/" + action);
            responseEntity = apiSMOImpl.doApi(JSONObject.toJSONString(getParameterStringMap(request)), headers, request);
            //responseEntity = apiServiceSMOImpl.service(JSONObject.toJSONString(getParameterStringMap(request)), headers);
        } catch (Throwable e) {
            logger.error("请求get 方法[" + action + "]失败：", e);
            responseEntity = ResultVo.error("请求发生异常，" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", action, responseEntity);

        return responseEntity;
    }

    /**
     * 资源请求 post方式
     *
     * @param resource 请求接口方式
     * @param postInfo post内容
     * @param request  请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */
    @RequestMapping(path = "/{resource}/{action}", method = RequestMethod.POST)
    @ApiOperation(value = "资源post请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "subServicePost", value = "用户编号", required = true, dataType = "String")
    @Java110Lang
    public ResponseEntity<String> subServicePost(
            @PathVariable String resource,
            @PathVariable String action,
            @RequestBody String postInfo,
            HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, "/" + resource + "/" + action);
            headers.put(CommonConstant.HTTP_RESOURCE, resource);
            headers.put(CommonConstant.HTTP_ACTION, action);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_POST);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", action, postInfo, headers);
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/app/" + resource + "/" + action);
            responseEntity = apiSMOImpl.doApi(postInfo, headers, request);

        } catch (Throwable e) {
            logger.error("请求post 方法[" + action + "]失败：" + postInfo, e);
            responseEntity = ResultVo.error("请求发生异常，" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", action, responseEntity);

        return responseEntity;
    }

    /**
     * 资源请求 put方式
     *
     * @param service  请求接口方式
     * @param postInfo 修改内容
     * @param request  请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}", method = RequestMethod.PUT)
    @ApiOperation(value = "资源put请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "service", value = "用户编号", required = true, dataType = "String")
    @Java110Lang
    public ResponseEntity<String> servicePut(@PathVariable String service,
                                             @RequestBody String postInfo,
                                             HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_PUT);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, postInfo, headers);
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/app/" + service);
            responseEntity = apiSMOImpl.doApi(postInfo, headers, request);
            //responseEntity = apiServiceSMOImpl.service(JSONObject.toJSONString(getParameterStringMap(request)), headers);
        } catch (Throwable e) {
            logger.error("请求put 方法[" + service + "]失败：", e);
            responseEntity = ResultVo.error("请求发生异常，" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", service, responseEntity);
        return responseEntity;
    }

    /**
     * 资源请求 delete方式
     *
     * @param service 请求接口方式
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}", method = RequestMethod.DELETE)
    @ApiOperation(value = "资源delete请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "service", value = "用户编号", required = true, dataType = "String")
    @Java110Lang
    public ResponseEntity<String> serviceDelete(@PathVariable String service,
                                                HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_DELETE);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, "", headers);
            IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);
            privilegeSMOImpl.hasPrivilege(restTemplate, pd, "/app/" + service);
            responseEntity = apiSMOImpl.doApi(JSONObject.toJSONString(getParameterStringMap(request)), headers, request);
            //responseEntity = apiServiceSMOImpl.service(JSONObject.toJSONString(getParameterStringMap(request)), headers);
        } catch (Throwable e) {
            logger.error("请求delete 方法[" + service + "]失败：", e);
            responseEntity = ResultVo.error("请求发生异常，" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("api：{} 返回信息为：{}", service, responseEntity);
        return responseEntity;
    }


    /**
     * 获取请求信息
     *
     * @param request
     * @param headers
     * @throws RuntimeException
     */
    private void getRequestInfo(HttpServletRequest request, Map headers) throws Exception {
        try {
            super.initHeadParam(request, headers);
            super.initUrlParam(request, headers);
            this.getUserInfo(request, headers);

        } catch (Exception e) {
            logger.error("加载头信息失败", e);
            throw e;
        }
    }


    private void getUserInfo(HttpServletRequest request, Map headers) throws Exception {
        Object claimsObj = request.getAttribute("claims");
        if (claimsObj == null) {
            return;
        }
        Map<String, String> claims = (Map<String, String>) claimsObj;

        for (String key : claims.keySet()) {

            if ("userId".equals(key)) {
                headers.put("user_id", claims.get(key));
            }
            headers.put(key, claims.get(key));
        }
    }
}
