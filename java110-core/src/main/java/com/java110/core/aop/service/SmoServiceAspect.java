package com.java110.core.aop.service;

import com.java110.utils.log.LoggerEngine;
//import org.aspectj.lang.ProceedingJoinPoint;


/**
 * 服务端切面，主要处理服务端发生未知异常，导致调用端无法处理，
 * 这里将 异常信息统一协议返回
 * Created by wuxw on 2017/2/28.
 */

public class SmoServiceAspect extends LoggerEngine {

    /**
     * 切面方法
     * @param pjp
     * @return
     * @throws Throwable

    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object[] httpParamObjects = pjp.getArgs();

        Object obj = null;
        Object[] params = null;
        try {
            params = pjp.getArgs();
            obj = pjp.proceed(params);
        } catch (Exception e) {
            //new UtilException(1999,e);
            logger.error("--------[SmoServiceAspect.aroundMethod] ---------出现异常：", e);
            String transactionId = "-1";
            String serviceCode = "-1";
            String resultCode = ProtocolUtil.RETURN_MSG_ERROR;
            String resultMsg = "失败，失败原因:";
            if (httpParamObjects.length != 0) {
                if (httpParamObjects[0] instanceof String) {
                    TcpCont tcpCont = ProtocolUtil.getTcpCont(StringUtils.isEmpty(httpParamObjects[0]) ? null : httpParamObjects[0].toString());
                    if (tcpCont != null) {
                        transactionId = tcpCont.getTransactionId();
                        serviceCode = tcpCont.getServiceCode();
                        obj = ProtocolUtil.createResponseJsonString(transactionId, serviceCode, resultMsg + e, null);
                        return obj;
                    }
                }
            }
            obj = ProtocolUtil.createResponseJsonString(transactionId, serviceCode,resultCode, resultMsg + " 报文格式错误 " + e, null);
        }finally {

            String saveServiceLogFlag = CodeMapUtil.getDynamicConstantValue(ConstantUtil.SAVE_SEVICE_LOG_FLAG);

            if(params instanceof Object[] && params.length > 0){
                logger.debug("------------------请求参数[SmoServiceAspect.aroundMethod]："+params[0]);
                //调用保存请求报文队列
                if("ON".equals(saveServiceLogFlag)){

                }
            }
            if(obj instanceof String){
                logger.debug("------------------返回参数[SmoServiceAspect.aroundMethod]："+obj);
                //调用保存返回报文队列
                if("ON".equals(saveServiceLogFlag)){

                }
            }
        }
        return obj;
    }
     */
}
