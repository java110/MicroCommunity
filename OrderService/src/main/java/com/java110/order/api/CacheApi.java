package com.java110.order.api;

import com.java110.order.smo.ICenterServiceCacheSMO;
import com.java110.common.constant.ResponseConstant;
import com.java110.core.base.controller.BaseController;
import com.java110.core.factory.DataQueryFactory;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.entity.service.DataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 主要提供给内部刷缓存用
 * Created by wuxw on 2018/4/18.
 */
@RestController
public class CacheApi extends BaseController{
    protected final static Logger logger = LoggerFactory.getLogger(CacheApi.class);
    @Autowired
    ICenterServiceCacheSMO centerServiceCacheSMOImpl;

    @RequestMapping(path = "/cacheApi/flush",method= RequestMethod.GET)
    public ResponseEntity<String> flushGet(HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            this.getRequestInfo(request, headers);
            centerServiceCacheSMOImpl.flush(headers);
            responseEntity = new ResponseEntity<String>("刷新缓存成功", HttpStatus.OK);
        }catch (Exception e){
            logger.error("刷新缓存失败，",e);
            responseEntity = new ResponseEntity<String>("刷新缓存失败，"+e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        return responseEntity;
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

    public ICenterServiceCacheSMO getCenterServiceCacheSMOImpl() {
        return centerServiceCacheSMOImpl;
    }

    public void setCenterServiceCacheSMOImpl(ICenterServiceCacheSMO centerServiceCacheSMOImpl) {
        this.centerServiceCacheSMOImpl = centerServiceCacheSMOImpl;
    }
}
