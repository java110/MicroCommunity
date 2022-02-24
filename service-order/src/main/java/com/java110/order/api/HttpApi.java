package com.java110.order.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.order.smo.ICenterServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.util.Assert;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.core.base.controller.BaseController;
import com.java110.core.event.center.DataFlowEventPublishing;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 中心http服务 统一服务类
 *  1、只提供service方法
 *  2、提供 透传机制
 * Created by wuxw on 2018/4/13.
 */
@RestController
@Deprecated
public class HttpApi extends BaseController {

    protected final static Logger logger = LoggerFactory.getLogger(HttpApi.class);

    @Autowired
    private ICenterServiceSMO centerServiceSMOImpl;

    /**
     *
     * @param request HttpServletRequest对象
     * @return
     */
    @RequestMapping(path = "/httpApi/service",method= RequestMethod.GET)
    public String serviceGet(HttpServletRequest request) {
        return DataTransactionFactory.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求").toJSONString();
    }

    @RequestMapping(path = "/httpApi/service",method= RequestMethod.POST)
    public String servicePost(@RequestBody String orderInfo, HttpServletRequest request) {
        try {
            Map<String, String> headers = new HashMap<String, String>();
            getRequestInfo(request, headers);
            //接受请求事件
            DataFlowEventPublishing.receiveRequest(orderInfo,headers);
            //预校验
            preValiateOrderInfo(orderInfo);
            return centerServiceSMOImpl.service(orderInfo, headers);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            return DataTransactionFactory.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                    ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
    }

    /**
     * 对协议不遵循的 接口进行透传
     * @param orderInfo
     * @param request
     * @return
     */
    @RequestMapping(path = "/httpApi/service/{serviceCode:.+}",method= RequestMethod.POST)
    public String servicePostTransfer(@PathVariable String serviceCode, @RequestBody String orderInfo, HttpServletRequest request,
                                      HttpServletResponse response) {
        String resData = "";
        Map<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("serviceCode",serviceCode);
            getRequestInfo(request, headers);
            //预校验
            preValiateOrderInfo(orderInfo,headers);
            resData = centerServiceSMOImpl.serviceTransfer(orderInfo, headers);
        }catch (Exception e){
            logger.error("请求订单异常",e);
            resData = DataTransactionFactory.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                    ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
        for(String key : headers.keySet()) {
            response.addHeader(key,headers.get(key));
        }
        return resData;
    }

    /**
     * 这里预校验，请求报文中不能有 dataFlowId
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo) {

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
