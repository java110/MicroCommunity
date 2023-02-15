package com.java110.api.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.IApiServiceSMO;
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
import com.java110.entity.center.AppRoute;
import com.java110.entity.center.AppService;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.po.transactionLog.TransactionLogPo;
import com.java110.utils.cache.AppRouteCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.exception.*;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
            //在post和 put 时才存在报文加密的情况
            if ("POST,PUT".contains(headers.get(CommonConstant.HTTP_METHOD))) {
                reqJson = decrypt(reqJson, headers);
            }

            //1.0 创建数据流 appId serviceCode
            dataFlow = DataFlowFactory.newInstance(ApiDataFlow.class).builder(reqJson, headers);

            //2.0 加载配置信息
            initConfigData(dataFlow);

            //3.0 校验 APPID是否有权限操作serviceCode
            judgeAuthority(dataFlow);

            //6.0 调用下游系统
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
            if (!sign.equals(dataFlow.getReqSign().toLowerCase())) {
                throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "签名失败");
            }
        }

        if (StringUtil.isNullOrNone(dataFlow.getRequestTime()) || !DateUtil.judgeDate(dataFlow.getRequestTime(), DateUtil.DATE_FORMATE_STRING_DEFAULT)) {
            //添加耗时
            DataFlowFactory.addCostTime(dataFlow, "judgeAuthority", "鉴权耗时", startDate);
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "requestTime 格式不对，遵循yyyyMMddHHmmss格式");
        }
        //用户ID校验
        if (StringUtil.isNullOrNone(dataFlow.getUserId())) {
            throw new NoAuthorityException(ResponseConstant.RESULT_CODE_NO_AUTHORITY_ERROR, "USER_ID 不能为空");
        }


        //判断 AppId 是否有权限操作相应的服务
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
        //拿到当前服务
        AppService appService = DataFlowFactory.getService(dataFlow, dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE));
        //这里对透传类处理
        if ("NT".equals(appService.getIsInstance())) {
            //如果是透传类 请求方式必须与接口提供方调用方式一致
            String httpMethod = dataFlow.getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
            if (!appService.getMethod().equals(httpMethod)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                        "服务【" + appService.getServiceCode() + "】调用方式不对请检查,当前请求方式为：" + httpMethod);
            }
            dataFlow.setApiCurrentService(ServiceCodeConstant.SERVICE_CODE_DO_SERVICE_TRANSFER);
        } else if ("T".equals(appService.getIsInstance())) {
            //如果是透传类 请求方式必须与接口提供方调用方式一致
            String httpMethod = dataFlow.getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
            if (!appService.getMethod().equals(httpMethod)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                        "服务【" + appService.getServiceCode() + "】调用方式不对请检查,当前请求方式为：" + httpMethod);
            }
            dataFlow.setApiCurrentService(ServiceCodeConstant.SERVICE_CODE_SYSTEM_TRANSFER);
        } else if ("CMD".equals(appService.getIsInstance())) {
            //如果是透传类 请求方式必须与接口提供方调用方式一致
            String httpMethod = dataFlow.getRequestCurrentHeaders().get(CommonConstant.HTTP_METHOD);
            if (!appService.getMethod().equals(httpMethod)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR,
                        "服务【" + appService.getServiceCode() + "】调用方式不对请检查,当前请求方式为：" + httpMethod);
            }
            dataFlow.setApiCurrentService(ServiceCodeConstant.SERVICE_CODE_SYSTEM_CMD);
        } else {
            dataFlow.setApiCurrentService(dataFlow.getRequestHeaders().get(CommonConstant.HTTP_SERVICE));
        }
        ServiceDataFlowEventPublishing.multicastEvent(dataFlow, appService);
        DataFlowFactory.addCostTime(dataFlow, "invokeBusinessSystem", "调用下游系统耗时", startDate);
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
