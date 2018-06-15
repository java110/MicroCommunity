package com.java110.feign.code;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by wuxw on 2018/6/15.
 */
@FeignClient(name = "code-service", fallback = CodeApiFallback.class)
public interface ICodeApi {

    /**
     * 生成 编码
     * @param prefix 前缀
     * @return
     */
    public String generateCode(String prefix);
}
