package com.java110.boot.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.boot.smo.IApiServiceSMO;
import com.java110.core.client.RestTemplate;
import com.java110.core.context.ApiDataFlow;
import com.java110.core.context.DataFlow;
import com.java110.core.event.service.api.ServiceDataFlowEventPublishing;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.ISaveTransactionLogSMO;
import com.java110.core.trace.Java110TraceLog;
import com.java110.dto.order.OrderDto;
import com.java110.dto.system.AppRoute;
import com.java110.dto.system.AppService;
import com.java110.dto.system.DataFlowLinksCost;
import com.java110.po.log.TransactionLogPo;
import com.java110.utils.cache.AppRouteCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.KafkaConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.*;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.BootReplaceUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 中心服务处理类
 * Created by wuxw on 2018/4/13.
 */
@Service("apiServiceSMOImpl")
//@Transactional
public class ApiServiceSMOImpl extends LoggerEngine implements IApiServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(ApiServiceSMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private ISaveTransactionLogSMO saveTransactionLogSMOImpl;


    /**
     * 服务调度
     *
     * @param reqJson 请求报文json
     * @param headers
     * @return
     * @throws SMOException
     */
    @Override
    @Java110TraceLog
    public ResponseEntity<String> service(String reqJson, Map<String, String> headers) throws SMOException {

        ApiDataFlow dataFlow = null;

        Date startDate = DateUtil.getCurrentDate();

        ResponseEntity<String> responseEntity = null;

        String resJson = "";

        try {
            //todo 在post和 put 时才存在报文加密的情况
            if ("POST,PUT".contains(headers.get(CommonConstant.HTTP_METHOD))) {
                reqJson = decrypt(reqJson, headers);
            }

            //todo 1.0 创建数据流 appId serviceCode
            dataFlow = DataFlowFactory.newInstance(ApiDataFlow.class).builder(reqJson, headers);

            //todo 2.0 加载配置信息
            initConfigData(dataFlow);

            //todo 3.0 校验 APPID是否有权限操作serviceCode
            judgeAuthority(dataFlow);

            //todo 6.0 调用下游系统
            invokeBusinessSystem(dataFlow);

            responseEntity = dataFlow.getResponseEntity();

        } catch (DecryptException e) { //解密异常
            logger.error("内部异常：", e);
            responseEntity = ResultVo.error("解密异常：" + e.getMessage());
        } catch (BusinessException e) {
            logger.error("内部异常：", e);
            responseEntity = ResultVo.error(e.getMessage());
        } catch (NoAuthorityException e) {
            logger.error("内部异常：", e);
            responseEntity = ResultVo.error("鉴权失败：" + e.getMessage());
        } catch (InitConfigDataException e) {
            logger.error("内部异常：", e);
            responseEntity = ResultVo.error("初始化失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("内部异常：", e);
            responseEntity = ResultVo.error("内部异常：" + e.getMessage() + e.getLocalizedMessage());
        } finally {
            Date endDate = DateUtil.getCurrentDate();
            if (dataFlow != null) {
                //这里记录日志
                dataFlow.setEndDate(endDate);
                //处理返回报文鉴权
                //AuthenticationFactory.putSign(dataFlow);
            }
            if (responseEntity == null) {
                //resJson = encrypt(responseJson.toJSONString(),headers);
                responseEntity = new ResponseEntity<String>(resJson, HttpStatus.OK);
            }

            //添加耗时
            saveLog(dataFlow, startDate, endDate, reqJson, responseEntity);
            //这里保存耗时，以及日志
        }
        return responseEntity;
    }

    /**
     * 日志记录
     *
     * @param dataFlow
     * @param startDate
     * @param endDate
     */
    private void saveLog(ApiDataFlow dataFlow, Date startDate, Date endDate, String reqJson, ResponseEntity<String> responseEntity) {

        if (dataFlow == null) {
            return;
        }

        String serviceCode = dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE);

        String logServiceCode = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingCache.LOG_SERVICE_CODE);

        //日志查询不记录
        if ("/transactionLog/queryTransactionLog".equals(serviceCode)
                || "/transactionLog/queryTransactionLogMessage".equals(serviceCode)
                || "file.getFile".equals(serviceCode)
                || "file.getFileByObjId".equals(serviceCode)
                || "/machine/heartbeat".equals(serviceCode) // 心跳也不记录
                ) {
            return;
        }

        if (StringUtil.isEmpty(logServiceCode) || "OFF".equals(logServiceCode.toUpperCase())) {
            return;
        }
        if (logServiceCode.contains("|")) {
            String[] logServiceCodes = logServiceCode.split("|");

            for (String lServiceCode : logServiceCodes) {
                if (serviceCode.equals(lServiceCode.trim())) {
                    doSaveLog(dataFlow, startDate, endDate, serviceCode, reqJson, responseEntity);
                    return;
                }
            }
        }

        if ("all".equals(logServiceCode.trim().toLowerCase())) {
            doSaveLog(dataFlow, startDate, endDate, serviceCode, reqJson, responseEntity);
            return;
        }

        if (serviceCode.equals(logServiceCode.trim())) {
            doSaveLog(dataFlow, startDate, endDate, serviceCode, reqJson, responseEntity);
        }
    }


    private void doSaveLog(ApiDataFlow dataFlow, Date startDate, Date endDate, String serviceCode, String reqJson, ResponseEntity<String> responseEntity) {

        TransactionLogPo transactionLogPo = new TransactionLogPo();
        transactionLogPo.setAppId(dataFlow.getAppId());
        transactionLogPo.setCostTime((endDate.getTime() - startDate.getTime()) + "");
        transactionLogPo.setIp("");
        transactionLogPo.setServiceCode(serviceCode);
        transactionLogPo.setSrcIp(dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SRC_IP));
        transactionLogPo.setState(responseEntity.getStatusCode() != HttpStatus.OK ? "F" : "S");
        transactionLogPo.setTimestamp(dataFlow.getRequestTime());
        transactionLogPo.setUserId(dataFlow.getUserId());
        transactionLogPo.setTransactionId(dataFlow.getTransactionId());
        transactionLogPo.setRequestHeader(dataFlow.getRequestHeaders() != null ? dataFlow.getRequestHeaders().toString() : "");
        transactionLogPo.setResponseHeader(responseEntity.getHeaders().toSingleValueMap().toString());
        transactionLogPo.setRequestMessage(reqJson);
        transactionLogPo.setResponseMessage(responseEntity.getBody());
        saveTransactionLogSMOImpl.saveLog(transactionLogPo);
    }


    /**
     * 抒写返回头信息
     *
     * @param dataFlow
     */
    private void putResponseHeader(DataFlow dataFlow, Map<String, String> headers) {
        headers.put("responseTime", DateUtil.getDefaultFormateTimeString(new Date()));
        headers.put("transactionId", dataFlow.getTransactionId());
    }

    /**
     * 解密
     *
     * @param reqJson
     * @return
     */
    private String decrypt(String reqJson, Map<String, String> headers) throws DecryptException {
        try {
            if (MappingConstant.VALUE_ON.equals(headers.get(CommonConstant.ENCRYPT))) {
                logger.debug("解密前字符：" + reqJson);
                reqJson = new String(AuthenticationFactory.decrypt(reqJson.getBytes("UTF-8"),
                        AuthenticationFactory.loadPrivateKey(MappingConstant.KEY_PRIVATE_STRING)
                        , NumberUtils.isNumber(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) ?
                                Integer.parseInt(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) :
                                Integer.parseInt(MappingCache.getValue(MappingConstant.KEY_DEFAULT_DECRYPT_KEY_SIZE))), "UTF-8");
                logger.debug("解密后字符：" + reqJson);
            }
        } catch (Exception e) {
            throw new DecryptException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "解密失败");
        }

        return reqJson;
    }

    /**
     * 加密
     *
     * @param resJson
     * @param headers
     * @return
     */
    private String encrypt(String resJson, Map<String, String> headers) {
        try {
            if (MappingConstant.VALUE_ON.equals(headers.get(CommonConstant.ENCRYPT))) {
                logger.debug("加密前字符：" + resJson);
                resJson = new String(AuthenticationFactory.encrypt(resJson.getBytes("UTF-8"), AuthenticationFactory.loadPubKey(MappingConstant.KEY_PUBLIC_STRING)
                        , NumberUtils.isNumber(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) ? Integer.parseInt(headers.get(CommonConstant.ENCRYPT_KEY_SIZE)) :
                                Integer.parseInt(MappingCache.getValue(MappingConstant.KEY_DEFAULT_DECRYPT_KEY_SIZE))), "UTF-8");
                logger.debug("加密后字符：" + resJson);
            }
        } catch (Exception e) {
            logger.error("加密失败：", e);
        }
        return resJson;
    }


    /**
     * 2.0初始化配置信息
     *
     * @param dataFlow
     */
    private void initConfigData(ApiDataFlow dataFlow) {
        Date startDate = DateUtil.getCurrentDate();
        //查询配置信息，并将配置信息封装到 dataFlow 对象中
        List<AppRoute> appRoutes = AppRouteCache.getAppRoute(dataFlow.getAppId());

        if (appRoutes == null) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "initConfigData", "加载配置耗时", startDate);
            throw new InitConfigDataException(ResponseConstant.RESULT_CODE_INNER_ERROR, "当前没有获取到AppId对应的信息，appId = " + dataFlow.getAppId());
        }
        for (AppRoute appRoute : appRoutes) {
            dataFlow.addAppRoutes(appRoute);
        }
        //
        if ("-1".equals(dataFlow.getDataFlowId()) || StringUtil.isNullOrNone(dataFlow.getDataFlowId())) {
            dataFlow.setDataFlowId(GenerateCodeFactory.getDataFlowId());
        }

        //添加耗时
        DataFlowFactory.addCostTime(dataFlow, "initConfigData", "加载配置耗时", startDate);
    }

    /**
     * 3.0判断 AppId 是否 有serviceCode权限
     *
     * @param dataFlow
     * @throws RuntimeException
     */
    private void judgeAuthority(ApiDataFlow dataFlow) throws NoAuthorityException {
        Date startDate = DateUtil.getCurrentDate();

        if (StringUtil.isNullOrNone(dataFlow.getAppId()) || dataFlow.getAppRoutes().size() == 0) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "APP_ID 为空或不正确");
        }

        if (StringUtil.isNullOrNone(dataFlow.getTransactionId())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "TRANSACTION_ID 不能为空");
        }

        if (!StringUtil.isNullOrNone(dataFlow.getAppRoutes().get(0).getSecurityCode())) {
            String sign = AuthenticationFactory.apiDataFlowMd5(dataFlow);
            if (StringUtil.isEmpty(dataFlow.getReqSign()) || !sign.equals(dataFlow.getReqSign().toLowerCase())) {
                throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "签名失败");
            }
        }

        if (StringUtil.isNullOrNone(dataFlow.getRequestTime()) || !DateUtil.judgeDate(dataFlow.getRequestTime(), DateUtil.DATE_FORMATE_STRING_DEFAULT)) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "requestTime 格式不对，遵循yyyyMMddHHmmss格式");
        }
        //todo 用户ID校验
        if (StringUtil.isNullOrNone(dataFlow.getUserId())) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "USER_ID 不能为空");
        }


        //todo 判断 AppId 是否有权限操作相应的服务
        AppService appService = DataFlowFactory.getService(dataFlow, dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE));

        //这里调用缓存 查询缓存信息
        if (appService == null || !CommonConstant.HTTP_SERVICE_API.equals(appService.getBusinessTypeCd())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "AppId 没有权限访问 serviceCode = " + dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE));
        }


        //检验白名单
        List<String> whileListIp = dataFlow.getAppRoutes().get(0).getWhileListIp();
        if (whileListIp != null && whileListIp.size() > 0 && !whileListIp.contains(dataFlow.getIp())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "当前IP被限制不能访问服务");
        }

        //检查黑名单
        List<String> backListIp = dataFlow.getAppRoutes().get(0).getBackListIp();
        if (backListIp != null && backListIp.size() > 0 && backListIp.contains(dataFlow.getIp())) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "当前IP被限制不能访问服务");
        }
        //添加耗时
        DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
    }


    /**
     * 6.0 调用下游系统
     *
     * @param dataFlow
     * @throws BusinessException
     */
    private void invokeBusinessSystem(ApiDataFlow dataFlow) throws BusinessException {
        Date startDate = DateUtil.getCurrentDate();
        //todo 拿到当前服务
        AppService appService = DataFlowFactory.getService(dataFlow, dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE));
        //todo 这里对透传类处理,目前很少用到，可以不用关注
        if ("NT".equals(appService.getIsInstance())) {
            //如果是透传类 请求方式必须与接口提供方调用方式一致
            String httpMethod = dataFlow.getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
            if (!appService.getMethod().equals(httpMethod)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                        "服务【" + appService.getServiceCode() + "】调用方式不对请检查,当前请求方式为：" + httpMethod);
            }
            //dataFlow.setApiCurrentService(ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_TRANSFER);
            doNT(appService, dataFlow, dataFlow.getReqJson());
            return;
        } else if ("T".equals(appService.getIsInstance())) { // todo 通过透传方式 调用 目前很少用到，可以不用关注
            //todo 如果是透传类 请求方式必须与接口提供方调用方式一致
            String httpMethod = dataFlow.getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
            if (!appService.getMethod().equals(httpMethod)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                        "服务【" + appService.getServiceCode() + "】调用方式不对请检查,当前请求方式为：" + httpMethod);
            }
            doTransfer(appService, dataFlow, dataFlow.getReqJson());
            return;
        } else if ("CMD".equals(appService.getIsInstance())) { // todo 微服务调用方式，目前主要用这种方式调度分发 到不同的微服务，这里是通过c_service 中配置 调用到不同的微服务
            //如果是透传类 请求方式必须与接口提供方调用方式一致
            String httpMethod = dataFlow.getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
            if (!appService.getMethod().equals(httpMethod)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                        "服务【" + appService.getServiceCode() + "】调用方式不对请检查,当前请求方式为：" + httpMethod);
            }
            // todo 根据接口编码找到 appService 也就是c_service 表中的内容
            dealCmd(appService, dataFlow, dataFlow.getReqJson());
            return;
        } else {
            dataFlow.setApiCurrentService(dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE));
        }
        ServiceDataFlowEventPublishing.multicastEvent(dataFlow, appService);
        DataFlowFactory.addCostTime(dataFlow, "invokeBusinessSystem", "调用下游系统耗时", startDate);
    }

    private void doNT(AppService service, ApiDataFlow dataFlow, JSONObject reqJson) {


        HttpHeaders header = new HttpHeaders();
        for (String key : dataFlow.getRequestCurrentHeaders().keySet()) {
            header.add(key, dataFlow.getRequestCurrentHeaders().get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toJSONString(), header);
        //http://user-service/test/sayHello

        ResponseEntity responseEntity = null;
        //配置c_service 时请注意 如果是以out 开头的调用外部的地址

        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(service.getMethod())) {
                String requestUrl = dataFlow.getRequestHeaders().get("REQUEST_URL");
                if (!StringUtil.isNullOrNone(requestUrl)) {
                    String param = requestUrl.contains("?") ? requestUrl.substring(requestUrl.indexOf("?") + 1, requestUrl.length()) : "";
                    if (service.getUrl().contains("?")) {
                        requestUrl = service.getUrl() + "&" + param;
                    } else {
                        requestUrl = service.getUrl() + "?" + param;
                    }
                }

                requestUrl = BootReplaceUtil.replaceServiceName(requestUrl);

                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(service.getMethod())) {
                responseEntity = outRestTemplate.exchange(service.getUrl(), HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(service.getMethod())) {
                String requestUrl = dataFlow.getRequestHeaders().get("REQUEST_URL");
                if (!StringUtil.isNullOrNone(requestUrl)) {
                    String param = requestUrl.contains("?") ? requestUrl.substring(requestUrl.indexOf("?"), requestUrl.length()) : "";
                    if (service.getUrl().contains("?")) {
                        requestUrl = service.getUrl() + "&" + param;
                    } else {
                        requestUrl = service.getUrl() + "?" + param;
                    }
                }
                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {

                String requestUrl = BootReplaceUtil.replaceServiceName(service.getUrl());
                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
            }
        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        }

        logger.debug("API 服务调用下游服务请求：{}，返回为：{}", httpEntity, responseEntity);

        dataFlow.setResponseEntity(responseEntity);
    }

    private void doTransfer(AppService appService, ApiDataFlow dataFlow, JSONObject reqJson) {

        Map<String, String> reqHeader = dataFlow.getRequestCurrentHeaders();
        HttpHeaders header = new HttpHeaders();
        for (String key : dataFlow.getRequestCurrentHeaders().keySet()) {
            header.add(key, reqHeader.get(key));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toJSONString(), header);
        String orgRequestUrl = dataFlow.getRequestHeaders().get("REQUEST_URL");

        //String serviceCode = "/" + reqHeader.get(CommonConstant.HTTP_RESOURCE) + "/" + reqHeader.get(CommonConstant.HTTP_ACTION);
        String serviceCode = appService.getServiceCode();
        serviceCode = serviceCode.startsWith("/") ? serviceCode : ("/" + serviceCode);

        String requestUrl = "http://127.0.0.1:8008" + serviceCode;

        ResponseEntity responseEntity = null;
        if (!StringUtil.isNullOrNone(orgRequestUrl)) {
            String param = orgRequestUrl.contains("?") ? orgRequestUrl.substring(orgRequestUrl.indexOf("?") + 1, orgRequestUrl.length()) : "";
            requestUrl += ("?" + param);
        }
        try {
            if (CommonConstant.HTTP_METHOD_GET.equals(appService.getMethod())) {
                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_PUT.equals(appService.getMethod())) {
                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
            } else if (CommonConstant.HTTP_METHOD_DELETE.equals(appService.getMethod())) {
                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
            } else {
                responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
            }
            HttpHeaders headers = responseEntity.getHeaders();
            String oId = "-1";
            if (headers.containsKey(OrderDto.O_ID)) {
                oId = headers.get(OrderDto.O_ID).get(0);
            }

//            //进入databus
//            if (!CommonConstant.HTTP_METHOD_GET.equals(appService.getMethod())) {
//
//                // dealDatabus(serviceCode, reqJson, oId);
//            }


        } catch (HttpStatusCodeException e) { //这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            logger.error("请求下游服务【" + requestUrl + "】异常，参数为" + httpEntity + e.getResponseBodyAsString(), e);
            String body = e.getResponseBodyAsString();

            if (StringUtil.isJsonObject(body)) {
                JSONObject bodyObj = JSONObject.parseObject(body);
                if (bodyObj.containsKey("message") && !StringUtil.isEmpty(bodyObj.getString("message"))) {
                    body = bodyObj.getString("message");
                }
            }
            responseEntity = new ResponseEntity<String>(body, e.getStatusCode());
        }

        logger.debug("API 服务调用下游服务请求：{}，返回为：{}", httpEntity, responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, String.valueOf(responseEntity.getBody()));
            dataFlow.setResponseEntity(responseEntity);

            return;
        }
        if (StringUtils.isEmpty(responseEntity.getBody() + "")) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "处理失败");
            dataFlow.setResponseEntity(responseEntity);
            return;
        }
        JSONObject resParam = JSONObject.parseObject(responseEntity.getBody() + "");
        if (resParam.containsKey("code") && resParam.containsKey("msg")) { // 说明微服务返回的是 resultVo 对象直接返回就可以
            dataFlow.setResponseEntity(responseEntity);
            return;
        }
        responseEntity = ResultVo.createResponseEntity(resParam);
        dataFlow.setResponseEntity(responseEntity);
    }

    /**
     * 开始调度微服务
     * @param appService
     * @param dataFlow
     * @param reqJson
     */
    private void dealCmd(AppService appService, ApiDataFlow dataFlow, JSONObject reqJson) {
        Map<String, String> reqHeader = dataFlow.getRequestCurrentHeaders();
        HttpHeaders header = new HttpHeaders();
        //todo 对头信息重新包装
        for (String key : dataFlow.getRequestCurrentHeaders().keySet()) {
            if("userName".equals(key) || "user-name".equals(key)){
                header.add(key, "-");
                continue;
            }
            header.add(key, reqHeader.get(key));
        }
        //todo 用户信息再次包装
        if (reqHeader.containsKey(CommonConstant.USER_ID)
                && (!reqJson.containsKey("userId") || StringUtil.isEmpty(reqJson.getString("userId")))) {
            reqJson.put("userId", reqHeader.get(CommonConstant.USER_ID));
        }
        if (reqHeader.containsKey(CommonConstant.USER_ID)
                && (!reqJson.containsKey("loginUserId") || StringUtil.isEmpty(reqJson.getString("loginUserId")))) {
            reqJson.put("loginUserId", reqHeader.get(CommonConstant.LOGIN_U_ID));
        }
        if (reqHeader.containsKey(CommonConstant.STORE_ID)
                && (!reqJson.containsKey("storeId") || StringUtil.isEmpty(reqJson.getString("storeId")))) {
            reqJson.put("storeId", reqHeader.get(CommonConstant.STORE_ID));
        }
        HttpEntity<String> httpEntity = new HttpEntity<String>(reqJson.toJSONString(), header);
        String orgRequestUrl = dataFlow.getRequestHeaders().get("REQUEST_URL");

        String serviceCode = appService.getServiceCode();

        serviceCode = serviceCode.startsWith("/") ? serviceCode : ("/" + serviceCode);

        //todo 组装调用微服务的地址
        String requestUrl = "http://127.0.0.1:8008/cmd" + serviceCode;
        //
        ResponseEntity responseEntity = null;
        //todo url 带了地址这里 拼接
        if (!StringUtil.isNullOrNone(orgRequestUrl)) {
            String param = orgRequestUrl.contains("?") ? orgRequestUrl.substring(orgRequestUrl.indexOf("?") + 1, orgRequestUrl.length()) : "";
            requestUrl += ("?" + param);
        }
        try {
            //todo http的方式调用微服务，相应的java类可以到相应微服务下的cmd下根据serviceCode 的寻找
            //todo 这里会调用到 java110-service 模块下的 CmdApi 类，这个类各个微服务都会集成
            responseEntity = outRestTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
            HttpHeaders headers = responseEntity.getHeaders();
            String oId = "-1";
            if (headers.containsKey(OrderDto.O_ID)) {
                oId = headers.get(OrderDto.O_ID).get(0);
            }

        } catch (HttpStatusCodeException e) { //todo 这里spring 框架 在4XX 或 5XX 时抛出 HttpServerErrorException 异常，需要重新封装一下
            logger.error("请求下游服务【" + requestUrl + "】异常，参数为" + httpEntity + e.getResponseBodyAsString(), e);
            String body = e.getResponseBodyAsString();

            if (StringUtil.isJsonObject(body)) {
                JSONObject bodyObj = JSONObject.parseObject(body);
                if (bodyObj != null && bodyObj.containsKey("message") && !StringUtil.isEmpty(bodyObj.getString("message"))) {
                    body = bodyObj.getString("message");
                }
            }
            responseEntity = new ResponseEntity<String>(body, e.getStatusCode());
        }

        logger.debug("API 服务调用下游服务请求：{}，返回为：{}", httpEntity, responseEntity);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, String.valueOf(responseEntity.getBody()));
            dataFlow.setResponseEntity(responseEntity);
            return;
        }
        if (StringUtils.isEmpty(responseEntity.getBody() + "")) {
            responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "处理失败");
            dataFlow.setResponseEntity(responseEntity);
            return;
        }
        dataFlow.setResponseEntity(responseEntity);
    }


    /**
     * 保存日志信息
     *
     * @param requestJson
     */
    private void saveLogMessage(String requestJson, String responseJson) {

        try {
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_LOG_ON_OFF))) {
                JSONObject log = new JSONObject();
                log.put("request", requestJson);
                log.put("response", responseJson);
                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_LOG_NAME, "", log.toJSONString());
            }
        } catch (Exception e) {
            logger.error("报错日志出错了，", e);
        }
    }

    /**
     * 保存耗时信息
     *
     * @param dataFlow
     */
    private void saveCostTimeLogMessage(DataFlow dataFlow) {
        try {
            if (MappingConstant.VALUE_ON.equals(MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingConstant.KEY_COST_TIME_ON_OFF))) {
                List<DataFlowLinksCost> dataFlowLinksCosts = dataFlow.getLinksCostDates();
                JSONObject costDate = new JSONObject();
                JSONArray costDates = new JSONArray();
                JSONObject newObj = null;
                for (DataFlowLinksCost dataFlowLinksCost : dataFlowLinksCosts) {
                    newObj = JSONObject.parseObject(JSONObject.toJSONString(dataFlowLinksCost));
                    newObj.put("dataFlowId", dataFlow.getDataFlowId());
                    newObj.put("transactionId", dataFlow.getTransactionId());
                    costDates.add(newObj);
                }
                costDate.put("costDates", costDates);

                KafkaFactory.sendKafkaMessage(KafkaConstant.TOPIC_COST_TIME_LOG_NAME, "", costDate.toJSONString());
            }
        } catch (Exception e) {
            logger.error("报错日志出错了，", e);
        }
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
