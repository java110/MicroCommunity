package com.java110.service.api;

import com.java110.core.base.controller.BaseController;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.core.trace.Java110TraceLog;
import com.java110.service.context.DataQuery;
import com.java110.service.context.DataQueryFactory;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.util.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
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
 * 查询服务
 * add by wuxw on 2018/4/20.
 * modify by wuxw on 2019/4/20.
 *
 * @version 1.1
 */
@RestController
@Api(value = "查询业务统一提供服务")
public class BusinessApi extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BusinessApi.class);

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;


    @RequestMapping(path = "/businessApi/query", method = RequestMethod.GET)
    @ApiOperation(value = "业务查询get请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "method", value = "用户编号", required = true, dataType = "String")
    @Deprecated
    @Java110TraceLog
    public ResponseEntity<String> service(HttpServletRequest request) {
        try {
            Map<String, Object> headers = new HashMap<String, Object>();
            this.getRequestInfo(request, headers);
            Assert.isNotNull(headers, CommonConstant.HTTP_SERVICE.toLowerCase(), "请求信息中没有包含service信息");
            //Assert.isNotNull(headers, CommonConstant.HTTP_PARAM,"请求信息中没有包含参数（params）信息");
            Map readOnlyMap = super.getParameterStringMap(request);
            headers.put("params", readOnlyMap);
            DataQuery dataQuery = DataQueryFactory.newInstance().builder(headers);
            initConfig(dataQuery);
            queryServiceSMOImpl.commonQueryService(dataQuery);
            return dataQuery.getResponseEntity();
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            //DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
            return new ResponseEntity<String>("请求发生异常，" + e.getMessage() + e, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * {
     * "bId":"12345678",
     * "serviceCode": "querycustinfo",
     * "serviceName": "查询客户",
     * "remark": "备注",
     * "datas": {
     * "params": {
     * //这个做查询时的参数
     * }
     * //这里是具体业务
     * }
     * }
     *
     * @param businessInfo
     * @return
     */
    @RequestMapping(path = "/businessApi/query", method = RequestMethod.POST)
    @ApiOperation(value = "业务查询post请求", notes = "test: 返回 2XX 表示服务正常")
    @ApiImplicitParam(paramType = "query", name = "method", value = "用户编号", required = true, dataType = "String")
    @Deprecated
    @Java110TraceLog
    public ResponseEntity<String> queryPost(@RequestBody String businessInfo) {
        try {
            DataQuery dataQuery = DataQueryFactory.newInstance().builder(businessInfo);
            initConfig(dataQuery);
            queryServiceSMOImpl.commonQueryService(dataQuery);
            return dataQuery.getResponseEntity();
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            //DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR,e.getMessage()+e).toJSONString();
            return new ResponseEntity<String>("请求发生异常，" + e.getMessage() + e, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Deprecated
    @RequestMapping(path = "/businessApi/do", method = RequestMethod.GET)
    @Java110TraceLog
    public String doGet(HttpServletRequest request) {
        return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR, "不支持Get方法请求").toJSONString();
    }

    /**
     * {
     * "bId":"12345678",
     * "serviceCode": "querycustinfo",
     * "serviceName": "查询客户",
     * "remark": "备注",
     * "datas": {
     * "params": {
     * //这个做查询时的参数
     * }
     * //这里是具体业务
     * }
     * }
     *
     * @param businessInfo
     * @return
     */
    @Deprecated
    @RequestMapping(path = "/businessApi/do", method = RequestMethod.POST)
    @Java110TraceLog
    public String doPost(@RequestBody String businessInfo) {
        try {
            DataQuery dataQuery = DataQueryFactory.newInstance().builder(businessInfo);
            initConfig(dataQuery);
            //这里应该添加 只有配置类数据才能处理数据，业务类数据不能操作 逻辑
            queryServiceSMOImpl.commonDoService(dataQuery);
            return dataQuery.getResponseInfo().toJSONString();
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            return DataTransactionFactory.createBusinessResponseJson(ResponseConstant.RESULT_CODE_ERROR, e.getMessage() + e).toJSONString();
        }
    }

    /**
     * 初始化配置
     *
     * @param dataQuery
     */
    private void initConfig(DataQuery dataQuery) {
        dataQuery.setServiceSql(DataQueryFactory.getServiceSql(dataQuery));
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


    @RequestMapping(path = "/businessApi/fallBack", method = RequestMethod.POST)
    @Java110TraceLog
    public ResponseEntity<String> fallBack(@RequestBody String fallBackInfo) {
        try {
            return queryServiceSMOImpl.fallBack(fallBackInfo);
        } catch (Exception e) {
            logger.error("请求订单异常", e);
            return new ResponseEntity<String>("处理异常", HttpStatus.BAD_REQUEST);
        }
    }

    public IQueryServiceSMO getQueryServiceSMOImpl() {
        return queryServiceSMOImpl;
    }

    public void setQueryServiceSMOImpl(IQueryServiceSMO queryServiceSMOImpl) {
        this.queryServiceSMOImpl = queryServiceSMOImpl;
    }
}
