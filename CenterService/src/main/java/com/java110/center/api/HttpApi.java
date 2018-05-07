package com.java110.center.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.center.smo.ICenterServiceSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.exception.BusinessException;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 中心http服务 统一服务类
 *  1、只提供service方法
 * Created by wuxw on 2018/4/13.
 */
@RestController
public class HttpApi extends BaseController {

    @Autowired
    private ICenterServiceSMO centerServiceSMOImpl;

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
     * 这里预校验，请求报文中不能有 dataFlowId
     * @param orderInfo
     */
    private void preValiateOrderInfo(String orderInfo) {
        if(JSONObject.parseObject(orderInfo).getJSONObject("orders").containsKey("dataFlowId")){
            throw new BusinessException(ResponseConstant.RESULT_CODE_ERROR,"报文中不能存在dataFlowId节点");
        }
    }

    /**
     * 获取请求信息
     * @param request
     * @param headers
     * @throws RuntimeException
     */
    private void getRequestInfo(HttpServletRequest request,Map headers) throws RuntimeException{
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
