package com.java110.api.rest;

import com.java110.api.smo.IApiServiceSMO;
import com.java110.common.constant.CommonConstant;
import com.java110.core.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * rest api
 * Created by wuxw on 2018/10/16.
 */
@RestController

@RequestMapping(path = "/api")
@Api(value = "对外统一提供服务接口服务")
public class RestApi extends BaseController {


    @Autowired
    private IApiServiceSMO apiServiceSMOImpl;
    /**
     * 健康检查 服务
     * @return
     */
    @RequestMapping(path = "/health",method = RequestMethod.GET)
    @ApiOperation(value="服务健康检查", notes="test: 返回 2XX 表示服务正常")
    public String health(){
        return "";
    }


    /**
     * 资源请求 post方式
     * @param service 请求接口方式
     * @param postInfo post内容
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}",method = RequestMethod.POST )
    @ApiOperation(value="资源post请求", notes="test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType="query", name = "method", value = "用户编号", required = true, dataType = "String")
    public ResponseEntity<String> servicePost(@PathVariable String service,
                                              @RequestBody String postInfo,
                                              HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE,service);
            headers.put(CommonConstant.HTTP_METHOD,CommonConstant.HTTP_METHOD_POST);
            responseEntity = apiServiceSMOImpl.service(postInfo,headers);
        }catch (Throwable e){
            logger.error("请求post 方法["+service+"]失败：",e);
            return new ResponseEntity<String>("请求发生异常，"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * 资源请求 get方式
     * @param service 请求接口方式
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}",method = RequestMethod.GET )
    @ApiOperation(value="资源get请求", notes="test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType="query", name = "service", value = "用户编号", required = true, dataType = "String")
    public ResponseEntity<String> serviceGet(@PathVariable String service,
                                              HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE,service);
            headers.put(CommonConstant.HTTP_METHOD,CommonConstant.HTTP_METHOD_GET);
            responseEntity = apiServiceSMOImpl.service("",headers);
        }catch (Throwable e){
            logger.error("请求get 方法["+service+"]失败：",e);
            return new ResponseEntity<String>("请求发生异常，"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * 资源请求 put方式
     * @param service 请求接口方式
     * @param postInfo 修改内容
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}",method = RequestMethod.PUT )
    @ApiOperation(value="资源put请求", notes="test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType="query", name = "service", value = "用户编号", required = true, dataType = "String")
    public ResponseEntity<String> servicePut(@PathVariable String service,
                                             @RequestBody String postInfo,
                                             HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE,service);
            headers.put(CommonConstant.HTTP_METHOD,CommonConstant.HTTP_METHOD_PUT);
            responseEntity = apiServiceSMOImpl.service(postInfo,headers);
        }catch (Throwable e){
            logger.error("请求put 方法["+service+"]失败：",e);
            return new ResponseEntity<String>("请求发生异常，"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    /**
     * 资源请求 delete方式
     * @param service 请求接口方式
     * @param request 请求对象 查询头信息 url等信息
     * @return http status 200 成功 其他失败
     */

    @RequestMapping(path = "/{service:.+}",method = RequestMethod.DELETE )
    @ApiOperation(value="资源delete请求", notes="test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType="query", name = "method", value = "用户编号", required = true, dataType = "String")
    public ResponseEntity<String> serviceDelete(@PathVariable String service,
                                             HttpServletRequest request){
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE,service);
            headers.put(CommonConstant.HTTP_METHOD,CommonConstant.HTTP_METHOD_DELETE);
            responseEntity = apiServiceSMOImpl.service("",headers);
        }catch (Throwable e){
            logger.error("请求delete 方法["+service+"]失败：",e);
            return new ResponseEntity<String>("请求发生异常，"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }



    /**
     * 获取请求信息
     * @param request
     * @param headers
     * @throws RuntimeException
     */
    private void getRequestInfo(HttpServletRequest request,Map headers) throws Exception{
        try{
            super.initHeadParam(request,headers);
            super.initUrlParam(request,headers);
        }catch (Exception e){
            logger.error("加载头信息失败",e);
            throw e;
        }
    }


    public IApiServiceSMO getApiServiceSMOImpl() {
        return apiServiceSMOImpl;
    }

    public void setApiServiceSMOImpl(IApiServiceSMO apiServiceSMOImpl) {
        this.apiServiceSMOImpl = apiServiceSMOImpl;
    }
}
