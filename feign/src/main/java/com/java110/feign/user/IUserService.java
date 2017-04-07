package com.java110.feign.user;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 用户服务接口
 * 用户查询，修改，删除 实现
 * Created by wuxw on 2017/4/5.
 */
@FeignClient(name = "user-service", fallback = UserServiceFallback.class)
public interface IUserService {

    /**
     * 通过User对象中数据查询用户信息
     * 如,用户ID，名称
     * @param userJson
     * @return
     */
    @RequestMapping("/userService/queryUserInfo")
    public String queryUserInfo(@RequestParam("userJson") String userJson);
}
