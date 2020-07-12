package com.java110.intf.code;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ICodeApi
 * @Description 编码生成服务
 * @Author wuxw
 * @Date 2019/4/23 9:09
 * @Version 1.0
 * add by wuxw 2019/4/23
 **/
@FeignClient("code-service")
public interface ICodeApi {

    /**
     * 生成编码
     * @param orderInfo 订单信息
     * @param request request对象
     * @return 编码对象
     */
    @RequestMapping(path = "/codeApi/generate", method = RequestMethod.POST)
    String generatePost(@RequestBody String orderInfo, HttpServletRequest request);

    /**
     * 生成 编码
     *
     * @param prefix 前缀
     * @return 编码
     */
    @RequestMapping(value = "/codeApi/generateCode", method = RequestMethod.POST)
    String generateCode(@RequestParam("prefix") String prefix);
}
