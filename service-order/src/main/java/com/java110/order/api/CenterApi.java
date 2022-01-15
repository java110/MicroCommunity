package com.java110.order.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.order.smo.ICenterServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.util.Assert;
import com.java110.core.base.controller.BaseController;
import com.java110.core.event.center.DataFlowEventPublishing;
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
 * 中心http服务 统一服务类
 *  1、只提供service方法
 *  2、提供 透传机制
 * Created by wuxw on 2018/4/13.
 */
@RestController
@Api(value = "中心服务接口服务规范")
@RequestMapping(path = "/centerApi")
@Deprecated
public class CenterApi extends BaseController {

    protected final static Logger logger = LoggerFactory.getLogger(CenterApi.class);


    @Autowired
    private ICenterServiceSMO centerServiceSMOImpl;

    /**
     *
     * @param request HttpServletRequest对象
     * @return
     */
    @RequestMapping(path = "/service",method= RequestMethod.GET)
    @ApiOperation(value="中心服务get方式请求", notes="test: 返回 502 表示服务不支持GET 请求方式")
    public ResponseEntity<String> serviceGet(HttpServletRequest request) {
        return new ResponseEntity<String>("center服务 不支持GET 请求方式", HttpStatus.METHOD_NOT_ALLOWED);
    }
    /**
     *
     * @param request HttpServletRequest对象
     * @return
     */
    @RequestMapping(path = "/service",method= RequestMethod.PUT)
    @ApiOperation(value="中心服务put方式请求", notes="test: 返回 502 表示服务不支持PUT 请求方式")
    public ResponseEntity<String> servicePut(HttpServletRequest request) {
        return new ResponseEntity<String>("center服务 不支持GET 请求方式", HttpStatus.METHOD_NOT_ALLOWED);
    }
    /**
     *
     * @param request HttpServletRequest对象
     * @return
     */
    @RequestMapping(path = "/service",method= RequestMethod.DELETE)
    @ApiOperation(value="中心服务delete方式请求", notes="test: 返回 502 表示服务不支持DELETE 请求方式")
    public ResponseEntity<String> serviceDelete(HttpServletRequest request) {
        return new ResponseEntity<String>("center服务 不支持GET 请求方式", HttpStatus.METHOD_NOT_ALLOWED);
    }


    @RequestMapping(path = "/service",method= RequestMethod.POST)
    @ApiOperation(value="中心服务订单受理", notes="test: 返回 200 表示服务受理成功，其他表示失败")
    @ApiImplicitParam(paramType="query", name = "orderInfo", value = "订单受理信息", required = true, dataType = "String")
    public ResponseEntity<String> servicePost(@RequestBody String orderInfo, HttpServletRequest request) {
        try {
            Map<String, String> headers = new HashMap<String, String>();
            getRequestInfo(request, headers);
            //接受请求事件
            DataFlowEventPublishing.receiveRequest(orderInfo,headers);
            //预校验
            preValiateOrderInfo(orderInfo);
            return centerServiceSMOImpl.serviceApi(orderInfo, headers);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            return new ResponseEntity<String>("请求中心服务发生异常，"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo) {

        Assert.jsonObjectHaveKey(orderInfo,"orders","请求报文中未包含orders节点，"+orderInfo);

        Assert.jsonObjectHaveKey(orderInfo,"business","请求报文中未包含business节点，"+orderInfo);

        if(JSONObject.parseObject(orderInfo).getJSONObject("orders").containsKey("dataFlowId")){
            throw new BusinessException(ResponseConstant.RESULT_CODE_ERROR,"报文中不能存在dataFlowId节点");
        }
    }
    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo,Map<String, String> headers) {

        Assert.hasKey(headers,"serviceCode","没有包含serviceCode");

        Assert.hasLength(headers.get("serviceCode"),"serviceCode 不能为空");

        Assert.hasKey(headers,"appId","没有包含appId");

        Assert.hasLength(headers.get("appId"),"appId 不能为空");
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


    public ICenterServiceSMO getCenterServiceSMOImpl() {
        return centerServiceSMOImpl;
    }

    public void setCenterServiceSMOImpl(ICenterServiceSMO centerServiceSMOImpl) {
        this.centerServiceSMOImpl = centerServiceSMOImpl;
    }
}
