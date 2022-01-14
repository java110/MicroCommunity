package com.java110.core.client;

import com.java110.core.log.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

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

}
