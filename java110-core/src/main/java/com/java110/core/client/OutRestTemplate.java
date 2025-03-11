package com.java110.core.client;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.LogFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.log.TransactionOutLogDto;
import com.java110.intf.common.ITransactionOutLogV1ServiceSMO;
import com.java110.po.log.TransactionOutLogPo;
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
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.net.URI;
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
            LogFactory.saveOutLog(url, "POST", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null,
                    (requestEntity != null && requestEntity.getBody() != null) ? requestEntity.getBody().toString() : "",
                    tmpResponseEntity);

        }
        return responseEntity;
    }


    @Override
    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request,
                                               Class<T> responseType, Object... uriVariables) throws RestClientException {
        String errMsg = "";

        ResponseEntity<T> responseEntity = null;
        ResponseEntity tmpResponseEntity = null;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:{},request:{},uriVariables:{}", url, "POST", request, uriVariables);
            responseEntity = super.postForEntity(url, request, responseType, uriVariables);
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
            //  saveLog(url, "POST", null, tmpResponseEntity, DateUtil.getCurrentDate().getTime() - startTime.getTime());

            LogFactory.saveOutLog(url, "POST", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null, request.toString(), tmpResponseEntity);
        }
        return responseEntity;
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        String errMsg = "";

        ResponseEntity<T> responseEntity = null;
        ResponseEntity tmpResponseEntity = null;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:{},uriVariables:{}", url, "POST", uriVariables);
            responseEntity = super.getForEntity(url, responseType, uriVariables);
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
            //  saveLog(url, "POST", null, tmpResponseEntity, DateUtil.getCurrentDate().getTime() - startTime.getTime());

            LogFactory.saveOutLog(url, "GET", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null, "", tmpResponseEntity);
        }
        return responseEntity;
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        String errMsg = "";
        T resMsg;
        Date startTime = DateUtil.getCurrentDate();
        try {
            logger.debug("请求信息：url:{},method:GET", url);
             resMsg = super.getForObject(url, responseType,uriVariables);
            logger.debug("返回信息：responseEntity:{}", resMsg);
        } catch (HttpStatusCodeException e) {
            errMsg = ExceptionUtil.getStackTrace(e);
            throw e;
        } finally {
              ResponseEntity  tmpResponseEntity = new ResponseEntity(errMsg, HttpStatus.OK);
            //  saveLog(url, "POST", null, tmpResponseEntity, DateUtil.getCurrentDate().getTime() - startTime.getTime());
            LogFactory.saveOutLog(url, "GET", DateUtil.getCurrentDate().getTime() - startTime.getTime(), null, "", tmpResponseEntity);
        }
        return resMsg;
    }


}
