package com.java110.core.smo.user;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务接口类（供服务间调用）
 */
@FeignClient("user-service")
@RequestMapping("/userApi")
public interface IUserInnerServiceSMO {

    /**
     * 查询用户服务版本
     * @param code
     * @return
     */
    @RequestMapping(value = "/getUserServiceVersion",method = RequestMethod.GET)
    String getUserServiceVersion(@RequestParam("code") String code);
}
