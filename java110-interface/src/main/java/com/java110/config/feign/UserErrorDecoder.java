package com.java110.config.feign;


import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 自定义异常解析
 */
public class UserErrorDecoder implements ErrorDecoder {

    private static final int HTTP_STATUS_400 = 400;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 异常解析 在 SynchronousMethodHandler 类 134 号调用
     *
     * @param methodKey 方法key
     * @param response  返回对象
     * @return 返回异常
     */
    public Exception decode(String methodKey, Response response) {

        Exception exception = null;
        try {
            String json = Util.toString(response.body().asReader());

            logger.error("调用方法出现异常了：" + json);
            exception = new RuntimeException(json);
            // 这里只封装4开头的请求异常ß && response.status() < 500
            if (HTTP_STATUS_400 <= response.status()) {
                exception = new HystrixBadRequestException("请求参数错误：" + json, exception);
            } else {
                logger.error(exception.getMessage(), exception);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }


        return exception;
    }
}
