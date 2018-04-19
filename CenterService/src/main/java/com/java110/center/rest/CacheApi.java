package com.java110.center.rest;

import com.java110.center.smo.ICenterServiceCacheSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.ResponseTemplateUtil;
import com.java110.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 主要提供给内部刷缓存用
 * Created by wuxw on 2018/4/18.
 */
@RestController
public class CacheApi extends BaseController{
    @Autowired
    ICenterServiceCacheSMO centerServiceCacheSMOImpl;

    @RequestMapping(path = "/cacheApi/flush",method= RequestMethod.GET)
    public String flushGet(HttpServletRequest request) {

        return ResponseTemplateUtil.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                ResponseConstant.NO_NEED_SIGN,ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求").toJSONString();
    }

    @RequestMapping(path = "/cacheApi/flush",method= RequestMethod.POST)
    public String flushPost(HttpServletRequest request) {
        try {
            centerServiceCacheSMOImpl.flush();
        }catch (Exception e){
            logger.error("刷新缓存异常",e);
            return ResponseTemplateUtil.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                    ResponseConstant.NO_NEED_SIGN,ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
        return ResponseTemplateUtil.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                ResponseConstant.NO_NEED_SIGN,ResponseConstant.RESULT_CODE_SUCCESS,"成功").toJSONString();
    }

    public ICenterServiceCacheSMO getCenterServiceCacheSMOImpl() {
        return centerServiceCacheSMOImpl;
    }

    public void setCenterServiceCacheSMOImpl(ICenterServiceCacheSMO centerServiceCacheSMOImpl) {
        this.centerServiceCacheSMOImpl = centerServiceCacheSMOImpl;
    }
}
