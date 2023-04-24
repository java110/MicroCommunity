package com.java110.core.client;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.transactionLog.TransactionOutLogDto;
import com.java110.intf.common.ITransactionOutLogV1ServiceSMO;
import com.java110.po.transactionOutLog.TransactionOutLogPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.Date;

/**
 * 该类只要负责调用外部资源
 *
 * @author wux
 * @create 2019-02-02 下午8:28
 * @desc 对RestTemplate类封装
 **/
public class OutRestTemplate extends RestTemplate {

    private static Logger logger = LoggerFactory.getLogger(OutRestTemplate.class);

    // exchange

    /**
     * 重写spring RestTemplate类 加入日志等信息
     *
     * @param url
     * @param method
     * @param requestEntity
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     * @throws RestClientException
     */
    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {

        String errMsg = "";

        ResponseEntity<T> responseEntity = null;
        ResponseEntity tmpResponseEntity = null;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:{},request:{},uriVariables:{}", url, method, requestEntity, uriVariables);
            responseEntity = super.exchange(url, method, requestEntity, responseType, uriVariables);
            logger.debug("返回信息：responseEntity:{}", responseEntity);

        } catch (HttpStatusCodeException e) {
            errMsg = ExceptionUtil.getStackTrace(e);
            throw e;
        } finally {

            if (responseEntity != null) {
                tmpResponseEntity = new ResponseEntity(responseEntity.getBody(), responseEntity.getStatusCode());
            } else {
                tmpResponseEntity = new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
            }
            saveLog(url, method.name(), requestEntity, tmpResponseEntity, DateUtil.getCurrentDate().getTime() - startTime.getTime());
        }
        return responseEntity;
    }


    private void saveLog(String url, String method, HttpEntity<?> requestEntity, ResponseEntity<String> responseEntity, long costTime) {

        String logServiceCode = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,MappingCache.CALL_OUT_LOG);

        if(StringUtil.isEmpty(logServiceCode) || "OFF".equalsIgnoreCase(logServiceCode) || url.startsWith(ServiceConstant.BOOT_SERVICE_ORDER_URL)){
            return;
        }



        ITransactionOutLogV1ServiceSMO transactionOutLogV1InnerServiceSMO = null;

        try {
            transactionOutLogV1InnerServiceSMO
                    = ApplicationContextFactory.getBean(ITransactionOutLogV1ServiceSMO.class.getName(), ITransactionOutLogV1ServiceSMO.class);
        }catch (Exception e){
            transactionOutLogV1InnerServiceSMO
                    = ApplicationContextFactory.getBean("transactionOutLogV1ServiceSMOImpl",ITransactionOutLogV1ServiceSMO.class);
        }
        if(transactionOutLogV1InnerServiceSMO == null){
            transactionOutLogV1InnerServiceSMO
                    = ApplicationContextFactory.getBean("transactionOutLogV1ServiceSMOImpl",ITransactionOutLogV1ServiceSMO.class);
        }

        TransactionOutLogPo transactionOutLogPo = new TransactionOutLogPo();

        transactionOutLogPo.setCostTime(costTime + "");
        transactionOutLogPo.setLogId(GenerateCodeFactory.getGeneratorId("11"));
        transactionOutLogPo.setRequestHeader(requestEntity.getHeaders() == null ? "" : requestEntity.getHeaders().toSingleValueMap().toString());
        transactionOutLogPo.setRequestMessage(requestEntity.getBody() == null ? "" : requestEntity.getBody().toString());
        transactionOutLogPo.setRequestMethod(method);
        transactionOutLogPo.setRequestUrl(url);
        transactionOutLogPo.setResponseHeader(responseEntity.getHeaders() == null ? "" : responseEntity.getHeaders().toSingleValueMap().toString());
        transactionOutLogPo.setResponseMessage(responseEntity.getBody() == null ? "": responseEntity.getBody().toString());
        transactionOutLogPo.setState(responseEntity.getStatusCode() == HttpStatus.OK ? TransactionOutLogDto.STATE_S:TransactionOutLogDto.STATE_F);

        transactionOutLogV1InnerServiceSMO.saveTransactionOutLog(transactionOutLogPo);
    }

}
