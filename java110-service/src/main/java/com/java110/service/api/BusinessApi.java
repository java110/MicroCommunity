package com.java110.service.api;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.factory.DataQueryFactory;
import com.java110.common.factory.DataTransactionFactory;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.service.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 查询服务
 * Created by wuxw on 2018/4/20.
 */
@RestController
public class BusinessApi extends BaseController {

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @RequestMapping(path = "/businessApi/query",method= RequestMethod.GET)
    public String queryGet(HttpServletRequest request) {
        return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求").toJSONString();
    }

    /**
     * {
     "bId":"12345678",
     "serviceCode": "querycustinfo",
     "serviceName": "查询客户",
     "remark": "备注",
     "datas": {
     "params": {
     //这个做查询时的参数
     }
     //这里是具体业务
     }
     }
     * @param businessInfo
     * @return
     */
    @RequestMapping(path = "/businessApi/query",method= RequestMethod.POST)
    public String queryPost(@RequestBody String businessInfo) {
        try {
            DataQuery dataQuery = DataQueryFactory.newInstance().builder(businessInfo);
            initConfig(dataQuery);
            queryServiceSMOImpl.commonQueryService(dataQuery);
            return dataQuery.getResponseInfo().toJSONString();
        }catch (Exception e){
            logger.error("请求订单异常",e);
            return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
    }
    @Deprecated
    @RequestMapping(path = "/businessApi/do",method= RequestMethod.GET)
    public String doGet(HttpServletRequest request) {
        return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,"不支持Get方法请求").toJSONString();
    }

    /**
     * {
     "bId":"12345678",
     "serviceCode": "querycustinfo",
     "serviceName": "查询客户",
     "remark": "备注",
     "datas": {
     "params": {
     //这个做查询时的参数
     }
     //这里是具体业务
     }
     }
     * @param businessInfo
     * @return
     */
    @Deprecated
    @RequestMapping(path = "/businessApi/do",method= RequestMethod.POST)
    public String doPost(@RequestBody String businessInfo) {
        try {
            DataQuery dataQuery = DataQueryFactory.newInstance().builder(businessInfo);
            initConfig(dataQuery);
            queryServiceSMOImpl.commonDoService(dataQuery);
            return dataQuery.getResponseInfo().toJSONString();
        }catch (Exception e){
            logger.error("请求订单异常",e);
            return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
        }
    }

    /**
     * 初始化配置
     * @param dataQuery
     */
    private void initConfig(DataQuery dataQuery){
        dataQuery.setServiceSql(DataQueryFactory.getServiceSql(dataQuery));
    }

    public IQueryServiceSMO getQueryServiceSMOImpl() {
        return queryServiceSMOImpl;
    }

    public void setQueryServiceSMOImpl(IQueryServiceSMO queryServiceSMOImpl) {
        this.queryServiceSMOImpl = queryServiceSMOImpl;
    }
}
