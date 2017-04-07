package com.java110.user.rest;

import com.java110.user.smo.IUserServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务提供类
 * Created by wuxw on 2017/4/5.
 */
@RestController
public class UserServiceRest {

    @Autowired
    IUserServiceSMO iUserServiceSMO;

    /**
     * 通过User对象中数据查询用户信息
     * 如,用户ID，名称
     * @param userJson
     * @return
     */
    @RequestMapping("/userService/queryUserInfo")
    public String queryUserInfo(@RequestParam("userJson") String userJson){
        return null;
    }


    public IUserServiceSMO getiUserServiceSMO() {
        return iUserServiceSMO;
    }

    public void setiUserServiceSMO(IUserServiceSMO iUserServiceSMO) {
        this.iUserServiceSMO = iUserServiceSMO;
    }
}
