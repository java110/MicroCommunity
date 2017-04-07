package com.java110.feign.user;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用用户服务失败时，返回
 * Created by wuxw on 2017/4/5.
 */
public class UserServiceFallback implements IUserService{
    /**
     * 查询用户信息失败
     * @param userJson
     * @return
     */
    @Override
    public String queryUserInfo(@RequestParam("userJson") String userJson) {
        return null;
    }
}
