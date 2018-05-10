package com.java110.center.api;

import com.java110.center.smo.ICenterServiceCacheSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.factory.DataQueryFactory;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.DataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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

        return DataTransactionFactory.createOrderResponseJson(ResponseConstant.NO_TRANSACTION_ID,
                ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求").toJSONString();
    }

    @RequestMapping(path = "/cacheApi/flush",method= RequestMethod.POST)
    public String flushPost(@RequestBody String businessInfo) {
        try {
            DataQuery dataQuery = DataQueryFactory.newInstance().builder(businessInfo);
            centerServiceCacheSMOImpl.flush(dataQuery);
            return dataQuery.getResponseInfo().toJSONString();
        }catch (Exception e){
            logger.error("请求订单异常",e);
            return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
    }

    public ICenterServiceCacheSMO getCenterServiceCacheSMOImpl() {
        return centerServiceCacheSMOImpl;
    }

    public void setCenterServiceCacheSMOImpl(ICenterServiceCacheSMO centerServiceCacheSMOImpl) {
        this.centerServiceCacheSMOImpl = centerServiceCacheSMOImpl;
    }
}
