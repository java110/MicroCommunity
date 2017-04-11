package com.java110.feign.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
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
     * @param data
     * @return
     */
    @RequestMapping("/userService/queryUserInfo")
    public String queryUserInfo(@RequestParam("data") String data);


    /**
     * 用户服务信息受理
     * 协议：
     * {
     *     'boCust':[{}],
     *     'boCustAttr':[{}]
     * }
     * @param data
     * @return
     */
    @RequestMapping("/userService/soUserService")
    public String soUserService(@RequestParam("data") String data);


    /**
     * 客户信息处理
     * 协议：
     *{
     *     boCust:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/userService/soBoCust")
    public String soBoCust(@RequestParam("data") String data ) ;

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boCustAttr:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/userService/soBoCustAttr")
    public String soBoCustAttr(@RequestParam("data") String data) ;

}
