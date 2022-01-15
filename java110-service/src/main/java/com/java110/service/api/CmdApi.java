package com.java110.service.api;

import com.java110.core.base.controller.BaseController;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.service.context.DataQuery;
import com.java110.service.context.DataQueryFactory;
import com.java110.service.smo.ICmdServiceSMO;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询服务
 * add by wuxw on 2018/4/20.
 * modify by wuxw on 2019/4/20.
 *
 * @version 1.1
 */
@RestController
@RequestMapping(path = "/cmd")
public class CmdApi extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(CmdApi.class);

    @Autowired
    private ICmdServiceSMO cmdServiceSMOImpl;


    @RequestMapping(path = "/{service:.+}", method = RequestMethod.POST)
    public ResponseEntity<String> service(@PathVariable String service,
                                          @RequestBody String postInfo,
                                          HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        Map<String, String> headers = new HashMap<String, String>();
        try {

            this.getRequestInfo(request, headers);
            headers.put(CommonConstant.HTTP_SERVICE, service);
            headers.put(CommonConstant.HTTP_METHOD, CommonConstant.HTTP_METHOD_POST);
            logger.debug("api：{} 请求报文为：{},header信息为：{}", service, postInfo, headers);
            responseEntity = cmdServiceSMOImpl.cmd(postInfo, headers);
        } catch (Throwable e) {
            logger.error("请求post 方法[" + service + "]失败：" + postInfo, e);
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("cmd：{} 返回信息为：{}", service, responseEntity);
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
            responseEntity = cmdServiceSMOImpl.cmd(postInfo, headers);
        } catch (Throwable e) {
            logger.error("请求post 方法[" + action + "]失败：" + postInfo, e);
            responseEntity = new ResponseEntity<String>("请求发生异常，" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.debug("cmd：{} 返回信息为：{}", action, responseEntity);
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

}
