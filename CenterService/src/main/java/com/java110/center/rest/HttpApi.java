package com.java110.center.rest;

import com.java110.center.smo.ICenterServiceSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.ResponseTemplateUtil;
import com.java110.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        return ResponseTemplateUtil.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                ResponseConstant.NO_NEED_SIGN,ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求");
    }

    @RequestMapping(path = "/httpApi/service",method= RequestMethod.POST)
    public String servicePost(@RequestBody String orderInfo, HttpServletRequest request) {
        return centerServiceSMOImpl.service(orderInfo, request);
    }

    public ICenterServiceSMO getCenterServiceSMOImpl() {
        return centerServiceSMOImpl;
    }

    public void setCenterServiceSMOImpl(ICenterServiceSMO centerServiceSMOImpl) {
        this.centerServiceSMOImpl = centerServiceSMOImpl;
    }
}
