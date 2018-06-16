package com.java110.feign.code;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping(value = "/codeApi/generateCode",method = RequestMethod.POST)
    public String generateCode(@RequestParam("prefix") String prefix);
}
