package com.java110.core.client;

import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import java.net.URI;

/**
 * @author wux
 * @create 2019-02-02 下午8:28
 * @desc 对RestTemplate类封装
 **/
public class RestTemplate extends org.springframework.web.client.RestTemplate {

    private static Logger logger = LoggerFactory.getLogger(RestTemplate.class);

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

        logger.debug("请求信息：url:{},method:{},request:{},uriVariables:{}", url, method, requestEntity, uriVariables);
        ResponseEntity<T> responseEntity = super.exchange(url, method, requestEntity, responseType, uriVariables);
        logger.debug("返回信息：responseEntity:{}", responseEntity);

        return responseEntity;
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request,
                                               Class<T> responseType, Object... uriVariables) throws RestClientException {

        logger.debug("请求信息：url:{},method:{},request:{},uriVariables:{}", url, request, uriVariables);
        ResponseEntity<T> responseEntity = super.postForEntity(url, request, responseType, uriVariables);
        logger.debug("返回信息：responseEntity:{}", responseEntity);
        return responseEntity;
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        logger.debug("请求信息：url:{},method:GET,uriVariables:{}", url, uriVariables);
        ResponseEntity<T> responseEntity = super.getForEntity(url, responseType, uriVariables);
        logger.debug("返回信息：responseEntity:{}", responseEntity);
        return responseEntity;
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {

        logger.debug("请求信息：url:{},method:GET", url);
        T responseEntity = super.getForObject(url, responseType,uriVariables);
        logger.debug("返回信息：responseEntity:{}", responseEntity);
        return responseEntity;
    }

}
