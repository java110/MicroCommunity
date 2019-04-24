package com.java110.core.feign;


import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserErrorDecoder implements ErrorDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public Exception decode(String methodKey, Response response) {

        Exception exception = null;
        try {
            String json = Util.toString(response.body().asReader());

            logger.error("调用方法出现异常了："+json);
            exception = new RuntimeException(json);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        // 这里只封装4开头的请求异常ß
        if (400 <= response.status() && response.status() < 600){
            exception = new HystrixBadRequestException("request exception wrapper", exception);
        }else{
            logger.error(exception.getMessage(), exception);
        }
        return exception;
    }
}