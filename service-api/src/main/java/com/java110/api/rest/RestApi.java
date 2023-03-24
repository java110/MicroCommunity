package com.java110.api.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.IApiServiceSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.log.LoggerFactory;
import com.java110.doc.annotation.Java110ApiDoc;
import com.java110.doc.annotation.Java110RequestMappingDoc;
import com.java110.doc.annotation.Java110RequestMappingsDoc;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.vo.ResultVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * rest api
 * Created by wuxw on 2018/10/16.
 */
@RestController
@RequestMapping(path = "/api")
@Api(value = "对外统一提供服务接口服务")
@Java110ApiDoc(
        title = "HC小区管理系统api接口文档",
        description = "HC小区管理系统api接口文档",
        company="Java110工作室",
        version = "v1.4"
)

@Java110RequestMappingsDoc(
        mappingsDocs = {
                @Java110RequestMappingDoc(name="用户中心",resource = "userDoc",url="http://user-service",seq = 1),
                @Java110RequestMappingDoc(name="资产中心",resource = "communityDoc",url="http://community-service",seq = 2),
                @Java110RequestMappingDoc(name="商户中心",resource = "storeDoc",url="http://store-service",seq = 3),
                @Java110RequestMappingDoc(name="账户中心",resource = "acctDoc",url="http://acct-service",seq = 4),
                @Java110RequestMappingDoc(name="通用中心",resource = "commonDoc",url="http://common-service",seq = 5),
                @Java110RequestMappingDoc(name="费用中心",resource = "feeDoc",url="http://fee-service",seq = 6),
                @Java110RequestMappingDoc(name="报表中心",resource = "reportDoc",url="http://report-service",seq = 7),
        }
)
public class RestApi extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RestApi.class);
    private static final String VERSION = "version";
    private static final String VERSION_2 = "2.0";
    @Autowired
    private IApiServiceSMO apiServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;



    /**
     * 健康检查 服务
     *
     * @return
     */
    @RequestMapping(path = "/health", method = RequestMethod.GET)
    @ApiOperation(value = "服务健康检查", notes = "test: 返回 2XX 表示服务正常")
    public String health() {
        return "";
    }

    /**
     * 健康检查 服务
     *
     * @return
     */
    @RequestMapping(path = "/checkUserServiceVersion", method = RequestMethod.GET)
    @ApiOperation(value = "检查用服务版本", notes = "test: 返回 2XX 表示服务正常")
    public String checkUserServiceVersion() {
        return userInnerServiceSMOImpl.getUserServiceVersion("test");
    }


    /**
     * 资源请求 post方式
     *
     * @param service  请求接口方式
     * @param postInfo post内容
     * @param request  请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */
    @RequestMapping(path = "/{service:.+}", method = RequestMethod.POST)
    @ApiOperation(value = "资源post请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "service", value = "用户编号", required = true, dataType = "String")
    public ResponseEntity<String> servicePost(@PathVariable String service,
                                              @RequestBody String postInfo,
                                              HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {

            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_POST);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, postInfo, headers);
            responseEntity = apiServiceSMOImpl.service(postInfo, headers);
        } catch (Throwable e) {
            logger.error("请求post 方法[" + service + "]失败：" + postInfo, e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", service, responseEntity);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        //当 接口版本号为2.0时 返回错误处理
        if (headers.containsKey(VERSION) && VERSION_2.equals(headers.get(VERSION))) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
        return responseEntity;
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
    public ResponseEntity<String> serviceGet(@PathVariable String service,
                                             HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_GET);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, headers);
            responseEntity = apiServiceSMOImpl.service(JSONObject.toJSONString(getParameterStringMap(request)), headers);
        } catch (Throwable e) {
            logger.error("请求get 方法[" + service + "]失败：", e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", service, responseEntity);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        //当 接口版本号为2.0时 返回错误处理
        if (headers.containsKey(VERSION) && VERSION_2.equals(headers.get(VERSION))) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
        }

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
    public ResponseEntity<String> servicePut(@PathVariable String service,
                                             @RequestBody String postInfo,
                                             HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {

            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_PUT);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, postInfo, headers);
            responseEntity = apiServiceSMOImpl.service(postInfo, headers);
        } catch (Throwable e) {
            logger.error("请求put 方法[" + service + "]失败：", e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", service, responseEntity);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        //当 接口版本号为2.0时 返回错误处理
        if (headers.containsKey(VERSION) && VERSION_2.equals(headers.get(VERSION))) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
        }

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
    public ResponseEntity<String> serviceDelete(@PathVariable String service,
                                                HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {

            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_DELETE);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, "", headers);

            responseEntity = apiServiceSMOImpl.service(JSONObject.toJSONString(getParameterStringMap(request)), headers);
        } catch (Throwable e) {
            logger.error("请求delete 方法[" + service + "]失败：", e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("api：{} 返回信息为：{}", service, responseEntity);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        //当 接口版本号为2.0时 返回错误处理
        if (headers.containsKey(VERSION) && VERSION_2.equals(headers.get(VERSION))) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
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
        } catch (Exception e) {
            logger.error("加载头信息失败", e);
            throw e;
        }
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
            responseEntity = apiServiceSMOImpl.service(postInfo, headers);
        } catch (Throwable e) {
            logger.error("请求post 方法[" + action + "]失败：" + postInfo, e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", action, responseEntity);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        //当 接口版本号为2.0时 返回错误处理
        if (headers.containsKey(VERSION) && VERSION_2.equals(headers.get(VERSION))) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
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
            responseEntity = apiServiceSMOImpl.service(JSONObject.toJSONString(getParameterStringMap(request)), headers);
        } catch (Throwable e) {
            logger.error("请求get 方法[" + action + "]失败：", e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("api：{} 返回信息为：{}", action, responseEntity);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        //当 接口版本号为2.0时 返回错误处理
        if (headers.containsKey(VERSION) && VERSION_2.equals(headers.get(VERSION))) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, responseEntity.getBody());
        }

        return responseEntity;
    }





    public IApiServiceSMO getApiServiceSMOImpl() {
        return apiServiceSMOImpl;
    }

    public void setApiServiceSMOImpl(IApiServiceSMO apiServiceSMOImpl) {
        this.apiServiceSMOImpl = apiServiceSMOImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
