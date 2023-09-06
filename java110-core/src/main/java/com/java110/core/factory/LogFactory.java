package com.java110.core.factory;

import com.java110.dto.log.TransactionOutLogDto;
import com.java110.intf.common.ITransactionOutLogV1ServiceSMO;
import com.java110.po.log.TransactionOutLogPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LogFactory {


    /**
     * 保存对外交互日志
     *
     * @param url            地址
     * @param reqStr         请求参数
     * @param responseEntity 返回参数
     */
    public static void saveOutLog(String url, String reqStr, ResponseEntity responseEntity) {
        saveOutLog(url, "无", 0, null, reqStr, responseEntity);
    }

    /**
     * 保存对外交互日志
     *
     * @param url            地址
     * @param method         方法
     * @param reqStr         请求参数
     * @param responseEntity 返回参数
     */
    public static void saveOutLog(String url, String method, String reqStr, ResponseEntity responseEntity) {
        saveOutLog(url, method, 0, null, reqStr, responseEntity);

    }

    /**
     * 保存对外交互日志
     *
     * @param url            地址
     * @param method         方法
     * @param costTime       所需时间
     * @param reqHeaders     请求头
     * @param reqStr         请求参数
     * @param responseEntity 返回参数
     */
    public static void saveOutLog(String url, String method, long costTime, HttpHeaders reqHeaders, String reqStr, ResponseEntity responseEntity) {
        try {
            String logServiceCode = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH, MappingCache.CALL_OUT_LOG);

            if (StringUtil.isEmpty(logServiceCode) || "OFF".equalsIgnoreCase(logServiceCode) || url.startsWith(ServiceConstant.BOOT_SERVICE_ORDER_URL)) {
                return;
            }
            ITransactionOutLogV1ServiceSMO transactionOutLogV1InnerServiceSMO = null;
            try {
                transactionOutLogV1InnerServiceSMO
                        = ApplicationContextFactory.getBean(ITransactionOutLogV1ServiceSMO.class.getName(), ITransactionOutLogV1ServiceSMO.class);
            } catch (Exception e) {
                transactionOutLogV1InnerServiceSMO
                        = ApplicationContextFactory.getBean("transactionOutLogV1ServiceSMOImpl", ITransactionOutLogV1ServiceSMO.class);
            }
            if (transactionOutLogV1InnerServiceSMO == null) {
                transactionOutLogV1InnerServiceSMO
                        = ApplicationContextFactory.getBean("transactionOutLogV1ServiceSMOImpl", ITransactionOutLogV1ServiceSMO.class);
            }

            TransactionOutLogPo transactionOutLogPo = new TransactionOutLogPo();
            //todo 分析日志类型
            String logType = analysisLogType(url);

            transactionOutLogPo.setCostTime(costTime + "");
            transactionOutLogPo.setLogId(GenerateCodeFactory.getGeneratorId("11"));
            transactionOutLogPo.setRequestHeader(reqHeaders == null ? "{}" : reqHeaders.toSingleValueMap().toString());
            transactionOutLogPo.setRequestMessage(reqStr);
            transactionOutLogPo.setRequestMethod(method);
            transactionOutLogPo.setRequestUrl(url);
            transactionOutLogPo.setResponseHeader(responseEntity.getHeaders() == null ? "" : responseEntity.getHeaders().toSingleValueMap().toString());
            transactionOutLogPo.setResponseMessage(responseEntity.getBody().toString());
            transactionOutLogPo.setState(responseEntity.getStatusCode() == HttpStatus.OK ? TransactionOutLogDto.STATE_S : TransactionOutLogDto.STATE_F);
            transactionOutLogPo.setLogType(logType);

            transactionOutLogV1InnerServiceSMO.saveTransactionOutLog(transactionOutLogPo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 分析日志类型
     *
     * @param url
     * @return
     */
    private static String analysisLogType(String url) {

        if (url.contains("9999") || url.contains("iot") || url.contains("extApi")) {
            return TransactionOutLogDto.LOG_TYPE_IOT;
        }

        if (url.contains("api.mch.weixin.qq.com")) {
            return TransactionOutLogDto.LOG_TYPE_WECHAT_PAY;
        }

        if (url.contains("api.weixin.qq.com")) {
            return TransactionOutLogDto.LOG_TYPE_WECHAT;
        }

        if(TransactionOutLogDto.LOG_TYPE_SMS.equals(url)){
            return TransactionOutLogDto.LOG_TYPE_SMS;
        }

        if(TransactionOutLogDto.LOG_TYPE_OSS.equals(url)){
            return TransactionOutLogDto.LOG_TYPE_OSS;
        }

        return TransactionOutLogDto.LOG_TYPE_DEFAULT;
    }
}
